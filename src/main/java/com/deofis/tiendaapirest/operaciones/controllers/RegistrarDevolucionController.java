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
 * API utilizada para, como administrador de la tienda, poder registrar que se ha completado
 * la devolución de los items de una operación requerida.
 */
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class RegistrarDevolucionController {

    private final OperacionService operacionService;

    /**
     * API para registrar que una devolución de operación ha sido completada por un administrador.
     * URL: ~/api/operaciones/devolucion/registrar
     * HttpMethod: POST
     * HttpStatus: OK
     * @param nroOperacion Long nro de operación ha registrar como devuelta.
     * @return ResponseEntity con la operación en su nuevo estado.
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("/operaciones/devolucion/registrar")
    public ResponseEntity<?> registrarDevolucion(@RequestParam Long nroOperacion) {
        Map<String, Object> response = new HashMap<>();
        Operacion operacionDevuelta;

        try {
            operacionDevuelta = this.operacionService.registrarDevolucion(nroOperacion);
        } catch (OperacionException e) {
            response.put("mensaje", "Error al registrar devolución de operación");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("operacion", operacionDevuelta);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
