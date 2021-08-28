package com.deofis.tiendaapirest.compras.services;

import com.deofis.tiendaapirest.clientes.entities.Cliente;
import com.deofis.tiendaapirest.operaciones.entities.DetalleOperacion;
import com.deofis.tiendaapirest.operaciones.entities.EstadoOperacion;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import com.deofis.tiendaapirest.operaciones.exceptions.OperacionException;
import com.deofis.tiendaapirest.operaciones.repositories.OperacionRepository;
import com.deofis.tiendaapirest.pagos.entities.MedioPagoEnum;
import com.deofis.tiendaapirest.pagos.entities.OperacionPago;
import com.deofis.tiendaapirest.pagos.services.PagoHandler;
import com.deofis.tiendaapirest.perfiles.services.PerfilService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CompletadorPago implements CompletarPagoService {

    private final PerfilService perfilService;
    private final EncontradorCompraCliente encontradorCompraCliente;
    private final PagoHandler pagoHandler;
    private final OperacionRepository operacionRepository;

    @Transactional
    @Override
    public OperacionPago completarPago(Long nroOperacion) {
        Cliente clienteActual = this.getClienteDelPerfilActual();
        Operacion operacion = this.encontradorCompraCliente.encontrarCompraCliente(nroOperacion, clienteActual);

        // Si el estado NO ES pendiente de pago o creado, o si el medio de pago ES efectivo: no se puede completar
        // un pago (si es efectivo, ya esta completado; si no esta pendiente de pago/creado, no hay nada que pagar).
        List<EstadoOperacion> estadosAceptados = new ArrayList<>();
        estadosAceptados.add(EstadoOperacion.PAGO_RECHAZADO);
        estadosAceptados.add(EstadoOperacion.PAGO_PENDIENTE);
        if (!estadosAceptados.contains(operacion.getEstado()))
            throw new OperacionException("No puede completar el pago de una operación que no esté en" +
                    " en pendiente de pago/pago rechazado");
        List<MedioPagoEnum> mediosNoAceptados = new ArrayList<>();
        mediosNoAceptados.add(MedioPagoEnum.EFECTIVO);
        mediosNoAceptados.add(MedioPagoEnum.TRANSFERENCIA_BANCARIA);
        if (mediosNoAceptados.contains(operacion.getMedioPago().getNombre()))
            throw new OperacionException("No se puede completar el pago de una operación con medio " +
                    "de pago efectivo/transferencia bancaria");

        log.info(String.valueOf(operacion.getPago()));

        // Necesitamos validar la disponibilidad, para ver si al momento de completar un pago, algún item se quedó
        // sin stock.
        boolean hayDisponibilidad = true;
        for (DetalleOperacion item: operacion.getItems()) {
            if (item.getSku().getDisponibilidad() - item.getCantidad() < 0) {
                hayDisponibilidad = false;
                break;
            }
        }

        if (!hayDisponibilidad)
            throw new OperacionException("Error al completar la compra: " +
                    "La cantidad de productos vendidos no puede ser menor a la disponibilidad actual");

        OperacionPago pagoManejado = this.pagoHandler.procesarPago(operacion);
        operacion.setPago(pagoManejado);
        this.operacionRepository.save(operacion);
        return pagoManejado;
    }

    /**
     * Obtiene el {@link Cliente} para el perfil del usuario que esta actualmente
     * en la sesión.
     * @return {@link Cliente} del usuario logueado.
     */
    private Cliente getClienteDelPerfilActual() {
        return this.perfilService.obtenerDatosCliente();
    }
}
