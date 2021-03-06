package com.deofis.tiendaapirest.operaciones.statemachine;

import com.deofis.tiendaapirest.administracionusuarios.services.AdministradorUsuariosService;
import com.deofis.tiendaapirest.autenticacion.dto.UsuarioDTO;
import com.deofis.tiendaapirest.catalogo.skus.entities.Sku;
import com.deofis.tiendaapirest.catalogo.skus.services.SkuService;
import com.deofis.tiendaapirest.emails.dto.CompNotificacionEmail;
import com.deofis.tiendaapirest.emails.dto.EmailBody;
import com.deofis.tiendaapirest.emails.services.MailService;
import com.deofis.tiendaapirest.notificaciones.services.NotificationSender;
import com.deofis.tiendaapirest.notificaciones.services.factory.NotificationSenderFactory;
import com.deofis.tiendaapirest.operaciones.entities.DetalleOperacion;
import com.deofis.tiendaapirest.operaciones.entities.EstadoOperacion;
import com.deofis.tiendaapirest.operaciones.entities.EventoOperacion;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import com.deofis.tiendaapirest.operaciones.notifications.NotificationSenderEnum;
import com.deofis.tiendaapirest.pagos.config.ExpiracionPagoConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;

@Slf4j
@EnableStateMachineFactory
@AllArgsConstructor
@Configuration
public class StateMachineConfig extends StateMachineConfigurerAdapter<EstadoOperacion, EventoOperacion> {

    @Qualifier("operacionMailService") private final MailService mailService;
    private final NotificationSenderFactory notificationSenderFactory;
    private final AdministradorUsuariosService administradorUsuariosService;
    private final SkuService skuService;
    private final String clientUrl;


    @Override
    public void configure(StateMachineStateConfigurer<EstadoOperacion, EventoOperacion> states) throws Exception {
        states.withStates()
                .initial(EstadoOperacion.PAGO_PENDIENTE)
                .states(EnumSet.allOf(EstadoOperacion.class))
                //Como de ENTREGADO se puede pasar a EN_DEVOLUCION, no puede ser estado final
                // .end(EstadoOperacion.ENTREGADO)
                .end(EstadoOperacion.COMPLETADO)
                .end(EstadoOperacion.CANCELADO)
                .end(EstadoOperacion.ANULADO)
                .end(EstadoOperacion.DEVUELTO);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<EstadoOperacion, EventoOperacion> transitions) throws Exception {
        transitions
                .withExternal().source(EstadoOperacion.PAGO_PENDIENTE).target(EstadoOperacion.PAGO_CONFIRMADO).event(EventoOperacion.CONFIRMAR_PAGO).action(confirmarPago())
                .and()
                .withExternal().source(EstadoOperacion.PAGO_PENDIENTE).target(EstadoOperacion.PAGO_RECHAZADO).event(EventoOperacion.RECHAZAR_PAGO).action(rechazarPago())
                .and()
                .withExternal().source(EstadoOperacion.PAGO_PENDIENTE).target(EstadoOperacion.EN_RESERVA).event(EventoOperacion.REGISTRAR_RESERVA).action(registrarReserva())
                .and()
                .withExternal().source(EstadoOperacion.PAGO_PENDIENTE).target(EstadoOperacion.CANCELADO).event(EventoOperacion.CANCELAR).action(cancelar())
                .and()
                .withExternal().source(EstadoOperacion.PAGO_RECHAZADO).target(EstadoOperacion.PAGO_RECHAZADO).event(EventoOperacion.RECHAZAR_PAGO).action(rechazarPago())
                .and()
                .withExternal().source(EstadoOperacion.PAGO_RECHAZADO).target(EstadoOperacion.PAGO_CONFIRMADO).event(EventoOperacion.CONFIRMAR_PAGO).action(confirmarPago())
                .and()
                .withExternal().source(EstadoOperacion.PAGO_RECHAZADO).target(EstadoOperacion.CANCELADO).event(EventoOperacion.CANCELAR).action(cancelar())
                .and()
                .withExternal().source(EstadoOperacion.PAGO_CONFIRMADO).target(EstadoOperacion.ENVIADO).event(EventoOperacion.ENVIAR).action(enviar())
                .and()
                .withExternal().source(EstadoOperacion.PAGO_CONFIRMADO).target(EstadoOperacion.EN_DEVOLUCION).event(EventoOperacion.DEVOLVER).action(devolver())
                .and()
                .withExternal().source(EstadoOperacion.EN_RESERVA).target(EstadoOperacion.ENTREGADO).event(EventoOperacion.ENTREGAR).action(entregar())
                .and()
                .withExternal().source(EstadoOperacion.EN_RESERVA).target(EstadoOperacion.COMPROBADO).event(EventoOperacion.COMPROBAR_PAGO).action(comprobarPago())
                .and()
                .withExternal().source(EstadoOperacion.EN_RESERVA).target(EstadoOperacion.ANULADO).event(EventoOperacion.ANULAR).action(anular())
                .and()
                .withExternal().source(EstadoOperacion.COMPROBADO).target(EstadoOperacion.PAGO_CONFIRMADO).event(EventoOperacion.CONFIRMAR_PAGO).action(confirmarPagoTransferencia())
                .and()
                .withExternal().source(EstadoOperacion.COMPROBADO).target(EstadoOperacion.EN_RESERVA).event(EventoOperacion.REGISTRAR_RESERVA).action(rechazarComprobante())
                .and()
                .withExternal().source(EstadoOperacion.ENVIADO).target(EstadoOperacion.ENTREGADO).event(EventoOperacion.ENTREGAR).action(entregar())
                .and()
                .withExternal().source(EstadoOperacion.ENVIADO).target(EstadoOperacion.EN_DEVOLUCION).event(EventoOperacion.DEVOLVER).action(devolver())
                .and()
                .withExternal().source(EstadoOperacion.ENTREGADO).target(EstadoOperacion.EN_DEVOLUCION).event(EventoOperacion.DEVOLVER).action(devolver())
                .and()
                .withExternal().source(EstadoOperacion.EN_DEVOLUCION).target(EstadoOperacion.DEVUELTO).event(EventoOperacion.REGISTRAR_DEVOLUCION).action(registrarDevolucion())
                .and()
                .withExternal().source(EstadoOperacion.ENTREGADO).target(EstadoOperacion.COMPLETADO).event(EventoOperacion.COMPLETAR);
    }

