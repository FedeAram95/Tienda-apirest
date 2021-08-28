package com.deofis.tiendaapirest.catalogo.skus.services.actualizador;

import com.deofis.tiendaapirest.catalogo.skus.entities.Sku;

/**
 * Servicio para el manejo de la disponibilidad de los skus.
 */
public interface AgregadorDisponibilidadService {

    /**
     * Servicio que agrega (suma) disponibilidad requerida, para un sku requerido.
     * @param skuId Long id del sku.
     * @param disponibilidadAgregada Integer con disponibilidad a agregar.
     * @return {@link Sku} con nueva disponibilidad.
     */
    Sku agregarDisponibilidad(Long skuId, Integer disponibilidadAgregada);
}
