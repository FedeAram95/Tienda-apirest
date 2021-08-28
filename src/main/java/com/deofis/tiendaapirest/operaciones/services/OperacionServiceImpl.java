package com.deofis.tiendaapirest.operaciones.services;

import com.deofis.tiendaapirest.autenticacion.exceptions.AutenticacionException;
import com.deofis.tiendaapirest.autenticacion.services.AutenticacionService;
import com.deofis.tiendaapirest.catalogo.skus.entities.Sku;
import com.deofis.tiendaapirest.catalogo.skus.services.SkuService;
import com.deofis.tiendaapirest.checkout.dto.CheckoutPayload;
import com.deofis.tiendaapirest.checkout.services.CheckoutService;
import com.deofis.tiendaapirest.clientes.entities.Cliente;
import com.deofis.tiendaapirest.emails.dto.CompNotificacionEmail;
import com.deofis.tiendaapirest.emails.dto.EmailBody;
import com.deofis.tiendaapirest.emails.services.MailService;
import com.deofis.tiendaapirest.notificaciones.services.NotificationSender;
import com.deofis.tiendaapirest.notificaciones.services.factory.NotificationSenderFactory;
import com.deofis.tiendaapirest.operaciones.dto.OperacionRequest;
import com.deofis.tiendaapirest.operaciones.entities.DetalleOperacion;
import com.deofis.tiendaapirest.operaciones.entities.EstadoOperacion;
import com.deofis.tiendaapirest.operaciones.entities.EventoOperacion;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import com.deofis.tiendaapirest.operaciones.exceptions.OperacionException;
import com.deofis.tiendaapirest.operaciones.notifications.NotificationSenderEnum;
import com.deofis.tiendaapirest.operaciones.repositories.OperacionRepository;
import com.deofis.tiendaapirest.operaciones.statemachine.StateMachineService;
import com.deofis.tiendaapirest.pagos.entities.MedioPago;
import com.deofis.tiendaapirest.pagos.entities.MedioPagoEnum;
import com.deofis.tiendaapirest.pagos.factory.OperacionPagoInfo;
import com.deofis.tiendaapirest.pagos.mapper.OperacionPagoMapper;
import com.deofis.tiendaapirest.pagos.repositories.MedioPagoRepository;
import com.deofis.tiendaapirest.perfiles.entities.Perfil;
import com.deofis.tiendaapirest.perfiles.services.PerfilService;
import com.deofis.tiendaapirest.utils.rounding.RoundService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class OperacionServiceImpl implements OperacionService {
    public static final String NRO_OPERACION_HEADER = "nroOperacion";

    private final StateMachineService stateMachineService;

    private final AutenticacionService autenticacionService;

    @Qualifier("operacionMailService") private final MailService mailService;
    // private final NotificationSender notificationSender;
    private final NotificationSenderFactory notificationSenderFactory;

    private final OperacionRepository operacionRepository;
    private final MedioPagoRepository medioPagoRepository;
    private final SkuService skuService;
    private final PerfilService perfilService;

    private final CheckoutService checkoutService;

    private final OperacionPagoMapper operacionPagomapper;
    private final RoundService roundService;
    private final ValidadorItems validadorItems;

    private final String clientUrl;

    @Transactional
    @Override
    public OperacionPagoInfo registrarNuevaOperacion(Operacion operacion) {
        this.validarAutenticacion();
        Cliente cliente = this.perfilService.obtenerDatosCliente();
        MedioPago medioPago = this.obtenerMedioPagoGuardado(operacion.getMedioPago().getId());

        Operacion nuevaOperacion = Operacion.builder()
                .cliente(cliente)
                .direccionEnvio(operacion.getDireccionEnvio())
                .fechaOperacion(new Date(new Date().getTime()))
                .fechaEnvio(null)
                .fechaEntrega(null)
                .medioPago(medioPago)
                .pago(null)
                .estado(EstadoOperacion.PAGO_PENDIENTE)
                .total(0.0)
                .items(operacion.getItems())
                .build();

        boolean hayDisponibilidad = true;
        boolean hayItemSinCantidad = false;
        boolean itemNoVendible = false;
        for (DetalleOperacion item: operacion.getItems()) {
            Sku sku = this.skuService.obtenerSku(item.getSku().getId());
            // Validamos que el SKU no sea default sku si su producto NO es vendible sin propiedades (no tiene
            // skus adicionales)
            if (this.validadorItems.esItemNoVendible(sku) || !sku.isActivo()) {
                itemNoVendible = true;
                break;
            }

            // Seteamos el SKU completo al item (lo que esta guardado en la BD).
            item.setSku(sku);

            if (sku.getDisponibilidad() - item.getCantidad() < 0) {
                hayDisponibilidad = false;
                break;
            }

            if (item.getCantidad() <= 0) {
                hayItemSinCantidad = true;
                break;
            }

            // Calculamos el precio de venta el producto (sku) de acuerdo a si está en promoción o no.
            item.setPrecioVenta(this.calcularPrecioVenta(sku));

            // Calculamos y guardamos el subtotal del item.
            item.setSubtotal(item.getPrecioVenta() * item.getCantidad().doubleValue());

            // Calculamos dentro del ciclo el TOTAL de la operación, para evitarnos calcularlo fuera y volverlo
            // a recorrer. Redondeamos el total y guardamos.
            nuevaOperacion.setTotal(this.roundService.round(this.calcularTotal(nuevaOperacion.getTotal(),
                    item.getSubtotal())));
        }

        // Si no hay disponibilidad de un producto, se cancela la operación y tira excepción informando.
        if (!hayDisponibilidad) throw new OperacionException("Error al completar la compra: " +
                "La cantidad de productos vendidos no puede ser menor a la disponibilidad actual");

        if (hayItemSinCantidad)
            throw new OperacionException("Error al completar la compra: La cantidad de productos no puede ser" +
                    " menor o igual que 0");

        if (itemNoVendible)
            throw new OperacionException("Hay al menos un item no vendible: algún item posee un sku por defecto y" +
                    " el producto tiene skus adicionales ó algun SKU no está activo para la venta.");

        this.save(nuevaOperacion);

        // Agregar nueva operación (COMPRA) al array de compras del perfil del usuario.
        this.agregarCompraAlPerfil(nuevaOperacion);

        log.info("total guardado -> " + nuevaOperacion.getTotal());
        // Delegar la creación del PAGO de operación al PagoStrategy correspondiente.
        OperacionPagoInfo operacionPagoInfo = this.checkoutService.iniciarCheckout(nuevaOperacion);

        // Persistir el pago creado y pendiente de pagar asociado a la operación recientemente registrada.
        this.guardarOperacionPago(nuevaOperacion, operacionPagoInfo);

        // NOTIFICACIONES E EMAILS --> Proponer utilizar OBSERVER para desacoplar OperacionService de EmailService y
        // Notificacionservice.
        // Llamamos a los métodos para enviar mails al usuario final.
        this.enviarEmailUsuario(nuevaOperacion, cliente.getEmail());

        // Enviar la notificación al usuario que realizó la compra
        this.enviarNotificacionUsuario(nuevaOperacion);
        return operacionPagoInfo;
    }

    /**
     * Método auxiliar para enviar notificación al registrar nueva operación. Se encarga de utilizar
     * el objeto adecuado de {@link NotificationSender}, para delegar a dicho objeto el envío de la
     * notificación.
     * @param nuevaOperacion {@link Operacion} nueva registrada.
     */
    private void enviarNotificacionUsuario(Operacion nuevaOperacion) {
        // Medios de pago en tienda o trans. bancaria no producen envío email al usuario al crear objeto operacion.
        if (nuevaOperacion.getMedioPago().getNombre().equals(MedioPagoEnum.EFECTIVO) ||
                nuevaOperacion.getMedioPago().getNombre().equals(MedioPagoEnum.TRANSFERENCIA_BANCARIA)) return;

        String title = "¡Nueva compra registrada!";
        String actionUrl = this.clientUrl.concat("/detalle/") + nuevaOperacion.getNroOperacion();
        String user = nuevaOperacion.getCliente().getEmail();

        NotificationSender notificationSender = this.notificationSenderFactory.get(String.valueOf(NotificationSenderEnum.nuevaOperacionSender));
        notificationSender.sendNotification(title, nuevaOperacion, actionUrl, user);
    }

    private void agregarCompraAlPerfil(Operacion operacion) {
        Perfil perfil = this.perfilService.obtenerPerfil();
        perfil.getCompras().add(operacion);
        this.perfilService.vaciarCarrito();
        this.perfilService.save(perfil);
    }

    private MedioPago obtenerMedioPagoGuardado(Long medioPagoId) {
        return this.medioPagoRepository.findById(medioPagoId)
                .orElseThrow(() -> new OperacionException("Medio de pago no implementado en el sistema"));
    }

    private void validarAutenticacion() {
        if (!this.autenticacionService.estaLogueado())
            throw new AutenticacionException("Usuario no logueado en el sistema");
    }

    @Transactional
    @Override
    public OperacionPagoInfo registrarComprarYa(OperacionRequest operacionRequest) {
        Cliente clienteActual = this.perfilService.obtenerDatosCliente();
        MedioPago medioPago = this.obtenerMedioPagoGuardado(operacionRequest.getMedioPago().getId());

        Operacion nuevaOperacion = Operacion.builder()
                .cliente(clienteActual)
                .direccionEnvio(operacionRequest.getDireccionEnvio())
                .medioPago(medioPago)
                .items(new ArrayList<>())
                .build();

        nuevaOperacion.getItems().add(operacionRequest.getItem());
        return this.registrarNuevaOperacion(nuevaOperacion);
    }

    @Transactional
    @Override
    public Operacion enviar(Long nroOperacion) {
        Operacion operacion = this.findById(nroOperacion);

        if (!operacion.getEstado().equals(EstadoOperacion.PAGO_CONFIRMADO))
            throw new OperacionException("No se pueden enviar operaciones que no estén en el estado pago confirmado");

        StateMachine<EstadoOperacion, EventoOperacion> sm = this.stateMachineService.build(nroOperacion);

        sm.getExtendedState().getVariables().put("operacion", operacion);
        sm.getExtendedState().getVariables().put("useremail", operacion.getCliente().getEmail());

        this.stateMachineService.enviarEvento(nroOperacion, sm, EventoOperacion.ENVIAR);
        return operacion;
    }

    @Transactional
    @Override
    public Operacion entregar(Long nroOperacion) {
        Operacion operacion = this.findById(nroOperacion);

        // Si el medio de pago es EFECTIVO, la entrega completa el CHECKOUT
        if (operacion.getMedioPago().getNombre().equals(MedioPagoEnum.EFECTIVO)) {
            if (!operacion.getEstado().equals(EstadoOperacion.EN_RESERVA))
                throw new OperacionException("No se pueden registrar entregadas aquelas operaciones que no estén en el estado " +
                        "EN_RESERVA. Sólo para medio de pago en efectivo.");

            // Necesitamos el objeto para completar el pago, ya que registrar una operación como entregada, implica
            // que se completa su pago en efectivo.
            CheckoutPayload checkoutPayload = CheckoutPayload.builder()
                    .nroOperacion(operacion.getNroOperacion())
                    .paymentId(operacion.getPago().getId())
                    .preferenceId("").build();

            this.checkoutService.ejecutarCheckoutSuccess(checkoutPayload);
            return operacion;
        }

        if (!operacion.getEstado().equals(EstadoOperacion.ENVIADO))
            throw new OperacionException("No se puede registrar una operación como recibida si no están en el estado" +
                    " enviado");

        StateMachine<EstadoOperacion, EventoOperacion> sm = this.stateMachineService.build(nroOperacion);

        sm.getExtendedState().getVariables().put("operacion", operacion);
        sm.getExtendedState().getVariables().put("useremail", operacion.getCliente().getEmail());

        this.stateMachineService.enviarEvento(nroOperacion, sm, EventoOperacion.ENTREGAR);
        return operacion;
    }

    @Transactional
    @Override
    public Operacion confirmarPago(Long nroOperacion) {
        Operacion operacion = this.findById(nroOperacion);

        if (!operacion.getEstado().equals(EstadoOperacion.COMPROBADO))
            throw new OperacionException("No se pueden confirmar pagos para operaciones que no están en estado comprobado");

        CheckoutPayload checkoutPayload = CheckoutPayload.builder()
                .nroOperacion(operacion.getNroOperacion())
                .paymentId(operacion.getPago().getId())
                .preferenceId("").build();

        OperacionPagoInfo pagoInfo = this.checkoutService.ejecutarCheckoutSuccess(checkoutPayload);
        log.info(pagoInfo.toString());
        return operacion;
    }

    @Transactional
    @Override
    public Operacion rechazarComprobantePago(Long nroOperacion) {
        Operacion operacion = this.findById(nroOperacion);

        if (!operacion.getEstado().equals(EstadoOperacion.COMPROBADO))
            throw new OperacionException("No se puede rechazar un comprobante de pago si la operación no se encuentra " +
                    "en estado comprobado");

        StateMachine<EstadoOperacion, EventoOperacion> sm = this.stateMachineService.build(nroOperacion);
        sm.getExtendedState().getVariables().put("operacion", operacion);
        sm.getExtendedState().getVariables().put("useremail", operacion.getCliente().getEmail());

        this.stateMachineService.enviarEvento(nroOperacion, sm, EventoOperacion.REGISTRAR_RESERVA);
        return operacion;
    }

    @Transactional
    @Override
    public Operacion cancelar(Long nroOperacion) {
        Operacion operacion = this.findById(nroOperacion);

        List<EstadoOperacion> estadosAceptados = new ArrayList<>();
        estadosAceptados.add(EstadoOperacion.PAGO_PENDIENTE);
        estadosAceptados.add(EstadoOperacion.PAGO_RECHAZADO);
        if (!estadosAceptados.contains(operacion.getEstado()))
            throw new OperacionException("No se pueden cancelar operaciones que no estén en estado pago pendiente/rechazado");

        StateMachine<EstadoOperacion, EventoOperacion> sm = this.stateMachineService.build(nroOperacion);

        sm.getExtendedState().getVariables().put("operacion", operacion);
        sm.getExtendedState().getVariables().put("useremail", operacion.getCliente().getEmail());

        this.stateMachineService.enviarEvento(nroOperacion, sm, EventoOperacion.CANCELAR);
        return operacion;
    }

    @Transactional
    @Override
    public Operacion anular(Long nroOperacion) {
        Operacion operacion = this.findById(nroOperacion);

        if (!operacion.getEstado().equals(EstadoOperacion.EN_RESERVA))
            throw new OperacionException("No se pueden anular operaciones que no estén en el estado EN_RESERVA");

        StateMachine<EstadoOperacion, EventoOperacion> sm = this.stateMachineService.build(nroOperacion);

        sm.getExtendedState().getVariables().put("operacion", operacion);
        sm.getExtendedState().getVariables().put("useremail", operacion.getCliente().getEmail());

        this.stateMachineService.enviarEvento(nroOperacion, sm, EventoOperacion.ANULAR);
        return operacion;
    }

    @Transactional
    @Override
    public Operacion registrarDevolucion(Long nroOperacion) {
        Operacion operacion = this.findById(nroOperacion);

        if (!operacion.getEstado().equals(EstadoOperacion.EN_DEVOLUCION))
            throw new OperacionException("No se pueden devolver operaciones que no estén en el estado EN_DEVOLUCION");

        StateMachine<EstadoOperacion, EventoOperacion> sm = this.stateMachineService.build(nroOperacion);

        sm.getExtendedState().getVariables().put("operacion", operacion);
        sm.getExtendedState().getVariables().put("useremail", operacion.getCliente().getEmail());

        this.stateMachineService.enviarEvento(nroOperacion, sm, EventoOperacion.REGISTRAR_DEVOLUCION);
        return operacion;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Operacion> findAll() {
        return this.operacionRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Operacion findById(Long id) {
        return this.operacionRepository.findById(id)
                .orElseThrow(() -> new OperacionException("No existe la operacion con numero: " + id));
    }

    @Transactional
    @Override
    public Operacion save(Operacion object) {
        return this.operacionRepository.save(object);
    }

    @Override
    public void delete(Operacion object) {
        System.out.println("Método no soportado... TODO --> segregación de interfaces");
    }

    @Override
    public void deleteById(Long aLong) {
        System.out.println("Método no soportado... TODO --> segregación de interfaces");
    }

    /**
     * Método sencillo que calcula el total de una operación, con el total actual y el nuevo subtotal a sumar.
     * @param total Double total actual de la operación.
     * @param subTotal Double subtotal nuevo a añadir al total.
     * @return Double nuevo total de la operación.
     */
    private Double calcularTotal(Double total, Double subTotal) {
        return total + subTotal;
    }

    /**
     * Método que se encarga de completar el registro de una operación, seteando el nuevo pago, usando el
     * servicio de mapeo de {@link OperacionPagoInfo} --> {@link com.deofis.tiendaapirest.pagos.entities.OperacionPago}, y
     * seteando la fecha de creación del Pago (que es la fecha actual en el sistema).
     * @param nuevaOperacion {@link Operacion} nueva operación a guardar.
     * @param operacionPagoInfo {@link OperacionPagoInfo} objeto con los datos del Pago.
     */
    private void guardarOperacionPago(Operacion nuevaOperacion, OperacionPagoInfo operacionPagoInfo) {
        nuevaOperacion.setPago(this.operacionPagomapper.mapToOperacionPago(operacionPagoInfo));
        nuevaOperacion.getPago().setFechaCreacion(new Date());
        this.save(nuevaOperacion);
    }

    /**
     * Método que calcula el precio venta que tendrá un item ({@link DetalleOperacion}) en particular, teniendo en
     * cuenta si, al momento de registrar la operación, algún producto/sku se encuentra en promoción o no.
     * @param sku {@link Sku} que tiene los datos para setear el precio de venta.
     * @return Double con el precio de venta al momento de registrar la operación.
     */
    private Double calcularPrecioVenta(Sku sku) {
        if (sku.getPromocion() != null && sku.getPromocion().getEstaVigente())
            return sku.getPromocion().getPrecioOferta();

        return sku.getPrecio();
    }

    /**
     * Este método se encarga del envío de email al registrar una nueva operación para el usuario comprador.
     * @param operacion {@link Operacion} con los datos sobre la compra.
     * @param emailUsuario String con el email del usuario comprador, para efectuar el envío del email.
     */
    private void enviarEmailUsuario(Operacion operacion, String emailUsuario) {
        // Medios de pago en tienda o trans. bancaria no producen envío email al usuario al crear objeto operacion.
        if (operacion.getMedioPago().getNombre().equals(MedioPagoEnum.EFECTIVO) ||
                operacion.getMedioPago().getNombre().equals(MedioPagoEnum.TRANSFERENCIA_BANCARIA)) return;

        String redirectUrl = this.clientUrl.concat("/user-my-purchases");
        String subject = "Nueva compra registrada";
        String bodyTitle = "¡".concat(operacion.getCliente().getNombre()) + ", gracias por tu compra!";
        String bodyMessage = "¡Ya casi lo tenés! En caso de no haberlo hecho antes, completá tu pago desde el menú de " +
                "compras, al cual podés acceder desde el siguiente enlace:\n";

        EmailBody body = EmailBody.builder()
                .title(bodyTitle)
                .message(bodyMessage)
                .content(operacion).build();

        log.info(emailUsuario);

        CompNotificacionEmail notificacionEmail = CompNotificacionEmail.builder()
                .redirectUrl(redirectUrl)
                .subject(subject)
                .recipient(emailUsuario)
                .body(body).build();

        this.mailService.sendEmail(notificacionEmail);
    }
}