    /**
     * Acci??n que se produce al transicionar: PAGO_PENDIENTE, PAGO_RECHAZADO --> PAGO_CONFIRMADO (CONFIRMAR_PAGO). Al completar
     * el pago se espera:
     *  - env??o de emails a todos los administradores y env??o de email al usuario comprador.
     *  - descontar disponibilidad de todos los productos de la {@link Operacion} correspondiente.
     * @return Action utilizada por la state machine.
     */
    public Action<EstadoOperacion, EventoOperacion> confirmarPago() {
        return stateContext -> {
            StateMachine<EstadoOperacion, EventoOperacion> sm = stateContext.getStateMachine();

            // Obtenemos la operaci??n y el email del usuario comprador
            Operacion operacion = sm.getExtendedState().get("operacion", Operacion.class);
            String useremail = sm.getExtendedState().get("useremail", String.class);

            // Recorremos los items para descontar disponibilidad de los skus de cada item
            this.descontarDisponibilidad(operacion.getItems());

            // L??gica de env??o de emails
            String redirectUrl;
            String subject;
            String bodyTitle;
            String bodyMessage;

            // Usuario comprador
            redirectUrl = this.clientUrl.concat("/user-my-purchases");
            subject = "Pago de compra completado";
            bodyTitle = "??".concat(operacion.getCliente().getNombre()) + ", tu pago ha sido procesado correctamente!";
            bodyMessage = "Tu pago fue confirmado con ??xito, ahora solo queda esperar a que envien tu compra.\n" +
                    "Por favor, revis?? los datos de la compra en tu perfil de usuario, por medio del siguiente enlace:";
            this.enviarEmailUsuario(redirectUrl, subject, bodyTitle, bodyMessage, operacion, useremail);

            // Administradores de la p??gina
            redirectUrl = this.clientUrl.concat("/detalle/venta/" + operacion.getNroOperacion());
            subject = "Nueva venta registrada en tu Tienda Online";
            bodyTitle = "Nueva venta registrada";
            bodyMessage = "Sr. Administrador, se ha regristrado una nueva venta en su tienda. Recuerde que la facturaci??n" +
                    " y su env??o al usuario debe hacerlo manualmente.\n" +
                    "Por favor, revise los datos de la venta accediendo al panel de administrador haciendo click en el siguiente enlace:";
            this.enviarEmailsAdmins(redirectUrl, subject, bodyTitle, bodyMessage, operacion);

            // L??gica notificaciones
            String notiTitle;
            String actionUrl;
            // User
            notiTitle = "??Pago completado con ??xito!";
            actionUrl = this.clientUrl.concat("/detalle/" + operacion.getNroOperacion());
            this.enviarNotificacionUsuario(NotificationSenderEnum.pagoRegistradoSender,notiTitle, operacion, actionUrl, useremail);

            // Admin
            notiTitle = "Nueva venta registrada";
            actionUrl = this.clientUrl.concat("/detalle/venta/" + operacion.getNroOperacion());
            this.enviarNotificacionesAdmins(NotificationSenderEnum.ventaSender,notiTitle, operacion, actionUrl);
        };
    }

