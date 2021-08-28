package com.deofis.tiendaapirest.catalogo.services;

import com.deofis.tiendaapirest.catalogo.categorias.entities.Subcategoria;
import com.deofis.tiendaapirest.catalogo.categorias.repositories.SubcategoriaRepository;
import com.deofis.tiendaapirest.catalogo.productos.entities.Marca;
import com.deofis.tiendaapirest.catalogo.productos.entities.Producto;
import com.deofis.tiendaapirest.catalogo.productos.entities.PropiedadProducto;
import com.deofis.tiendaapirest.catalogo.productos.exceptions.ProductoException;
import com.deofis.tiendaapirest.catalogo.productos.repositories.MarcaRepository;
import com.deofis.tiendaapirest.catalogo.productos.repositories.ProductoRepository;
import com.deofis.tiendaapirest.catalogo.productos.services.BuscadorProductosService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CatalogoServiceImpl implements CatalogoService {

    private final ProductoRepository productoRepository;
    private final SubcategoriaRepository subcategoriaRepository;
    private final MarcaRepository marcaRepository;
    private final BuscadorProductosService buscadorProductosService;

    @Transactional(readOnly = true)
    @Override
    public Map<String, Object> buscarProductos(String termino) {
        Map<String, Object> resultadoBusqueda = new HashMap<>();
        List<Producto> productos = this.buscadorProductosService.buscarProductos(termino);
        List<Marca> marcas = this.buscadorProductosService.marcasDeProductosEncontrados(termino);
        List<Subcategoria> subcategorias = this.buscadorProductosService.subcategoriasDeProductosEncontrados(termino);
        List<PropiedadProducto> propiedades = this.buscadorProductosService.propiedadesDeProductosEncontrados(termino);

        resultadoBusqueda.put("totalProductos", productos.size());
        resultadoBusqueda.put("productos", productos);
        resultadoBusqueda.put("marcas", marcas);
        resultadoBusqueda.put("subcategorias", subcategorias);
        resultadoBusqueda.put("propiedades", propiedades);
        return resultadoBusqueda;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Marca> listarMarcas() {
        return this.marcaRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Producto> obtenerProductosDestacados() {
        return this.productoRepository
                .findAllByDestacadoIsTrueAndActivoIsTrue();
    }

    @Transactional(readOnly = true)
    @Override
    public Producto obtenerProducto(Long id) {
        return this.productoRepository.findById(id)
                .orElseThrow(() -> new ProductoException("No existe el producto con id: " + id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Producto> productosPrecioMenorMayor() {
        return this.productoRepository.findAllByOrderByPrecioAsc();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Producto> productosPrecioMayorMenor() {
        return this.productoRepository.findAllByOrderByPrecioDesc();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Producto> productosPorSubcategoria(Long subcategoriaId) {
        Subcategoria subcategoria = this.subcategoriaRepository.findById(subcategoriaId)
                .orElseThrow(() -> new ProductoException("No se encontró la categoria con id: " + subcategoriaId));

        return this.productoRepository.findAllBySubcategoriaAndActivoIsTrue(subcategoria);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Producto> productosPorMarca(Long marcaId) {
        Marca marca = this.marcaRepository.findById(marcaId)
                .orElseThrow(() -> new ProductoException("No se encontró la marca con id: " + marcaId));

        return this.productoRepository.findAllByMarcaAndActivoIsTrue(marca);
    }

    @Override
    public List<Producto> productosPorPrecio(Double precioMax) {
        return this.productoRepository.findAllByPrecioBetween(0.00, precioMax);
    }
}
