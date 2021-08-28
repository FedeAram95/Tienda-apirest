package com.deofis.tiendaapirest.catalogo.skus.services.actualizador;


import com.deofis.tiendaapirest.catalogo.skus.entities.Sku;

import java.util.List;

/**
 * Este servicio se encarga de la actualización de precios de skus.
 */
public interface ActualizadorPreciosService {

    /**
     * Actualiza los precios de todos los skus que pertenecen a un producto requerido, a través de su id
     * @param productoId Long id del producto.
     * @param porcentaje Double con el porcentaje para la actualización de precios.
     * @return Listado de todos los skus actualizados.
     */
    List<Sku> actualizarPreciosSkus(Long productoId, Double porcentaje);


    /**
     * Actualiza los precios de todos los skus que pertenecen a los productos de una marca requerida, a través del id
     * de la marca.
     * @param marcaId Long id de la marca.
     * @param porcentaje Double con el porcentaje para la actualización de precios.
     * @return Lilstado de todos los skus actualizados.
     */
    List<Sku> actualizarPreciosSkusMarca(Long marcaId, Double porcentaje);

    /**
     * Actualiza los precios de todos los skus que pertenecen a los productos de una subcategoría requerida, a través
     * del id de la subcategoría.
     * @param subcategoriaId Long id de la subcategoría.
     * @param porcentaje Double con el porcentaje para la actualización de precios.
     * @return Listado de todos los skus actualizados.
     */
    List<Sku> actualizarPreciosSkusSubcategoria(Long subcategoriaId, Double porcentaje);
}