    /**
     * Acci??n que se produce al transicionar: EN_RESERVA --> COMPROBADO. Al registrar el comprobante de un pago por transferencia
     * bancaria, se espera:
     *  - env??o email al usuario indicando que se subi?? el comprobante del pago.
     *  - env??o emails administradores indicando que el pago de una operaci??n fue completado.
     * @return Action utilizada por la state machine.
     */
    private Action<EstadoOperacion, EventoOperacion> comprobarPago() {
        return stateContext -> {
            StateMachine<EstadoOperacion, EventoOperacion> sm = stateContext.getStateMachine();

            Operacion operacion = sm.getExtendedState().get("operacion", Operacion.class);
            String useremail = sm.getExtendedState().get("useremail", String.class);

            String redirectUrl;
            String subject;
            String bodyTitle;
            String bodyMessage;

            // usuario comprador
            redirectUrl = this.clientUrl.concat("/user-my-purchases");
            subject = "Imagen de comprobante de pago subida";
            bodyTitle = "La imagen del comprobante de pago fue subida";
            bodyMessage = "El comprobante de pago fue realizado correctamente, quedando a la espera de que un administrador " +
                    "de nuestra tienda confirme que el comprobante es v??lido.\n" +
                    "Podes revisar tus compras accediendo desde el siguiente enlace:";
            this.enviarEmailUsuario(redirectUrl, subject, bodyTitle, bodyMessage, operacion, useremail);

            // administradores
            redirectUrl = this.clientUrl.concat("/detalle/venta/" + operacion.getNroOperacion());
            subject = "Comprobante de pago";
            bodyTitle = "Nuevo comprobante de pago registrado";
            bodyMessage = "Un usuario subi?? el comprobante de pago para una operaci??n. Recuerde que debe confirmar que dicho " +
                    "comprobante es v??lido, accediendo al panel de administrador y a la venta correspondiente.\n" +
                    "Acceda al panel de administrador desde el siguiente enlace:";
            this.enviarEmailsAdmins(redirectUrl, subject, bodyTitle, bodyMessage, operacion);

            // Env??o de notificaciones
            String notiTitle;
            String actionUrl;
            // Usuario comprador
            notiTitle = "Comprobante subido";
            actionUrl = this.clientUrl.concat("/detalle/" + operacion.getNroOperacion());
            this.enviarNotificacionUsuario(NotificationSenderEnum.comprobarPagoSender, notiTitle, operacion,
                    actionUrl, useremail);
            // Admin
            notiTitle = "Nuevo comprobante de pago";
            actionUrl = this.clientUrl.concat("/detalle/venta/" + operacion.getNroOperacion());
            this.enviarNotificacionesAdmins(NotificationSenderEnum.nuevoComprobanteSubidoSender, notiTitle, operacion,
                    actionUrl);
        };
    }

    /**
     * Acci??n que se produce al transitar: COMPROBADO --> EN_RESERVA. Se produce cuando un administrador de la tienda decide
     * que un comprobante de pago para un pago subdio no es v??lido.
     * Se espera:
     *  - Env??o de emails al usuario avisando del evento.
     * @return Action utilizada por la state machine.
     */
    private Action<EstadoOperacion, EventoOperacion> rechazarComprobante() {
        return stateContext -> {
            StateMachine<EstadoOperacion, EventoOperacion> sm = stateContext.getStateMachine();

            Operacion operacion = sm.getExtendedState().get("operacion", Operacion.class);
            String useremail = sm.getExtendedState().get("useremail", String.class);

            String redirectUrl;
            String subject;
            String bodyTitle;
            String bodyMessage;

            redirectUrl = this.clientUrl.concat("/detalle/" + operacion.getNroOperacion());
            subject = "Comprobante de pago rechazado";
            bodyTitle = "Tu comprobante de pago fue rechazado";
            bodyMessage = "El comprobante de pago para tu compra ha sido marcado como no v??lido por un administrador.\n" +
                    "Porfavor, ingres?? al siguiente enlace para subir el comprobante v??lido:";
            this.enviarEmailUsuario(redirectUrl, subject, bodyTitle, bodyMessage, operacion, useremail);

            // Env??o notificaciones
            String notiTitle = "Comprobante rechazado";
            String actionUrl = this.clientUrl.concat("/detalle/" + operacion.getNroOperacion());
            this.enviarNotificacionUsuario(NotificationSenderEnum.rechazarComprobanteSender, notiTitle, operacion,
                    actionUrl, useremail);
        };
    }

