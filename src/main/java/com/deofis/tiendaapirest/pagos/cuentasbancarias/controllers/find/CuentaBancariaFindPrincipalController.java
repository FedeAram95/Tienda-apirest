package com.deofis.tiendaapirest.pagos.cuentasbancarias.controllers.find;

import com.deofis.tiendaapirest.pagos.cuentasbancarias.exceptions.CuentaBancariaException;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.facade.CuentaBancariaResponse;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.services.find.CuentaBancariaFinderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
@AllArgsConstructor
public class CuentaBancariaFindPrincipalController {

    private final CuentaBancariaFinderService cuentaBancariaFinderService;

    /**
     * API para obtener la cuenta bancaria principal. Esta API es publica para usuarios
     * autenticados (es decir, para admins/users).
     * URL: ~/api/pagos/cuentas-bancarias/principal
     * HttpMethod: GET
     * HttpStatus: OK
     * @return ResponseEntity con la {@link CuentaBancariaResponse} con datos de la cuenta principal.
     */
    @GetMapping("/cuentas-bancarias/principal")
    public ResponseEntity<Map<String, Object>> obtenerCuentaBancariaPrincipal() {
        Map<String, Object> response = new HashMap<>();
        CuentaBancariaResponse cuentaResponse;

        try {
            cuentaResponse = this.cuentaBancariaFinderService.findPrincipal();
        } catch (CuentaBancariaException e) {
            response.put("mensaje", "Error al obtener cuenta bancaria");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("cuenta", cuentaResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
