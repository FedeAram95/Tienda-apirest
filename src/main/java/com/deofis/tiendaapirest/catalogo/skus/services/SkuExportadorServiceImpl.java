package com.deofis.tiendaapirest.catalogo.skus.services;

import com.deofis.tiendaapirest.catalogo.categorias.entities.Subcategoria;
import com.deofis.tiendaapirest.catalogo.categorias.services.SubcategoriaService;
import com.deofis.tiendaapirest.catalogo.productos.entities.Marca;
import com.deofis.tiendaapirest.catalogo.productos.entities.Producto;
import com.deofis.tiendaapirest.catalogo.productos.services.MarcaService;
import com.deofis.tiendaapirest.catalogo.productos.services.ProductoService;
import com.deofis.tiendaapirest.catalogo.skus.entities.Sku;
import com.deofis.tiendaapirest.catalogo.skus.services.archivoshandler.SkuArchivosGenerador;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SkuExportadorServiceImpl implements SkuExportadorService {

    private final ProductoService productoService;
    private final MarcaService marcaService;
    private final SubcategoriaService subcategoriaService;
    private final SkuService skuService;

    @Override
    public ByteArrayInputStream exportar(SkuArchivosGenerador skuArchivosGenerador) throws IOException {
        List<Sku> skus = this.skuService.findAll();
        ByteArrayInputStream streamSkus;

        streamSkus = skuArchivosGenerador.generarArchivo(skus);

        return streamSkus;
    }

    @Override
    public ByteArrayInputStream exportar(SkuArchivosGenerador skuArchivosGenerador, Long productoId)
            throws IOException {
        Producto producto = this.productoService.obtenerProducto(productoId);
        // Obtenemos todos los skus de dicho producto
        List<Sku> skusProducto = new ArrayList<>(producto.getSkus());
        skusProducto.add(producto.getDefaultSku());

        return skuArchivosGenerador.generarArchivo(skusProducto);
    }

    @Override
    public ByteArrayInputStream exportarPorMarca(SkuArchivosGenerador skuArchivosGenerador, Long marcaId)
            throws IOException {
        Marca marca = this.marcaService.obtenerMarca(marcaId);
        List<Producto> productos = this.productoService.obtenerProductos();

        // List con los skus necesarios
        List<Sku> skusProductos = new ArrayList<>();

        // Obtenemos los productos de dicha marca, y de esos productos, todos sus skus
        for (Producto producto: productos) {
            if (producto.getMarca().equals(marca)) {
                skusProductos.addAll(producto.getSkus());
                skusProductos.add(producto.getDefaultSku());
            }
        }

        // Otra forma de obtener productos de marca
        // List<Producto> productosMarca = this.productoRepository.findAllByMarca(marca);

        // Devolvemos el archivo generado con dichos skus
        return skuArchivosGenerador.generarArchivo(skusProductos);
    }

    @Override
    public ByteArrayInputStream exportarPorSubcategoria(SkuArchivosGenerador skuArchivosGenerador, Long subcategoriaId)
            throws IOException {
        Subcategoria subcategoria = this.subcategoriaService.obtenerSubcategoria(subcategoriaId);
        List<Producto> productos = this.productoService.obtenerProductos();

        List<Sku> skusProductos = new ArrayList<>();

        for (Producto producto: productos) {
            if (producto.getSubcategoria().equals(subcategoria)) {
                skusProductos.addAll(producto.getSkus());
                skusProductos.add(producto.getDefaultSku());
            }
        }

        return skuArchivosGenerador.generarArchivo(skusProductos);
    }
}
