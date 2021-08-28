package com.deofis.tiendaapirest.catalogo.skus.services;

import com.deofis.tiendaapirest.catalogo.productos.entities.Producto;
import com.deofis.tiendaapirest.catalogo.skus.entities.Sku;
import com.deofis.tiendaapirest.utils.crud.CrudService;

import java.util.Map;

/**
 * Este servicio se encarga de los SKUs, es responsable de la creación, actualización, obtención
 * y eliminación de los mismos, asi como de la generación skus a partir de las distintas combinaciones
 * de propiedades de un Producto.
 *
 * Un {@link Sku} es un {@link Producto} vendible, es decir, es lo que realmente se vende/guarda en el carrito.
 */

public interface SkuService extends CrudService<Sku, Long> {

    /**
     * Crea un nuevo SKU a partir de un {@link Producto}. Por defecto, es creado activo.
     * @param sku Sku nuevo a crear y guardar.
     * @param producto Producto asociado al SKU por crear.
     * @return Sku creado y guardado.
     */
    Sku crearNuevoSku(Sku sku, Producto producto);

    /**
     * Genera SKUs de todas las posibles combinaciones de las propiedades de un producto, a partir
     * de un {@link Producto}.
     * Por ejemplo: Si un producto REMERA tiene TALLE (XL, S) y COLOR (Gris, Negra), se generarían
     * 8 combinaciones: XL/Gris, XL/Negra, S/Gris, S/Negra.
     * @param producto Producto al cual se generarán los SKUs con distintas combinaciones.
     * @return Map que contiene la cantidad y las combinaciones generadas.
     */
    Map<String, Object> generarSkusProducto(Producto producto);

    /**
     * Obtiene un SKU a traves de su ID.
     * @param skuId Long id del Sku requerido.
     * @return Sku requerido.
     */
    Sku obtenerSku(Long skuId);

    /**
     * Actualiza por completo los datos de un SKU.
     * @param skuId Long id del sku a actualizar.
     * @param sku Sku actualizado.
     * @return Sku actualizado y guardado.
     */
    Sku actualizarSku(Long skuId, Sku sku);

    /**
     * Actualiza la disponibilidad de un SKU requerido.
     * @param skuId Long id del sku a actualizar disponibilidad.
     * @param disponibilidad Integer nuevo valor de disponibilidad.
     * @return Sku actualizado y guardado.
     */
    Sku actualizarDisponibilidad(Long skuId, Integer disponibilidad);

    /**
     * Actualiza el precio de un SKU requerido.
     * @param skuId Long id del sku a actualizar precio.
     * @param precio Double precio nuevo.
     * @return Sku actualizado y guardado.
     */
    Sku actualizarPrecio(Long skuId, Double precio);

    /**
     * Elimina un SKU requerido.
     * @param skuId Long id del SKU a eliminar.
     */
    void eliminarSku(Long skuId);

    /**
     * Realiza la baja lógica de un SKU.
     * @param sku {@link Sku} a dar de baja
     */
    void darDeBaja(Sku sku);

    /**
     * Da de alta un SKU existente.
     * @param sku {@link Sku} a dar de alta.
     */
    void darDeAlta(Sku sku);
}
