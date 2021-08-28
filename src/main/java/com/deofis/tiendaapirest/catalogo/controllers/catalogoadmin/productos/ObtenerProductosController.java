package com.deofis.tiendaapirest.catalogo.controllers.catalogoadmin.productos;

import com.deofis.tiendaapirest.catalogo.productos.entities.Producto;
import com.deofis.tiendaapirest.catalogo.productos.entities.PropiedadProducto;
import com.deofis.tiendaapirest.catalogo.productos.entities.UnidadMedida;
import com.deofis.tiendaapirest.catalogo.productos.exceptions.ProductoException;
import com.deofis.tiendaapirest.catalogo.productos.services.ProductoService;
import com.deofis.tiendaapirest.catalogo.skus.entities.Sku;
import com.deofis.tiendaapirest.catalogo.skus.exceptions.SkuException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador que se encarga de obtener los productos de distintas formas.
 */

@RestController
@RequestMapping("/api/catalogo")
@AllArgsConstructor
public class ObtenerProductosController {

    private final ProductoService productoService;

    /**
     * Obtiene los productos ordenados alfabéticamente.
     * URL: ~/api/catalogo/productos
     * HttpMethod: GET
     * HttpStatus: OK
     * @return ResponseEntity con un listado de todos los productos.
     */
    @GetMapping("/productos")
    public ResponseEntity<?> obtenerProductos() {
        Map<String, Object> response = new HashMap<>();
        List<Producto> productos;

        try {
            productos = this.productoService.obtenerProductos();
        } catch (ProductoException e) {
            response.put("mensaje", "Error al obtener los productos de la Base de Datos");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (productos.size() == 0) {
            response.put("error", "No existen productos registrados en la Base de Datos");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response.put("productos", productos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Obtiene todos los productos del catálogo que tienen una marca requerida.
     * URL: ~/api/catalogo/productos/marcas/1
     * HttpMethod: GET
     * HttpStatus: OK
     * @param marcaId Long id de la marca.
     * @return ResponseEntity con listado de productos.
     */
    @GetMapping("/productos/por-marca/{marcaId}")
    public ResponseEntity<?> obtenerProductosMarca(@PathVariable Long marcaId) {
        Map<String, Object> response = new HashMap<>();
        List<Producto> productosMarca;

        try {
            productosMarca = this.productoService.obtenerProductosMarca(marcaId);
        } catch (ProductoException e) {
            response.put("mensaje", "Error al obtener los productos de la marca");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("productos", productosMarca);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Obtiene todos los productos del catálogo que tienen una subcategoria requerida.
     * URL: ~/api/catalogo/productos/por-subcategoria/1
     * HttpMethod: GET
     * HttpStatus: OK
     * @param subcategoriaId Long id de la subcategoría.
     * @return ResponseEntity con listado de productos.
     */
    @GetMapping("/productos/por-subcategoria/{subcategoriaId}")
    public ResponseEntity<?> obtenerProductosSubcategoria(@PathVariable Long subcategoriaId) {
        Map<String, Object> response = new HashMap<>();
        List<Producto> productosSubcategoria;

        try {
            productosSubcategoria = this.productoService.obtenerProductosSubcategoria(subcategoriaId);
        } catch (ProductoException e) {
            response.put("mensaje", "Error al obtener los productos de la subcategoría");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("productos", productosSubcategoria);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Obtiene un producto específico.
     * URL: ~/api/catalogo/productos/1
     * HttpStatus: OK
     * HttpMethod: GET
     * @param productoId PathVarible Long con el id solicitado.
     * @return ResponseEntity con el Producto.
     */
    @GetMapping("/productos/{productoId}")
    public ResponseEntity<?> obtenerProducto(@PathVariable Long productoId) {

        Map<String, Object> response = new HashMap<>();
        Producto producto;

        try {
            producto = this.productoService.obtenerProducto(productoId);
        } catch (ProductoException e) {
            response.put("mensaje", "Error al obtener el producto");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("producto", producto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Obtiene las propiedades de un producto requerido.
     * URL: ~/api/catalogo/productos/1/propiedades
     * HttpMethod: GET
     * HttpStatus: OK
     * @param productoId Long id del producto a listar propiedades.
     * @return ResponseEntity listado de propiedades.
     */
    @GetMapping("/productos/{productoId}/propiedades")
    public ResponseEntity<?> obtenerPropiedadesDeProducto(@PathVariable Long productoId) {
        Map<String, Object> response = new HashMap<>();
        List<PropiedadProducto> propiedades;
        String producto;

        try {
            propiedades = this.productoService.obtenerPropiedadesDeProducto(productoId);
            producto = this.productoService.obtenerProducto(productoId).getNombre();
        } catch (ProductoException e) {
            response.put("mensaje", "Error al obtener las propiedades del producto");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("producto", producto);
        response.put("propiedades", propiedades);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Obtiene todos los Skus de un producto.
     * URL: ~/api/catalogo/productos/1/skus
     * HttpMethod: GET
     * HttpStatus: OK
     * @param productoId Long id del producto.
     * @return ResponseEntity con el listado de skus del producto.
     */
    @GetMapping("/productos/{productoId}/skus")
    public ResponseEntity<?> obtenerSkusProducto(@PathVariable Long productoId) {
        Map<String, Object> response = new HashMap<>();
        List<Sku> skus;

        try {
            skus = this.productoService.obtenerSkusProducto(productoId);
        } catch (ProductoException e) {
            response.put("mensaje", "Error al obtener los skus del producto");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("skus", skus);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Obtiene un Sku a partir de un producto.
     * URL: ~/api/catalogo/productos/1/skus/1
     * HttpMethod: GET
     * HttpStatus: OK
     * @param productoId PathVariable Long id del producto.
     * @param skuId PathVariable Long id del sku.
     * @return ResponseEntity con el nombre del producto y el sku.
     */
    @GetMapping("/productos/{productoId}/skus/{skuId}")
    public ResponseEntity<?> obtenerSkuDeProducto(@PathVariable Long productoId,
                                                  @PathVariable Long skuId) {
        Map<String, Object> response = new HashMap<>();
        Sku sku;
        String productoNombre;

        try {
            sku = this.productoService.obtenerSkuProducto(productoId, skuId);
            productoNombre = this.productoService.obtenerProducto(productoId).getNombre();
        } catch (ProductoException | SkuException e) {
            response.put("mensaje", "Error al obtener sku del producto");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("producto", productoNombre);
        response.put("sku", sku);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Obtiene el Sku por defecto de un producto.
     * URL: ~/api/catalogo/productos/1/skus/defecto
     * HttpMethod: GET
     * HttpStatus: OK
     * @param productoId PathVariable Long id del producto.
     * @return ResponseEntity con el nombre del producto y su default sku.
     */
    @GetMapping("/productos/{productoId}/skus/defecto")
    public ResponseEntity<?> obtenerSkuPorDefectoDeProducto(@PathVariable Long productoId) {
        Map<String, Object> response = new HashMap<>();
        Sku sku;
        String productoNombre;

        try {
            sku = this.productoService.obtenerSkuDefectoProducto(productoId);
            productoNombre = this.productoService.obtenerProducto(productoId).getNombre();
        } catch (ProductoException e) {
            response.put("mensaje", "Error al obtener sku por defecto del producto");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("producto", productoNombre);
        response.put("sku", sku);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Obtiene listado de las unidades de medida.
     * URL: ~/api/catalogo/productos/unidades-medida
     * HttpMethod: GET
     * HttpStatus: OK
     * @return ResponseEntity con el listado de unidades de medida.
     */
    @GetMapping("/productos/unidades-medida")
    public ResponseEntity<?> obtenerUnidadesMedida() {
        Map<String, Object> response = new HashMap<>();
        List<UnidadMedida> unidadesMedida;

        try {
            unidadesMedida = this.productoService.obtenerUnidadesMedida();
        } catch (ProductoException e) {
            response.put("mensaje", "Error al obtener las unidades de medida");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(unidadesMedida, HttpStatus.OK);
    }
}