    /**
     * Acci??n que se produce al transicionar: COMPROBADO --> PAGO_CONFIRMADO. Al completar pago por transf. bancaria
     * se espera:
     * - env??o de emails usuario final.
     * (no se descuenta stock, ya fue descontado al pasar a EN_RESERVA (solo ef. y transf. bancaria)
     * @return Action utilizada por la state machine.
     */
    private Action<EstadoOperacion, EventoOperacion> confirmarPagoTransferencia() {
        return stateContext -> {
            StateMachine<EstadoOperacion, EventoOperacion> sm = stateContext.getStateMachine();

            Operacion operacion = sm.getExtendedState().get("operacion", Operacion.class);
            String useremail = sm.getExtendedState().get("useremail", String.class);

            String redirectUrl;
            String subject;
            String bodyTitle;
            String bodyMessage;

            // Usuario comprador
            redirectUrl = this.clientUrl.concat("/user-my-purchases");
            subject = "Pago por transferencia confirmado";
            bodyTitle = "??".concat(operacion.getCliente().getNombre() + " tu pago ha sido confirmado!");
            bodyMessage = "Tu pago por transferencia bancaria fue confirmado por un administrador de la tienda. Ahora solo " +
                    "queda esperar que envien tu compra.\n" +
                    "Podes revisar tus compras accediendo desde el siguiente enlace:";
            this.enviarEmailUsuario(redirectUrl, subject, bodyTitle, bodyMessage, operacion, useremail);

            // Env??o notificaciones
            String notiTitle = "Pago confirmado";
            String actionUrl = this.clientUrl.concat("/detalle/" + operacion.getNroOperacion());
            this.enviarNotificacionUsuario(NotificationSenderEnum.confirmarComprobanteSender, notiTitle, operacion,
                    actionUrl, useremail);
        };
    }

    /**
     * Acci??n que se produce en la transici??n: PAGO_PENDIENTE, PAGO_RECHAZADO --> PAGO_RECHAZADO
     * (RECHAZAR_PAGO). Se produce cuando el pago ha sido rechazado, indpendientemente del m??todo de pago.
     * Al rechazar el pago se espera:
     *  - env??o email al usuario comprador, informando que su pago ha sido rechazado.
     * @return Action utilizada por la state machine.
     */
    public Action<EstadoOperacion, EventoOperacion> rechazarPago() {
        return stateContext -> {

            StateMachine<EstadoOperacion, EventoOperacion> sm = stateContext.getStateMachine();

            Operacion operacion = sm.getExtendedState().get("operacion", Operacion.class);
            String useremail = sm.getExtendedState().get("useremail", String.class);

            String redirectUrl;
            String subject;
            String bodyTitle;
            String bodyMessage;

            // Usuario comprador
            redirectUrl = this.clientUrl.concat("/user-my-purchases");
            subject = "Pago rechazado";
            bodyTitle = "??".concat(operacion.getCliente().getNombre()) + ", el pago de tu compra fue rechazado!";
            bodyMessage = "El pago para tu compra fue rechazado por el medio de pago elegido. Para continuar " +
                    "con el pago, selecciona la compra y eleg?? la opci??n de completar pago.\n" +
                    "Busc?? tu compra en el listado del siguiente enlace:";
            this.enviarEmailUsuario(redirectUrl, subject, bodyTitle, bodyMessage, operacion, useremail);
        };
    }

