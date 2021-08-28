package com.deofis.tiendaapirest.catalogo.controllers.catalogoadmin.skus;

import com.deofis.tiendaapirest.catalogo.productos.exceptions.ProductoException;
import com.deofis.tiendaapirest.catalogo.skus.entities.Sku;
import com.deofis.tiendaapirest.catalogo.skus.exceptions.SkuException;
import com.deofis.tiendaapirest.catalogo.skus.services.actualizador.ActualizadorPreciosService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API que se encarga de la actualización de precios para los skus.
 */
@RestController
@RequestMapping("/api/catalogo")
@AllArgsConstructor
public class ActualizarPreciosController {

    private final ActualizadorPreciosService actualizadorPreciosService;

    /**
     * API para actualizar el precio de todos los skus que pertenecen a un producto requerido.
     * URL: ~/api/catalogo/productos/1/skus/precios
     * HttpMethod: PUT
     * HttpStatus: OK
     * @param productoId Long id del producto.
     * @return ResponseEntity con el listado de skus actualizados.
     */
    @Secured("ROLE_ADMIN")
    @PutMapping("/productos/{productoId}/skus/precios")
    public ResponseEntity<?> actualizarPreciosSkusPorProducto(@PathVariable Long productoId,
                                                              @RequestParam(name = "porcentaje") Double porcentaje) {
        Map<String, Object> response = new HashMap<>();
        List<Sku> skusActualizados;

        try {
            skusActualizados = this.actualizadorPreciosService.actualizarPreciosSkus(productoId, porcentaje);
        } catch (ProductoException | SkuException e) {
            response.put("mensaje", "Error al actualizar precios de skus");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("skus", skusActualizados);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * API para actualizar el precio de todos los skus que pertenecen a productos de una marca requerida.
     * URL: ~/api/catalogo/productos/marcas/1/skus/precios
     * HttpMethod: PUT
     * HttpStatus: OK
     * @param marcaId Long id de la marca.
     * @return ResponseEntity con el listado de skus actualizados.
     */
    @PutMapping("/productos/marcas/{marcaId}/skus/precios")
    public ResponseEntity<?> actualizarPreciosSkusPorMarca(@PathVariable Long marcaId,
                                                           @RequestParam(name = "porcentaje") Double porcentaje) {
        Map<String, Object> response = new HashMap<>();
        List<Sku> skusActualizados;

        try {
            skusActualizados = this.actualizadorPreciosService.actualizarPreciosSkusMarca(marcaId, porcentaje);
        } catch (ProductoException | SkuException e) {
            response.put("mensaje", "Error al actualizar precios de skus");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("skus", skusActualizados);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * API para actualizar el precio de todos los skus que pertenecen a productos de una
     * subcategoría requerida.
     * URL: ~/api/catalogo/productos/subcategoria/1/skus/precios
     * HttpMethod: PUT
     * HttpStatus: OK
     * @param subcategoriaId Long id de la subcategoría.
     * @return ResponseEntity con el listado de skus actualizados.
     */
    @PutMapping("/productos/subcategorias/{subcategoriaId}/skus/precios")
    public ResponseEntity<?> actualizarPreciosSkusPorSubcategoria(@PathVariable Long subcategoriaId,
                                                                  @RequestParam(name = "porcentaje") Double porcentaje) {
        Map<String, Object> response = new HashMap<>();
        List<Sku> skusActualizados;

        try {
            skusActualizados = this.actualizadorPreciosService.actualizarPreciosSkusSubcategoria(subcategoriaId, porcentaje);
        } catch (ProductoException | SkuException e) {
            response.put("mensaje", "Error al actualizar precios de skus");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("skus", skusActualizados);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
