package com.deofis.tiendaapirest.catalogo.controllers.catalogoadmin.skus;

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

/**
 * API que se encarga de dar de alta/baja a un SKU que se requiera.
 */
@RestController
@RequestMapping("/api/catalogo")
@AllArgsConstructor
public class BajaAltaSkuController {

    private final CatalogoAdminService catalogoAdminService;

    /**
     * API que implementa la baja lógica de un SKU requerido.
     * URL: ~/api/catalogo/productos/skus/1/baja
     * HttpMethod: POST
     * HttpStatus: OK
     * @param skuId Long id del SKU a dar de baja.
     * @return ResponseEntity con mensaje de éxito/error.
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("/productos/skus/{skuId}/baja")
    public ResponseEntity<?> darDeBajaSku(@PathVariable Long skuId) {
        Map<String, String> response = new HashMap<>();
        String msg;

        try {
            this.catalogoAdminService.bajaSku(skuId);
            msg = "Sku con id: " + skuId + " dado de baja con éxito";
        } catch (SkuException e) {
            response.put("mensaje", "Error al dar de baja al sku");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", msg);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * API que implementa el alta lógica de un SKU requerido.
     * URL: ~/api/catalogo/productos/skus/1/alta
     * HttpMethod: POST
     * HttpStatus: OK
     * @param skuId Long id del SKU a dar de alta.
     * @return ResponseEntity con mensaje de éxito/error.
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("/productos/skus/{skuId}/alta")
    public ResponseEntity<Map<String, String>> darDeAltaSku(@PathVariable Long skuId) {
        Map<String, String> response = new HashMap<>();
        String msg;

        try {
            this.catalogoAdminService.altaSku(skuId);
            msg = "Sku con id: " + skuId + " dado de alta con éxito";
        } catch (SkuException e) {
            response.put("mensaje", "Error al dar de alta al sku");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", msg);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
