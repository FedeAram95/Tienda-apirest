package com.deofis.tiendaapirest.catalogo.productos.services;

import com.deofis.tiendaapirest.catalogo.categorias.entities.Subcategoria;
import com.deofis.tiendaapirest.catalogo.productos.entities.Marca;
import com.deofis.tiendaapirest.catalogo.productos.entities.Producto;
import com.deofis.tiendaapirest.catalogo.productos.entities.PropiedadProducto;

import java.util.List;

/**
 * Servicio que se encarga de buscar productos por un término requerido.
 */

public interface BuscadorProductosService {

    /**
     * Busca productos por término. Este término debe coincidir con nombres de productos, marcas o
     * subcategorias del mismo y el listado de productos debe contener productos sin repetirse.
     * @param termino String termino a buscar. Puede contener: nombre, marca o subcategoría de producto.
     * @return List listado de productos encontrados.
     */
    List<Producto> buscarProductos(String termino);

    /**
     * Busca los productos por término, y devuelve un listado con las marcas pertenecientes a productos
     * encontrados.
     * <br>
     * Ej.: Termino = 'celulares' --> Del array de productos encontrados al término, genera un listado
     * con las marcas de esos productos: 'Samsung', 'Apple', etc...
     * @param termino String consulta sobre los productos a buscar (mismo que buscar productos).
     * @return List listado de marcas que son de productos encontrados.
     */
    List<Marca> marcasDeProductosEncontrados(String termino);

    /**
     * Busca los productos por término, y devuelve un listado con las subcategorías pertenecientes a productos
     * encontrados.
     * <br>
     * Ej.: Termino = 'celulares' --> Del array de productos encontrados al término, genera un listado
     * con las subcategorías de esos productos: 'Celulares'.
     * @param termino String consulta sobre los productos a buscar (mismo que buscar productos).
     * @return List listado de subcategorías que son de productos encontrados.
     */
    List<Subcategoria> subcategoriasDeProductosEncontrados(String termino);

    /**
     * Busca los productos por término, y devuelve un listado con las propiedades pertenecientes a productos
     * encontrados.
     * <br>
     * Ej.: Termino = 'celulares' --> Del array de productos encontrados al término, genera un listado
     * con las propiedades de esos productos: 'Color', 'Modelo', etc...
     * @param termino String consulta sobre los productos a buscar (mismo que buscar productos).
     * @return List listado de propiedades que son de productos encontrados.
     */
    List<PropiedadProducto> propiedadesDeProductosEncontrados(String termino);
}