    /**
     * Acci??n que se produce en la transici??n de: PAGO_PENDIENTE --> EN_RESERVA.
     * Se produce cuando el pago ha sido en efectivo/transf bancaria de manera autom??tica
     * al registrar una nueva operaci??n. Es un estado especial, se debe descontar stock,
     * pero pasado X cantidad de tiempo, debe reintegrarse en caso de que no se pase al estado ENTREGADO.
     * @return Action utilizada por la state machine.
     */
    private Action<EstadoOperacion, EventoOperacion> registrarReserva() {
        return stateContext -> {
            StateMachine<EstadoOperacion, EventoOperacion> sm = stateContext.getStateMachine();

            Operacion operacion = sm.getExtendedState().get("operacion", Operacion.class);
            String useremail = sm.getExtendedState().get("useremail", String.class);

            this.descontarDisponibilidad(operacion.getItems());

            // Env??o de emails
            String redirectUrl;
            String subject;
            String bodyTitle;
            String bodyMessage;

            // Usuario comprador
            int maxDiasCash = ExpiracionPagoConstants.EXPIRACION_CASH_DATE;
            int maxDiasTB = ExpiracionPagoConstants.EXPIRACION_TB_DATE;
            redirectUrl = this.clientUrl.concat("/user-my-purchases");
            subject = "Nueva compra registrada";
            bodyTitle = "??".concat(operacion.getCliente().getNombre()) + ", tu compra ha sido registrada correctamente!";
            bodyMessage = "Record?? que tu medio de pago es EFECTIVO/TRANSFERENCIA BANCARIA:\n" +
                    "Efectivo: Para completar tu compra, ten??s que ir a la tienda a retirar tus productos, y realizar el pago correspondiente, ten??s " +
                    "hasta " + maxDiasCash + " d??as para retirar tus productos.\n" +
                    "Transferencia: Cuando completes el pago, sub?? a trav??s de nuestra web una imagen con el comprobante del pago.\n" +
                    "Un administrador de la tienda tiene que validar tu transferencia. Ten??s hasta " + maxDiasTB + " d??as para completar la transferencia.\nAnte cualquier duda, consultanos a trav??s de los distintos medios de contacto.\n" +
                    "Haciendo click en el siguiente enlace, pod??s revisar los datos de la compra en tu perfil de usuario:";
            this.enviarEmailUsuario(redirectUrl, subject, bodyTitle, bodyMessage, operacion, useremail);

            // Administradores de la p??gina
            redirectUrl = this.clientUrl.concat("/detalle/venta/" + operacion.getNroOperacion());
            subject = "Nueva venta registrada en tu Tienda Online";
            bodyTitle = "Nueva venta en efectivo registrada";
            bodyMessage = "Sr. Administrador, se ha regristrado una nueva venta en su tienda. El medio de pago de esta venta " +
                    "es EFECTIVO/TRANSFERENCIA_BANCARIA, por lo que pasados los d??as establecidos, o anulando la operaci??n en el panel de administraci??n, la operaci??n " +
                    "quedar?? sin efecto, reintegrando la disponibilidad de aquellos items afectados.\n" +
                    "Por favor, revise los datos de la venta accediendo al panel de administrador haciendo click en el siguiente enlace:";
            this.enviarEmailsAdmins(redirectUrl, subject, bodyTitle, bodyMessage, operacion);

            // Notificaciones
            String notiTitle;
            String actionUrl;
            // Usuario
            notiTitle = "Nueva compra registrada";
            actionUrl = this.clientUrl.concat("/detalle/" + operacion.getNroOperacion());
            this.enviarNotificacionUsuario(NotificationSenderEnum.nuevaCompraCashSender, notiTitle,
                    operacion, actionUrl, useremail);

            // Admins
            notiTitle = "Nueva venta efectivo/transferencia bancaria registrada";
            actionUrl = this.clientUrl.concat("/detalle/venta/" + operacion.getNroOperacion());
            this.enviarNotificacionesAdmins(NotificationSenderEnum.nuevaVentaCashSender, notiTitle,
                    operacion, actionUrl);
        };
    }

    /**
     * Acci??n que se produce en la transici??n de: EN_RESERVA --> ANULADO. Se produce cuando el administrador, o el paso
     * del tiempo, indica que una operaci??n registrada como medio de pago en efectivo, nunca se termino de entregar.
     * @return Action utilizada por la state machine.
     */
    public Action<EstadoOperacion, EventoOperacion> anular() {
        return stateContext -> {
            StateMachine<EstadoOperacion, EventoOperacion> sm = stateContext.getStateMachine();

            Operacion operacion = sm.getExtendedState().get("operacion", Operacion.class);
            String useremail = sm.getExtendedState().get("useremail", String.class);

            this.reintegrarDisponibilidad(operacion.getItems());

            String redirectUrl = this.clientUrl.concat("/user-my-purchases");
            String subject = "Compra anulada";
            String bodyTitle = "Tu compra fue anulada";
            String bodyMessage = "Tu compra ha sido anulada por decisi??n propia, por el administrador, o porque no retiraste a tiempo tus " +
                    "productos en la tienda. Por cualquier consulta, no dudes en contactarnos.\n " +
                    "Para ver en detalle tu compra anulada, hac?? click en el siguiente enlace:";
            this.enviarEmailUsuario(redirectUrl, subject, bodyTitle, bodyMessage, operacion, useremail);

            // Env??o notificaciones
            String notiTitle = "Compra anulada";
            String actionUrl = this.clientUrl.concat("/detalle/" + operacion.getNroOperacion());
            this.enviarNotificacionUsuario(NotificationSenderEnum.anularOperacionSender, notiTitle, operacion,
                    actionUrl, useremail);
        };
    }

