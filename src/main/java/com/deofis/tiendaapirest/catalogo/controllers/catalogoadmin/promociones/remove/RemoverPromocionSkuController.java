package com.deofis.tiendaapirest.catalogo.controllers.catalogoadmin.promociones.remove;

import com.deofis.tiendaapirest.catalogo.promociones.exceptions.PromocionException;
import com.deofis.tiendaapirest.catalogo.promociones.services.PromocionService;
import com.deofis.tiendaapirest.catalogo.skus.entities.Sku;
import com.deofis.tiendaapirest.catalogo.skus.exceptions.SkuException;
import com.deofis.tiendaapirest.catalogo.skus.services.SkuService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * API para remover promociones de skus individuales.
 */
@RestController
@RequestMapping("/api/catalogo")
@AllArgsConstructor
public class RemoverPromocionSkuController {

    private final SkuService skuService;
    private final PromocionService promocionService;

    /**
     * API para remover la promoción para un Sku individual.
     * URL: ~/api/catalogo/productos/skus/1/promociones
     * HttpMethod: DELETE
     * HttpStatus: OK
     * @param skuId Long id del sku a remover su promoción.
     * @return ResponseEntity con mensaje éxito/error.
     */
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/productos/skus/{skuId}/promociones")
    public ResponseEntity<?> removerPromocionSku(@PathVariable Long skuId) {
        Map<String, Object> response = new HashMap<>();
        String msg;

        try {
            Sku sku = this.skuService.obtenerSku(skuId);
            this.promocionService.removerPromocionSku(sku);

            msg = "Promoción del sku: " + skuId + " removida con éxito";
        } catch (SkuException | PromocionException e) {
            response.put("mensaje", "Error al remover promoción");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", msg);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
