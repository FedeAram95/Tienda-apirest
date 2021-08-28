package com.deofis.tiendaapirest.catalogo.skus.services;

import com.deofis.tiendaapirest.catalogo.productos.entities.Producto;
import com.deofis.tiendaapirest.catalogo.productos.entities.PropiedadProducto;
import com.deofis.tiendaapirest.catalogo.productos.entities.ValorPropiedadProducto;
import com.deofis.tiendaapirest.catalogo.skus.entities.Sku;

import java.util.List;

/**
 * Este servicio se encarga de generar {@link Sku}
 * a partir de las distintas combinaciones posibles de propiedades de un
 * {@link Producto} requerido .
 */

public interface GeneradorSkus {

    /**
     * Genera los distintos Skus a partir de un producto y sus propiedades que son requeridas
     * @param producto Producto a generar SKUs.
     * @return Integer: -1 (El producto no tiene propiedades); -2 (No se generó ninguna combinación).
     */
    Integer generarSkusProducto(Producto producto);

    /**
     * Genera TODAS las combinaciones posibles para un Producto, sin tener en cuenta si esta repetida
     * cierta combinación, o no.
     * @param indexActual indice de la propiedad actual en el listado de propiedades recorridas.
     * @param combinacionActual List de valores de producto (combinacion) actual.
     * @param propiedades List de las propiedades del producto a generar combinaciones.
     * @return List de cada una de las combinaciones. (List de List).
     */
    List<List<ValorPropiedadProducto>> generarCombinaciones(Integer indexActual,
                                                            List<ValorPropiedadProducto> combinacionActual,
                                                            List<PropiedadProducto> propiedades);

    /**
     * Compara dos combinaciones (List de valores) y verifica si son la misma o no.
     * @param combinacion1 List valores de propiedad 1.
     * @param combinacion2 List valores de propiedad 2.
     * @return boolean true (misma combinacion); false (distinta combinacion).
     */
    boolean esMismaCombinacion(List<ValorPropiedadProducto> combinacion1,
                               List<ValorPropiedadProducto> combinacion2);

    /**
     * Persiste las distintas combinaciones de productos, y asigna los Skus generados al producto
     * requerido. Todos los skus generados a partir de las combinaciones se crean, por defecto, en
     * activo.
     * @param producto Producto al cual se deben asignar los Skus generados.
     * @param combinaciones Todas las combinaciones generadas a persistir (List de List)
     * @return Integer con la cantidad de combinaciones generadas.
     */
    Integer persistirCombinaciones(Producto producto, List<List<ValorPropiedadProducto>> combinaciones);
}