    /**
     * Acci??n que se produce al cancelar una oepraci??n. El comportamiento esperado es:
     *  - env??o email al usuario comprador informando sobre la cancelaci??n y emails a administradores.
     *  <br>
     *  NOTA: Ac?? no debe reintegrarse STOCK (disponibilidad), ya que s??lo se puede cancelar una operaci??n ANTES de
     *  confirmar el pago.
     * @return Action utilizada por la state machine.
     */
    public Action<EstadoOperacion, EventoOperacion> cancelar() {
        return stateContext -> {
            // TODO --> Dise??ar e implementar l??gica de cancelaci??n de operaciones. Tener en cuenta el pago.
            StateMachine<EstadoOperacion, EventoOperacion> sm = stateContext.getStateMachine();
            Operacion operacion = sm.getExtendedState().get("operacion", Operacion.class);
            String useremail = sm.getExtendedState().get("useremail", String.class);

            // Env??o de emails
            String redirectUrl = this.clientUrl.concat("/user-my-purchases");
            String subject = "Cancelaci??n de compra";
            String bodyTitle = operacion.getCliente().getNombre() + ", tu compra ha sido cancelada";
            String bodyMessage = "Tu compra ha sido cancelada, ??pero los productos a??n pueden ser tuyos, no te quedes sin ellos!\n" +
                    " Te dejamos un resumen de tu compra cancelada, por si quer??s retomarla luego.\n" +
                    " Para ver el detalle de la compra cancelada, hac?? click en el siguiente enlace:";

            this.enviarEmailUsuario(redirectUrl, subject, bodyTitle, bodyMessage, operacion, useremail);

            // Env??o de notificaciones
            String notiTitle = "Compra cancelada";
            String actionUrl = this.clientUrl.concat("/detalle/" + operacion.getNroOperacion());
            this.enviarNotificacionUsuario(NotificationSenderEnum.cancelarOperacionSender, notiTitle, operacion,
                    actionUrl, useremail);
        };
    }

    /**
     * Acci??n que se produce al registrar el env??o de una operaci??n. Se espera:
     *  - env??o de email al usuario comprador indicando que los productos estan en camino.
     * @return Action utilizada por la state machine.
     */
    public Action<EstadoOperacion, EventoOperacion> enviar() {
        return stateContext -> {
            StateMachine<EstadoOperacion, EventoOperacion> sm = stateContext.getStateMachine();
            Operacion operacion = sm.getExtendedState().get("operacion", Operacion.class);
            String useremail = sm.getExtendedState().get("useremail", String.class);

            operacion.setFechaEnvio(new Date());

            // Env??o de emails
            String redirectUrl = this.clientUrl.concat("/user-my-purchases");
            String subject = "Env??o de compra";
            String bodyTitle = "??".concat(operacion.getCliente().getNombre() + ", tu compra est?? en camino!");
            String bodyMessage = "??Tus productos ya han sido enviados y est??n en camino!\n" +
                    " Para ver el detalle de tu compra, hac?? click en el siguiente enlace:";

            this.enviarEmailUsuario(redirectUrl, subject, bodyTitle, bodyMessage, operacion, useremail);

            // Env??o de notificaciones
            String title = "En camino...";
            String actionUrl = this.clientUrl.concat("/detalle/" + operacion.getNroOperacion());
            this.enviarNotificacionUsuario(NotificationSenderEnum.operacionEnviadaSender, title, operacion,
                    actionUrl, useremail);
        };
    }

    /**
     * Acci??n que se produce al registrar que los items de una operaci??n han llegado a destino. Se espera:
     *  - email al usuario comprador, informando que se registr?? en el sistema el arribo de sus productos y agradeciendo
     * @return Action utilizada por la state machine.
     */
    public Action<EstadoOperacion, EventoOperacion> entregar() {
        return stateContext -> {
            StateMachine<EstadoOperacion, EventoOperacion> sm = stateContext.getStateMachine();
            Operacion operacion = sm.getExtendedState().get("operacion", Operacion.class);
            String useremail = sm.getExtendedState().get("useremail", String.class);

            operacion.setFechaEntrega(new Date());

            // Env??o de emails
            String redirectUrl = this.clientUrl.concat("/user-my-purchases");
            String subject = "Recibiste tu compra";
            String bodyTitle = "??".concat(operacion.getCliente().getNombre() + ", recibiste tu compra!");
            String bodyMessage = "Se registr?? en nuetro sistema que tu compra lleg?? a destino.\n Porfavor, no dejes de comunicarte" +
                    " con nosotros si hubo alg??n problema con tu compra.\n " +
                    " Para acceder al detalle de tu compra, hac?? click en el siguiente enlace:";

            this.enviarEmailUsuario(redirectUrl, subject, bodyTitle, bodyMessage, operacion, useremail);

            // Env??o de notificaciones
            String title = "Compra recibida";
            String actionUrl = this.clientUrl.concat("/detalle/" + operacion.getNroOperacion());
            this.enviarNotificacionUsuario(NotificationSenderEnum.operacionEntregadaSender, title,
                    operacion, actionUrl, useremail);
        };
    }

