package com.deofis.tiendaapirest.compras.controllers.cancel;

import com.deofis.tiendaapirest.autenticacion.exceptions.AutenticacionException;
import com.deofis.tiendaapirest.compras.services.AnularCompraService;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import com.deofis.tiendaapirest.operaciones.exceptions.OperacionException;
import com.deofis.tiendaapirest.perfiles.exceptions.PerfilesException;
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
public class AnularCompraController {

    private final AnularCompraService anularCompraService;

    /**
     * API para anular, por parte del usuario comprador, una compra (operacion) requerida,
     * a través del número de operación de la misma.
     * URL: ~/api/perfil/compras/1/anular
     * HttpMethod: POST
     * HttpStatus: OK
     * @param nroOperacion Long número de operación a anular.
     * @return ResponseEntity con la operación en su nuevo estado.
     */
    @PostMapping("/compras/{nroOperacion}/anular")
    public ResponseEntity<?> anularCompra(@PathVariable Long nroOperacion) {
        Map<String, Object> response = new HashMap<>();
        Operacion operacionAnulada;

        try {
            operacionAnulada = this.anularCompraService.anular(nroOperacion);
        } catch (AutenticacionException | PerfilesException | OperacionException e) {
            response.put("mensaje", "Error al anular compra");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("operacion", operacionAnulada);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
