package com.deofis.tiendaapirest.catalogo.promociones.services;

import com.deofis.tiendaapirest.catalogo.categorias.entities.Subcategoria;
import com.deofis.tiendaapirest.catalogo.productos.entities.Producto;
import com.deofis.tiendaapirest.catalogo.promociones.entities.Promocion;
import com.deofis.tiendaapirest.catalogo.skus.entities.Sku;

/**
 * Servicio que se encarga de crear y obtener las {@link Promocion}es (ofertas) asociadas a
 * {@link Producto}s o a {@link Sku}s. Los Productos y Skus pueden tener asociada solo una
 * promoción al mismo tiempo, con los datos de vigencia y precios de oferta.
 */

public interface PromocionService {

    /**
     * Programa una nueva oferta para un {@link Producto}, asociandole la nueva {@link Promocion} a crear,
     * con el precio de oferta y fecha de vigencia requerida.
     * @param productoId Long id del producto a programar la promoción.
     * @param promocion Promocion con los datos:  precio que tendrá prodoucto durante la
     *                     vigencia de la promoción, fecha hasta que será vigente la misma.
     * @return Producto actualizado con la promoción creada.
     */
    Producto programarOfertaProducto(Long productoId, Promocion promocion);

    /**
     * Programa una nueva oferta para un {@link Sku}, asociandole la nueva {@link Promocion} a crear,
     * con el precio de oferta y fecha de vigencia requerida.
     * @param skuId Long id del sku a programar la promoción.
     * @param promocion Promocion con los datos:  precio que tendrá el sku durante la
     *                     vigencia de la promoción, fecha hasta que será vigente la misma.
     * @return Sku actualizado con la promoción creada.
     */
    Sku programarOfertaSku(Long skuId, Promocion promocion);

    /**
     * Programa una {@link Promocion} para todos los {@link Producto}s, y sus {@link Sku},
     * que pertenecen a la {@link Subcategoria}
     * requerida.
     * @param subcategoriaId Long id de la subcategoría a crear promociones.
     * @param promocion Promocion con los datos para crear la promoción: porcentaje a aplicar en
     *                  las promociones, y las fechas (desde, hasta) de vigencia.
     * @return Integer cantidad de productos promocionados.
     */
    Integer programarOfertaProductosSubcategoria(Long subcategoriaId, Promocion promocion);

    /**
     * Obtiene la {@link Promocion} de un {@link Producto} requerido.
     * @param productoId Long id del producto.
     * @return Promocion.
     */
    Promocion obtenerPromocionProducto(Long productoId);

    /**
     * Obtiene la {@link Promocion} de un {@link Sku} requerido.
     * @param skuId Long id del sku.
     * @return Promocion
     */
    Promocion obtenerPromocionSku(Long skuId);

    /**
     * Remueve la {@link Promocion} del producto y su {@link Sku} por defecto, así como todas
     * las promociones de los {@link Sku}s que tiene el producto.
     * @param producto {@link Producto} a remover promoción para todos sus skus.
     */
    void removerPromocionProducto(Producto producto);

    /**
     * Remueve la {@link Promocion} del producto requerido. No afecta las promomociones de sus skus.
     * <br>
     * Notar que: Si el producto posee Skus (+defaultSku), este servicio solo revoca la promoción "visual"
     * del producto, es decir, la promoción que verán los usuarios al visualizar el producto. Todas sus
     * variaciones, items vendibles (skus), permanecen con su promoción en caso de tenerla.
     * @param producto {@link Producto} a eliminar promoción.
     */
    void removerPromocionProductoSinSkus(Producto producto);

    /**
     * Remueve la {@link Promocion} para un {@link Sku} requerido.
     * @param sku {@link Sku} a remover promoción.
     */
    void removerPromocionSku(Sku sku);
}
