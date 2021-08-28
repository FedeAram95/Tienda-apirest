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

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class RegistrarPagoTransferenciaController {

    private final OperacionService operacionService;

    /**
     * API para confirmar pagos por transferencia bancaria, del lado del administrador
     * de la tienda.
     * URL: ~/api/operaciones/confirmar/pago
     * HttpMethod: POST
     * HttpStatus: OK
     * @param nroOperacion Long numero de operación a registrar la confirmación del pago
     *                     por transferencia.
     * @return ResponseEntity con la Operacion y su nuevo estado.
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("/operaciones/confirmar/pago")
    public ResponseEntity<?> registrarConfirmacionPago(@RequestParam Long nroOperacion) {
        Map<String, Object> response = new HashMap<>();
        Operacion operacionPagoConfirmado;

        try {
            operacionPagoConfirmado = this.operacionService.confirmarPago(nroOperacion);
        } catch (OperacionException e) {
            response.put("mensaje", "Error al registrar confirmación de pago para operación");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("operacion", operacionPagoConfirmado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
