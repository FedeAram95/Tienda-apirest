package com.deofis.tiendaapirest.ventas.services;

import com.deofis.tiendaapirest.ventas.dto.VentaPayload;

import java.util.List;

public interface VentasClienteFinder {
    /**
     * Obtiene listado de ventas del cliente requerido, a través de su id.
     * @param clienteId Long id del cliente.
     * @return {@link List<VentaPayload>} listado ventas.
     */
    List<VentaPayload> ventasCliente(Long clienteId);
}
