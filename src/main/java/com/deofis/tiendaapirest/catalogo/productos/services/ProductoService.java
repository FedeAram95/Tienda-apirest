package com.deofis.tiendaapirest.catalogo.productos.services;

import com.deofis.tiendaapirest.catalogo.categorias.entities.Subcategoria;
import com.deofis.tiendaapirest.catalogo.productos.entities.Producto;
import com.deofis.tiendaapirest.catalogo.productos.entities.PropiedadProducto;
import com.deofis.tiendaapirest.catalogo.productos.entities.UnidadMedida;
import com.deofis.tiendaapirest.catalogo.skus.entities.Sku;
import com.deofis.tiendaapirest.utils.crud.CrudService;

import java.util.List;

/**
 * Servicio que se encarga de los {@link Producto}. Maneja la creación, actualización y obtención de productos,
 * asi como la activación/desactivación de los mismos. También es responsable de destacar o quitar de
 * destacados a un producto requerido.
 */

public interface ProductoService extends CrudService<Producto, Long> {

    /**
     * Método que se encarga de registrar un nuevo producto en la BD y devolverlo. También
     * se encarga de generar el SKU por defecto.
     * @param producto Recibe un producto con datos validos desde el controlador.
     * @return Producto guardado en la Base de Datos.
     */
    Producto crearProducto(Producto producto);

    /**
     * Método que lista todos los productos registrados.
     * @return List de Productos.
     */
    List<Producto> obtenerProductos();

    /**
     * Obtiene listado de todos los productos que son de una marca requerida.
     * @param marcaId Long id de la marca.
     * @return {@link List<Producto>} listado de productos de la marca.
     */
    List<Producto> obtenerProductosMarca(Long marcaId);

    /**
     * Obtiene listado de todos los productos que son de una subcategoría requerida.
     * @param subcategoriaId Long id de la subcategoría.
     * @return {@link List<Producto>} listado de productos de la subcategoría.
     */
    List<Producto> obtenerProductosSubcategoria(Long subcategoriaId);

    /**
     * Método que obtiene un producto guardado a través del Id.
     * @param id Long con el valor del id del producto.
     * @return Producto en la BD.
     */
    Producto obtenerProducto(Long id);

    /**
     * Método que modifica algún dato del producto, lo guarda y devuelve actualizado.
     * @param producto Producto ya modificado.
     * @param id Long del id del producto a modificar.
     * @return Producto actualizado.
     */
    Producto actualizarDatosProducto(Producto producto, Long id);

    /**
     * Actualiza la disponibilidad general de un {@link Producto} y la disponibilidad
     * de su {@link Sku} por defecto.
     * @param disponibilidadGral Integer nueva disponibilidad.
     * @param productoId Long id del producto.
     * @return Producto con disponibilidad actualizada.
     */
    Producto actualizarDisponibilidadGeneralProducto(Integer disponibilidadGral, Long productoId);

    /**
     * Establece un nuevo precio base para un {@link Producto} y su {@link Sku} por defecto.
     * @param precio Double precio nuevo.
     * @param productoId Long id del producto.
     * @return Producto con precio base actualizado.
     */
    Producto actualizarPrecioBaseProducto(Double precio, Long productoId);

    /**
     * Método para registrar la baja lógica de un producto.
     * @param id Long id del producto a dar de baja.
     */
    void darDeBaja(Producto producto, Long id);

    /**
     * Método para registrar el alta de un producto.
     * @param id Long id del producto a activar.
     */
    void darDeAlta(Producto producto, Long id);

    /**
     * Método para destacar/quitar de destacados a un producto.
     * @param producto Producto a destacar/quitar destacados.
     * @param id Long del id del producto.
     */
    void destacar(Producto producto, Long id);

    /**
     * Obtiene un listado con todas las unidades de medida disponibles.
     * @return List unidades medida.
     */
    List<UnidadMedida> obtenerUnidadesMedida();

    /**
     * Obtiene un listado de todas las propiedades asociadas a un producto.
     * @param productoId Long id del producto requerido a listar sus propiedades.
     * @return List de las propiedades del producto requerido.
     */
    List<PropiedadProducto> obtenerPropiedadesDeProducto(Long productoId);

    /**
     * Obtiene un listado con todos los SKUs de un producto requerido.
     * @param productoId Long id del producto a listar sus SKUs.
     * @return List de SKUs.
     */
    List<Sku> obtenerSkusProducto(Long productoId);

    /**
     * Obtiene un {@link Sku} requerido de un {@link Producto}.
     * @param productoId Long id del producto al que pertenece el sku.
     * @param skuId Long id del sku.
     * @return Sku.
     */
    Sku obtenerSkuProducto(Long productoId, Long skuId);

    /**
     * Obtiene el {@link Sku} por defecto de un {@link Producto} requerido.
     * @param productoId Long id del producto.
     * @return Sku.
     */
    Sku obtenerSkuDefectoProducto(Long productoId);

    /**
     * Obtiene un listado de todos los {@link Producto}s pertenecientes a una
     * {@link Subcategoria} requerida.
     * @param subcategoria Subcategoria requerida.
     * @return List listado de productos pertenecientes a la subcategoría.
     */
    List<Producto> productosEnSubcategoria(Subcategoria subcategoria);
}
