package com.deofis.tiendaapirest.operaciones.controllers;

import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import com.deofis.tiendaapirest.operaciones.exceptions.OperacionException;
import com.deofis.tiendaapirest.operaciones.services.OperacionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * API para registrar la anulación de una operación por parte del administrador.
 */
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class RegistrarAnulacionController {

    private final OperacionService operacionService;

    /**
     * API para anular una operación como administrador.
     * URL: ~/api/operaciones/anular
     * HttpMethod: POST
     * HttpStatus: OK
     * @param nroOperacion Long nro de operación a anular.
     * @return ResponseEntity con la operación y su nuevo estado.
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("/operaciones/anular")
    public ResponseEntity<?> registrarAnulacion(@RequestParam Long nroOperacion) {
        Map<String, Object> response = new HashMap<>();
        Operacion operacionAnulada;

        try {
            operacionAnulada = this.operacionService.anular(nroOperacion);
        } catch (OperacionException e) {
            response.put("mensaje", "Error al anular operación");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("operacion", operacionAnulada);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
