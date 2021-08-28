package com.deofis.tiendaapirest.catalogo.services;

import com.deofis.tiendaapirest.catalogo.productos.entities.Marca;
import com.deofis.tiendaapirest.catalogo.productos.entities.Producto;

import java.util.List;
import java.util.Map;

/**
 * Este servicio tiene la lógica relacionada con el catálogo de productos para mostrar,
 * tales como, obtener los productos por categoria, por marca, etc.
 */
public interface CatalogoService {

    /**
     * Busca y devuelve un listado de productos que contienen al termino que se envía. El listado
     * de productos que se genera a partir del termino debe ser por: nombre, marca o subcategoría
     * del producto.
     * <br>
     * Ej.: Termino = 'celulares' --> Busca todos los productos que contengan 'celulares' en su
     * nombre, marca o subcategoría, y si los encuentra, los agrega al listado a devolver.
     * @param termino String consulta sobre los productos a buscar.
     * @return List listado de productos que coinciden con el término de búsqueda.
     */
    Map<String, Object> buscarProductos(String termino);

    /**
     * Obtiene un listado desordenado de todas las marcas.
     * @return List de marcas.
     */
    List<Marca> listarMarcas();

    /**
     * Obtiene el listado de los productos destacados, que al ser destacados, tambiíen tienen que
     * estar activos.
     * @return List listado de productos activos y destacados.
     */
    List<Producto> obtenerProductosDestacados();

    /**
     * Obtiene un producto por id.
     * @param id Long id del producto.
     * @return Producto obtenido.
     */
    Producto obtenerProducto(Long id);

    /**
     * Obtiene los producots ordenados por precio de menor a mayor.
     * @return List productos ordenados por precio
     */
    List<Producto> productosPrecioMenorMayor();

    /**
     * Obtiene los producots ordenados por precio de mayor a menor.
     * @return List productos ordenados por precio
     */
    List<Producto> productosPrecioMayorMenor();

    /**
     * Obtiene un listado de los productos que pertenecen a una categoría.
     * @param subcategoriaId Long id de la categoría.
     * @return List listado de productos filtrados por categoria.
     */
    List<Producto> productosPorSubcategoria(Long subcategoriaId);

    /**
     * Obtiene un listado de los productos de una marca.
     * @param marcaId Long id de la marca.
     * @return List de los productos filtrados por marca.
     */
    List<Producto> productosPorMarca(Long marcaId);

    /**
     * Obtiene los productos que van de $0 a un máximo precio.
     * @param precioMax Double precio máximo a filtrar.
     * @return List productos.
     */
    List<Producto> productosPorPrecio(Double precioMax);
}
