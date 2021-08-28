package com.deofis.tiendaapirest.ventas.services;

import com.deofis.tiendaapirest.imagenes.entities.Imagen;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import com.deofis.tiendaapirest.operaciones.exceptions.OperacionException;
import com.deofis.tiendaapirest.operaciones.repositories.OperacionRepository;
import com.deofis.tiendaapirest.pagos.entities.MedioPagoEnum;
import com.deofis.tiendaapirest.pagos.entities.OperacionPago;
import com.deofis.tiendaapirest.ventas.dto.VentaPayload;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VentaServiceImpl implements VentaService {

    private final OperacionRepository operacionRepository;

    @Transactional(readOnly = true)
    @Override
    public List<VentaPayload> listarVentas() {
        return this.operacionRepository.findAllByOrderByFechaOperacionDesc()
                .stream()
                .map(this::mapToVenta)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<VentaPayload> ventasFecha(Date fechaDesde, Date fechaHasta) {
        List<VentaPayload> operaciones = this.operacionRepository.findAll()
                .stream()
                .map(this::mapToVenta)
                .collect(Collectors.toList());

        List<VentaPayload> ventasFecha = new ArrayList<>();

        for (VentaPayload venta: operaciones) {
            if (venta.getFechaOperacion().after(fechaDesde) && venta.getFechaOperacion().before(fechaHasta)) {
                ventasFecha.add(venta);
            }
        }
        return ventasFecha;
    }

    @Transactional(readOnly = true)
    @Override
    public List<VentaPayload> ventasEstado(String estado) {
        List<VentaPayload> ventas = this.operacionRepository.findAll()
                .stream()
                .map(this::mapToVenta)
                .collect(Collectors.toList());

        List<VentaPayload> ventasEstado = new ArrayList<>();

        for (VentaPayload venta: ventas) {
            if (estado.equalsIgnoreCase(String.valueOf(venta.getEstado())))
                ventasEstado.add(venta);
        }

        return ventasEstado;
    }

    @Transactional(readOnly = true)
    @Override
    public List<VentaPayload> ventasFechaYEstado(String estado, Date fechaDesde, Date fechaHasta) {
        List<VentaPayload> ventasFecha = this.operacionRepository.findAllByFechaOperacionBetween(fechaDesde, fechaHasta)
                .stream()
                .map(this::mapToVenta)
                .collect(Collectors.toList());

        List<VentaPayload> ventasTotales = new ArrayList<>();

        for (VentaPayload venta: ventasFecha) {
            if (estado.equalsIgnoreCase(String.valueOf(venta.getEstado()))) {
                ventasTotales.add(venta);
            }
        }

        return ventasTotales;
    }

    @Transactional(readOnly = true)
    @Override
    public VentaPayload obtenerVenta(Long nroOperacion) {
        return this.mapToVenta(this.operacionRepository.findById(nroOperacion)
                .orElseThrow(() -> new OperacionException("No existe la operación con id: " + nroOperacion)));
    }

    @Transactional(readOnly = true)
    @Override
    public Imagen obtenerComprobantePagoVenta(Long nroOperacion) {
        Operacion operacion = this.operacionRepository.findById(nroOperacion)
                .orElseThrow(() -> new OperacionException("No existe la operación con id: " + nroOperacion));

        OperacionPago pago = operacion.getPago();

        if (!operacion.getMedioPago().getNombre().equals(MedioPagoEnum.TRANSFERENCIA_BANCARIA))
            throw new OperacionException("Solo el medio de pago por transferencia bancaria tiene comprobante " +
                    "de pago");

        return pago.getComprobante();
    }

    private VentaPayload mapToVenta(Operacion operacion) {
        return VentaPayload.builder()
                .nroOperacion(operacion.getNroOperacion())
                .fechaOperacion(operacion.getFechaOperacion())
                .fechaEnvio(operacion.getFechaEnvio())
                .fechaEntrega(operacion.getFechaEntrega())
                .estado(operacion.getEstado())
                .cliente(operacion.getCliente())
                .direccionEnvio(operacion.getDireccionEnvio())
                .medioPago(operacion.getMedioPago())
                .items(operacion.getItems())
                .total(operacion.getTotal()).build();
    }
}
