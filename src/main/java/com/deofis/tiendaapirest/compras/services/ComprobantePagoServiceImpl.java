package com.deofis.tiendaapirest.compras.services;

import com.deofis.tiendaapirest.clientes.entities.Cliente;
import com.deofis.tiendaapirest.compras.dto.CompraPayload;
import com.deofis.tiendaapirest.imagenes.entities.Imagen;
import com.deofis.tiendaapirest.operaciones.entities.EstadoOperacion;
import com.deofis.tiendaapirest.operaciones.entities.EventoOperacion;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import com.deofis.tiendaapirest.operaciones.exceptions.OperacionException;
import com.deofis.tiendaapirest.operaciones.repositories.OperacionRepository;
import com.deofis.tiendaapirest.operaciones.statemachine.StateMachineService;
import com.deofis.tiendaapirest.pagos.entities.MedioPagoEnum;
import com.deofis.tiendaapirest.pagos.entities.OperacionPago;
import com.deofis.tiendaapirest.pagos.services.bancaria.ComprobantePagoRegistrador;
import com.deofis.tiendaapirest.perfiles.services.PerfilService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ComprobantePagoServiceImpl implements ComprobantePagoService {

    private final EncontradorCompraCliente encontradorCompraCliente;
    private final PerfilService perfilService;
    private final StateMachineService stateMachineService;

    private final ComprobantePagoRegistrador comprobantePagoRegistrador;
    private final OperacionRepository operacionRepository;

    @Transactional
    @Override
    public CompraPayload subirComprobantePago(MultipartFile comprobante, Long nroOperacion) {
        Cliente clienteActual = this.getClientePerfil();
        Operacion compra = this.encontradorCompraCliente.encontrarCompraCliente(nroOperacion, clienteActual);

        // Validaciones obvias
        if (!compra.getMedioPago().getNombre().equals(MedioPagoEnum.TRANSFERENCIA_BANCARIA))
            throw new OperacionException("No se puede subir comprobante de pago si el medio de pago no fue " +
                    "transferencia bancaria");

        List<EstadoOperacion> estadosAceptados = new ArrayList<>();
        estadosAceptados.add(EstadoOperacion.EN_RESERVA);
        estadosAceptados.add(EstadoOperacion.COMPROBADO);
        if (!estadosAceptados.contains(compra.getEstado()))
            throw new OperacionException("No se puede subir comprobante de pago para operaciones que no se " +
                    "encuentren en el estado en reserva/comprobado");

        OperacionPago pagoActual = compra.getPago();
        OperacionPago pagoComprobado = this.comprobantePagoRegistrador.registrarComprobantePago(comprobante, pagoActual);

        compra.setPago(pagoComprobado);

        StateMachine<EstadoOperacion, EventoOperacion> sm = this.stateMachineService.build(nroOperacion);
        sm.getExtendedState().getVariables().put("operacion", compra);
        sm.getExtendedState().getVariables().put("useremail", clienteActual.getEmail());
        this.stateMachineService.enviarEvento(nroOperacion, sm, EventoOperacion.COMPROBAR_PAGO);

        return this.mapToCompra(this.operacionRepository.save(compra));
    }

    @Transactional(readOnly = true)
    @Override
    public Imagen obtenerImagenComprobantePago(Long nroOperacion) {
        Cliente clienteActual = this.getClientePerfil();
        Operacion compra = this.encontradorCompraCliente.encontrarCompraCliente(nroOperacion, clienteActual);

        if (!compra.getMedioPago().getNombre().equals(MedioPagoEnum.TRANSFERENCIA_BANCARIA))
            throw new OperacionException("Solo el medio de pago por transferencia bancaria tiene comprobante " +
                    "de pago");

        OperacionPago pagoCompra = compra.getPago();
        return pagoCompra.getComprobante();
    }

    /**
     * Obtiene el cliente del perfil actual con sesión en el sistema.
     * @return {@link Cliente}.
     */
    private Cliente getClientePerfil() {
        return this.perfilService.obtenerDatosCliente();
    }

    private CompraPayload mapToCompra(Operacion operacion) {
        return CompraPayload.builder()
                .nroOperacion(operacion.getNroOperacion())
                .fechaOperacion(operacion.getFechaOperacion())
                .fechaEnvio(operacion.getFechaEnvio())
                .fechaEntrega(operacion.getFechaEntrega())
                .estado(operacion.getEstado())
                .cliente(operacion.getCliente().getApellido() + ", " + operacion.getCliente().getNombre())
                .direccionEnvio(operacion.getDireccionEnvio())
                .medioPago(operacion.getMedioPago())
                .items(operacion.getItems())
                .total(operacion.getTotal()).build();
    }
}
