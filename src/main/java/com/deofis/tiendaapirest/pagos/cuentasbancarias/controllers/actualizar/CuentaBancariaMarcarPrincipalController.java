package com.deofis.tiendaapirest.pagos.cuentasbancarias.controllers.actualizar;

import com.deofis.tiendaapirest.pagos.cuentasbancarias.exceptions.CuentaBancariaException;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.services.actualizar.CuentaBancariaMarcadorPrincipalService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
@AllArgsConstructor
public class CuentaBancariaMarcarPrincipalController {

    private final CuentaBancariaMarcadorPrincipalService marcadorPrincipalService;

    /**
     * API para marcar una cuenta bancaria como principal, y desmarcar la principal actual.
     * URL: ~/api/pagos/cuentas-bancarias/1
     * HttpMethod: PUT
     * HttpStatus: OK
     * @param cuentaId Long id de la cuenta a marcar como principal.
     * @return ResponseEntity con mensaje de éxito.
     */
    @Secured("ROLE_ADMIN")
    @PutMapping("/cuentas-bancarias/{cuentaId}/principal")
    public ResponseEntity<Map<String, Object>> marcarCuentaBancariaPrincipal(@PathVariable Long cuentaId) {
        Map<String, Object> response = new HashMap<>();
        String msg;

        try {
            this.marcadorPrincipalService.marcarCuentaPrincipal(cuentaId);
            msg = "Cuenta Bancaria con id : "+ cuentaId + " marcada como principal";
        } catch (CuentaBancariaException e) {
            response.put("mensaje", "Error al marcar cuenta bancaria como principal");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", msg);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