    /**
     * Acci??n que se produce al transitar al estado EN_DEVOLUCION. Se espera:
     *  - env??o email al usuario comprador confirmando la transacci??n de devoluci??n y los pasos a seguir.
     *  - env??o emails de administradores para comunicarse con el usuario comprador que inici?? la devoluci??n.
     * @return Action utilizada por la state machine.
     */
    public Action<EstadoOperacion, EventoOperacion> devolver() {
        return stateContext -> {
            StateMachine<EstadoOperacion, EventoOperacion> sm = stateContext.getStateMachine();

            Operacion operacion = sm.getExtendedState().get("operacion", Operacion.class);
            String useremail = sm.getExtendedState().get("useremail", String.class);

            String redirectUrl;
            String subject;
            String bodyTitle;
            String bodyMessage;

            // env??o usuario comprador
            redirectUrl = this.clientUrl.concat("/user-my-purchases");
            subject = "Devoluci??n de compra";
            bodyTitle = "Se inici?? el proceso de devoluci??n";
            bodyMessage = "El proceso de devoluci??n de tu compra ha sido iniciado. Vas a ser contactado por un " +
                    "administrador de la tienda para saber como continuar. En caso de que ning??n administrador se contacte, " +
                    "porfavor, comunicate con el personal de nuestra tienda a trav??s de cualquier medio que aparece en el apartado " +
                    "para contactarnos.\n" +
                    "Para ver en detalle tu compra a devolver, hac?? click en el siguiente enlace:";
            this.enviarEmailUsuario(redirectUrl, subject, bodyTitle, bodyMessage, operacion, useremail);

            // env??o administradores de la tienda
            redirectUrl = this.clientUrl.concat("/detalle/venta/" + operacion.getNroOperacion());
            subject = "Devoluci??n de operaci??n";
            bodyTitle = "Un cliente inici?? el proceso para devolver productos";
            bodyMessage= "Sr. Administrador, se ha regristrado que un cliente desea devolver los productos de una operaci??n.\n" +
                    "Porfavor, contactese de inmediato con el cliente asociado a dicha operaci??n para continuar con el proceso " +
                    "de devoluci??n.\n" +
                    "Revise los datos de la operaci??n a devolver accediendo al panel de administrador haciendo click en el siguiente enlace:";
            this.enviarEmailsAdmins(redirectUrl, subject, bodyTitle, bodyMessage, operacion);

            // Env??o notificaciones
            String notiTitle;
            String actionUrl;
            // Usuario comprador
            notiTitle = "Devoluci??n iniciada";
            actionUrl = this.clientUrl.concat("/detalle/" + operacion.getNroOperacion());
            this.enviarNotificacionUsuario(NotificationSenderEnum.devolverOperacionSender, notiTitle, operacion,
                    actionUrl, useremail);
            // Admin
            notiTitle = "Proceso de devoluci??n iniciado";
            actionUrl = this.clientUrl.concat("/detalle/venta/" + operacion.getNroOperacion());
            this.enviarNotificacionesAdmins(NotificationSenderEnum.nuevaDevolucionSender, notiTitle, operacion,
                    actionUrl);
        };
    }

    /**
     * Acci??n que se produce al transitar de EN_DEVOLUCION --> DEVUELTO, al registrar que los items de una operaci??n
     * devuelta han sido reintegrados. Se espera:
     * - reintegrar stock (disponibilidad) de aquellos skus afectados en la operaci??n.
     * @return Action utilizada por la state machine.
     */
    public Action<EstadoOperacion, EventoOperacion> registrarDevolucion() {
        return stateContext -> {
            StateMachine<EstadoOperacion, EventoOperacion> sm = stateContext.getStateMachine();

            Operacion operacion = sm.getExtendedState().get("operacion", Operacion.class);
            String useremail = sm.getExtendedState().get("useremail", String.class);

            this.reintegrarDisponibilidad(operacion.getItems());

            String redirectUrl = this.clientUrl.concat("/user-my-purchases");
            String subject = "Devoluci??n completada";
            String bodyTitle = "Devolviste tus productos";
            String bodyMessage = "Se registr?? que los productos de tu compra han sido devueltos correctamente.\n" +
                    "Para ver en detalle tu compra devuelta, hac?? click en el siguiente enlace:";
            this.enviarEmailUsuario(redirectUrl, subject, bodyTitle, bodyMessage, operacion, useremail);

            // Env??o notificaciones
            String notiTitle = "Devoluci??n completada";
            String actionUrl = this.clientUrl.concat("/detalle/" + operacion.getNroOperacion());
            this.enviarNotificacionUsuario(NotificationSenderEnum.confirmarDevolucionSender, notiTitle, operacion,
                    actionUrl, useremail);
        };
    }

