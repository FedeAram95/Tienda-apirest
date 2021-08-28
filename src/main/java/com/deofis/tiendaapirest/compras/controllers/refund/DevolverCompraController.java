package com.deofis.tiendaapirest.compras.controllers.refund;

import com.deofis.tiendaapirest.autenticacion.exceptions.AutenticacionException;
import com.deofis.tiendaapirest.compras.services.DevolverCompraService;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import com.deofis.tiendaapirest.operaciones.exceptions.OperacionException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/perfil")
@AllArgsConstructor
public class DevolverCompraController {

    private final DevolverCompraService devolverCompraService;

    /**
     * API para que el usuario comprador pueda devolver los items de una operación, en caso de requerirlo.
     * URL: ~/api/perfil/compras/1/devolver
     * HttpMethod: POST
     * HttpStatus: OK
     * @param nroOperacion Long nro de operación de la compra a devolver sus items.
     * @return ResponseEntity con la operación en su nuevo estado.
     */
    @PostMapping("/compras/{nroOperacion}/devolver")
    public ResponseEntity<?> devolverCompra(@PathVariable Long nroOperacion) {
        Map<String, Object> response = new HashMap<>();
        Operacion operacionADevolver;

        try {
            operacionADevolver = this.devolverCompraService.devolver(nroOperacion);
        } catch (AutenticacionException | OperacionException e) {
            response.put("mensaje", "Error al intentar devolver operación");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("operacion", operacionADevolver);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
