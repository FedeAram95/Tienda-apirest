package com.deofis.tiendaapirest.ventas.services;

import com.deofis.tiendaapirest.clientes.entities.Cliente;
import com.deofis.tiendaapirest.clientes.repositories.ClienteRepository;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import com.deofis.tiendaapirest.operaciones.repositories.OperacionRepository;
import com.deofis.tiendaapirest.perfiles.exceptions.PerfilesException;
import com.deofis.tiendaapirest.ventas.dto.VentaPayload;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VentasClienteFinderImpl implements VentasClienteFinder {

    private final ClienteRepository clienteRepository;
    private final OperacionRepository operacionRepository;

    @Transactional(readOnly = true)
    @Override
    public List<VentaPayload> ventasCliente(Long clienteId) {
        Cliente cliente = this.clienteRepository.findById(clienteId)
                .orElseThrow(() -> new PerfilesException("No existe cliente con id: " + clienteId));

        List<Operacion> ventasCliente = this.operacionRepository.findAllByClienteOrderByFechaOperacionAsc(cliente);
        return ventasCliente
                .stream()
                .map(this::mapToVenta)
                .collect(Collectors.toList());
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
