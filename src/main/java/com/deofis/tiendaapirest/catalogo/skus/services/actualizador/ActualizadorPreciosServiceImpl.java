package com.deofis.tiendaapirest.catalogo.skus.services.actualizador;

import com.deofis.tiendaapirest.catalogo.categorias.entities.Subcategoria;
import com.deofis.tiendaapirest.catalogo.categorias.services.SubcategoriaService;
import com.deofis.tiendaapirest.catalogo.productos.entities.Marca;
import com.deofis.tiendaapirest.catalogo.productos.entities.Producto;
import com.deofis.tiendaapirest.catalogo.productos.services.MarcaService;
import com.deofis.tiendaapirest.catalogo.productos.services.ProductoService;
import com.deofis.tiendaapirest.catalogo.skus.entities.Sku;
import com.deofis.tiendaapirest.catalogo.skus.services.SkuService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ActualizadorPreciosServiceImpl implements ActualizadorPreciosService {

    private final ProductoService productoService;
    private final SkuService skuService;
    private final MarcaService marcaService;
    private final SubcategoriaService subcategoriaService;

    @Override
    public List<Sku> actualizarPreciosSkus(Long productoId, Double porcentaje) {
        Producto producto = this.productoService.obtenerProducto(productoId);
        List<Sku> skusProducto = new ArrayList<>(producto.getSkus());
        skusProducto.add(producto.getDefaultSku());

        for (Sku sku: skusProducto) {
            // actualizar precios...
            Double nuevoPrecio = sku.getPrecio() + (sku.getPrecio() * porcentaje);
            this.skuService.actualizarPrecio(sku.getId(), nuevoPrecio);
        }

        return skusProducto;
    }

    @Override
    public List<Sku> actualizarPreciosSkusMarca(Long marcaId, Double porcentaje) {
        Marca marca = this.marcaService.obtenerMarca(marcaId);
        List<Producto> productos = this.productoService.obtenerProductosMarca(marcaId);

        List<Sku> skusActualizados = new ArrayList<>();
        for (Producto producto: productos) {
            if (producto.getMarca().equals(marca)) {
                // Actualizamos los precios de los skus de dicho producto
                List<Sku> skusProducto = new ArrayList<>(producto.getSkus());
                skusProducto.add(producto.getDefaultSku());

                for (Sku sku: skusProducto) {
                    Double nuevoPrecio = sku.getPrecio() + (sku.getPrecio() * porcentaje);
                    this.skuService.actualizarPrecio(sku.getId(), nuevoPrecio);
                    skusActualizados.add(sku);
                }
            }
        }

        return skusActualizados;
    }

    @Override
    public List<Sku> actualizarPreciosSkusSubcategoria(Long subcategoriaId, Double porcentaje) {
        Subcategoria subcategoria = this.subcategoriaService.obtenerSubcategoria(subcategoriaId);
        List<Producto> productos = this.productoService.obtenerProductosSubcategoria(subcategoriaId);

        List<Sku> skusActualizados = new ArrayList<>();

        for (Producto producto: productos) {
            if (producto.getSubcategoria().equals(subcategoria)) {
                List<Sku> skusProducto = new ArrayList<>(producto.getSkus());
                skusProducto.add(producto.getDefaultSku());

                for (Sku sku: skusProducto) {
                    Double nuevoPrecio = sku.getPrecio() + (sku.getPrecio() * porcentaje);
                    this.skuService.actualizarPrecio(sku.getId(), nuevoPrecio);
                    skusActualizados.add(sku);
                }
            }
        }

        return skusActualizados;
    }
}
