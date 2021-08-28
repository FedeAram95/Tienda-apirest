package com.deofis.tiendaapirest.catalogo.skus.services;

import com.deofis.tiendaapirest.catalogo.skus.services.archivoshandler.SkuArchivosGenerador;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Este servicio se encarga de exportar archivos (excel, etc) con listados de skus para su
 * actualización masiva.
 */
public interface SkuExportadorService {

    /**
     * Exporta plantilla para actualizar precios/disponibilidad para todos los skus registrados en el sistema.
     * @param skuArchivosGenerador {@link SkuArchivosGenerador} que manejara la generación del archivo.
     * @return {@link ByteArrayInputStream} representando al archivo generado.
     * @throws IOException generación de archivo.
     */
    ByteArrayInputStream exportar(SkuArchivosGenerador skuArchivosGenerador) throws IOException;

    /**
     * Exporta plantilla para actualizar precios/disponibilidad para skus de un producto requerido,
     * a través de su id.
     * @param skuArchivosGenerador {@link SkuArchivosGenerador} que manejara la generación del archivo.
     * @param productoId Long id del producto a generar archivo con sus skus.
     * @return {@link ByteArrayInputStream} representando al archivo generado.
     * @throws IOException generación de archivo.
     */
    ByteArrayInputStream exportar(SkuArchivosGenerador skuArchivosGenerador, Long productoId) throws IOException;

    /**
     * Exporta plantilla para actualizar precios/disponibilidad para skus que pertenecen a productos de una marca
     * requerida, a través del id de la marca.
     * @param skuArchivosGenerador {@link SkuArchivosGenerador} que manejara la generación del archivo.
     * @param marcaId Long id de la marca a la que pertenecen los productos a exportar skus.
     * @return {@link ByteArrayInputStream} representando al archivo generado.
     * @throws IOException generación de archivo.
     */
    ByteArrayInputStream exportarPorMarca(SkuArchivosGenerador skuArchivosGenerador, Long marcaId) throws IOException;

    /**
     * Exporta plantilla para actualizar precios/disponibilidad para skus que pertenecen a productos de una subcategoría
     * requerida, a través del id de la subcategoría.
     * @param skuArchivosGenerador {@link SkuArchivosGenerador} que manejara la generación del archivo.
     * @param subcategoriaId Long id de la subcategoría a la que pertenecen los productos a exportar skus.
     * @return {@link ByteArrayInputStream} representando al archivo generado.
     * @throws IOException generación de archivo.
     */
    ByteArrayInputStream exportarPorSubcategoria(SkuArchivosGenerador skuArchivosGenerador, Long subcategoriaId) throws IOException;
}
