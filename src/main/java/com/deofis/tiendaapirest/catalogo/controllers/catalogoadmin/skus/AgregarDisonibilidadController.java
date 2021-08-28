package com.deofis.tiendaapirest.catalogo.controllers.catalogoadmin.skus;

import com.deofis.tiendaapirest.catalogo.skus.entities.Sku;
import com.deofis.tiendaapirest.catalogo.skus.exceptions.SkuException;
import com.deofis.tiendaapirest.catalogo.skus.services.actualizador.AgregadorDisponibilidadService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/catalogo")
@AllArgsConstructor
public class AgregarDisonibilidadController {

    private final AgregadorDisponibilidadService agregadorDisponibilidadService;

    /**
     * API para agregar una cantidad fija de disponibilidad a un sku requerido.
     * URL: ~/api/catalogo/productos/skus/1/disponibilidad/agregar
     * HttpMethod: PUT
     * HttpStatus: OK
     * @param skuId Long id del sku a agregar disponibilidad.
     * @param disponibilidad Integer cantidad de disponibilidad a sumar.
     * @return ResponseEntity con el sku actualizado.
     */
    @Secured("ROLE_ADMIN")
    @PutMapping("/productos/skus/{skuId}/disponibilidad/agregar")
    public ResponseEntity<?> agregarDisponibilidadSku(@PathVariable Long skuId,
                                                      @RequestParam Integer disponibilidad) {
        Map<String, Object> response = new HashMap<>();
        Sku skuActualizado;

        try {
            skuActualizado = this.agregadorDisponibilidadService.agregarDisponibilidad(skuId, disponibilidad);
        } catch (SkuException e) {
            response.put("mensaje", "Error al agregar disponibilidad al sku requerido");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("sku", skuActualizado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
