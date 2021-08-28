package com.deofis.tiendaapirest.catalogo.controllers.catalogoadmin.skus;

import com.deofis.tiendaapirest.catalogo.productos.exceptions.ProductoException;
import com.deofis.tiendaapirest.catalogo.services.CatalogoAdminService;
import com.deofis.tiendaapirest.catalogo.skus.exceptions.SkuException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/catalogo")
@AllArgsConstructor
public class GenerarSkusProductoController {

    private final CatalogoAdminService catalogoAdminService;

    /**
     * API para generar todas las combinaciones posibles de {@link com.deofis.tiendaapirest.catalogo.skus.entities.Sku}s
     * para un producto requerido, a través de su id.
     * URL: ~/api/catalogo/productos/1/generarSkus
     * HttpMethod: POST
     * HttpStatus: CREATED
     * @param productoId Long id del producto a generar sus skus.
     * @return ResponseEntity con datos: total combinaciones generadas, skus generados.
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("/productos/{productoId}/generarSkus")
    public ResponseEntity<?> generarSkusProducto(@PathVariable Long productoId) {
        Map<String, Object> response = new HashMap<>();

        try {
            response = this.catalogoAdminService.generarSkusProducto(productoId);
        } catch (ProductoException | SkuException e) {
            response.put("mensaje", "Error al generar los SKUs del producto");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
