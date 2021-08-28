package com.deofis.tiendaapirest.catalogo.controllers.catalogoadmin.skus;

import com.deofis.tiendaapirest.catalogo.productos.exceptions.ProductoException;
import com.deofis.tiendaapirest.catalogo.skus.entities.Sku;
import com.deofis.tiendaapirest.catalogo.skus.services.SkuService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/catalogo")
@AllArgsConstructor
public class ActualizarSkuController {

    private final SkuService skuService;

    /**
     * Actualiza los datos de un sku.
     * URL: ~/api/catalogo/productos/skus/1
     * HttpMethod: PUT
     * HttpStatus: CREATED
     * @param sku Sku actualizado.
     * @param skuId PathVariable Long del sku a modificar.
     * @return ResponseEntity con el sku actualizado.
     */
    @Secured("ROLE_ADMIN")
    @PutMapping("/productos/skus/{skuId}")
    public ResponseEntity<?> actualizarDatosSku(@PathVariable Long skuId,
                                                @Valid @RequestBody Sku sku,
                                                BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        Sku skuActualizado;

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("error", "Bad Request");
            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            skuActualizado = this.skuService.actualizarSku(skuId, sku);
        } catch (ProductoException e) {
            response.put("mensaje", "Error al modificar sku");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("sku", skuActualizado);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Actualiza la disponibilidad de un Sku.
     * URL: ~/api/catalogo/productos/skus/1/disponibilidad
     * HttpMethod: PUT
     * HttpStatus: OK
     * @param skuId PathVariable Long id del sku.
     * @param disponibilidad RequestParam Integer disponibilidad nueva.
     * @return ResponseEntity sku actualizado.
     */
    @PutMapping("/productos/skus/{skuId}/disponibilidad")
    public ResponseEntity<?> actualizarDisponibilidadSku(@PathVariable Long skuId,
                                                         @RequestParam Integer disponibilidad) {
        Map<String, Object> response = new HashMap<>();
        Sku skuActualizado;

        try {
            skuActualizado = this.skuService.actualizarDisponibilidad(skuId, disponibilidad);
        } catch (ProductoException e) {
            response.put("mensaje", "Error al actualizar disponibilidad del sku");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("sku", skuActualizado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Actualiza el precio base de un Sku.
     * URL: ~/api/catalogo/productos/skus/1/precios/base
     * HttpMethod: PUT
     * HttpStatus: OK
     * @param skuId PathVariable Long id del sku.
     * @param precio RequestParam Double precio base nuevo.
     * @return ResponseEntity sku actualizado.
     */
    @PutMapping("/productos/skus/{skuId}/precios/base")
    public ResponseEntity<?> actualizarPrecioBaseSku(@PathVariable Long skuId,
                                                     @RequestParam Double precio) {
        Map<String, Object> response = new HashMap<>();
        Sku skuActualizado;

        try {
            skuActualizado = this.skuService.actualizarPrecio(skuId, precio);
        } catch (ProductoException e) {
            response.put("mensaje", "Error al actualizar precio base del sku");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("sku", skuActualizado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