    /**
     * M??todo auxiliar para descontar la disponibilidad de los skus por cada item de la operaci??n, y guardarlos al
     * actualizarse dicha disponibilidad.
     * @param items {@link List<DetalleOperacion>} items afectados..
     */
    private void descontarDisponibilidad(List<DetalleOperacion> items) {
        for (DetalleOperacion item: items) {
            Sku skuActual = this.skuService.obtenerSku(item.getSku().getId());
            skuActual.setDisponibilidad(skuActual.getDisponibilidad() - item.getCantidad());

            // Guardamos el sku con disponibilidad actualizada.
            this.skuService.save(skuActual);
        }
    }

    /**
     * M??todo auxiliar para reintegrar la disponibilidad de los items cuando sea necesario. Por cada item, suma la cantidad
     * a la disponibilidad de su sku, y al terminar, los guarda y actualiza.
     * @param items {@link List<DetalleOperacion>} items afectados.
     */
    private void reintegrarDisponibilidad(List<DetalleOperacion> items) {
        for (DetalleOperacion item: items) {
            Sku skuActual = this.skuService.obtenerSku(item.getSku().getId());
            skuActual.setDisponibilidad(skuActual.getDisponibilidad() + item.getCantidad());

            this.skuService.save(skuActual);
        }
    }

    /**
     * Este m??todo se encarga del env??o de email al registrar que una operaci??n ha sido completada, al completar el
     * pago de la misma. El env??o de email es para el usuario comprador.
     * @param operacion {@link Operacion} con los datos sobre la compra.
     * @param email String con el email del usuario comprador, para efectuar el env??o del email.
     */
    private void enviarEmailUsuario(String redirectUrl, String subject, String bodyTitle, String bodyMessage,
                                    Operacion operacion, String email) {
        EmailBody body = EmailBody.builder()
                .title(bodyTitle)
                .message(bodyMessage)
                .content(operacion).build();

        CompNotificacionEmail notificacionEmail = CompNotificacionEmail.builder()
                .redirectUrl(redirectUrl)
                .subject(subject)
                .recipient(email)
                .body(body).build();

        this.mailService.sendEmail(notificacionEmail);
    }

    /**
     * Este m??todo se encarga de enviar los emails correspondientes a los administradores del sistema, recorriendo todos
     * los usuarios actuales, y obteniendo aquellos que son administradores para completar el env??o de emails.
     * <br>
     * Este env??o de email se activa cuando el pago de una operaci??n es completado.
     * @param operacion {@link Operacion} con los datos de la venta para enviar a los administradores.
     */
    private void enviarEmailsAdmins(String redirectUrl, String subject, String bodyTitle, String bodyMessage, Operacion operacion) {
        List<UsuarioDTO> admins = this.administradorUsuariosService.obtenerAdministradores();

        for (UsuarioDTO admin: admins) {
            EmailBody body = EmailBody.builder()
                    .title(bodyTitle)
                    .message(bodyMessage)
                    .content(operacion).build();

            log.info(admin.getEmail());

            CompNotificacionEmail notificacionEmail = CompNotificacionEmail.builder()
                    .redirectUrl(redirectUrl)
                    .subject(subject)
                    .recipient(admin.getEmail())
                    .body(body).build();

            this.mailService.sendEmail(notificacionEmail);
        }
    }

    private void enviarNotificacionUsuario(NotificationSenderEnum notificationSenderType, String title, Operacion operacion, String actionUrl, String user) {
        NotificationSender notificationSender = notificationSenderFactory.get(String.valueOf(notificationSenderType));
        notificationSender.sendNotification(title, operacion, actionUrl, user);
    }

    private void enviarNotificacionesAdmins(NotificationSenderEnum notificationSenderType, String title, Operacion operacion, String actionUrl) {
        NotificationSender notificationSender = notificationSenderFactory.get(String.valueOf(notificationSenderType));

        List<UsuarioDTO> admins = this.administradorUsuariosService.obtenerAdministradores();
        for (UsuarioDTO admin: admins) {
            String currentEmail = admin.getEmail();
            notificationSender.sendNotification(title, operacion, actionUrl, currentEmail);
        }
    }
}
