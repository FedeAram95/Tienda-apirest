package com.deofis.tiendaapirest.pagos.cuentasbancarias.controllers.find;

import com.deofis.tiendaapirest.localizaciones.exceptions.LocalizationException;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.exceptions.CuentaBancariaException;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.facade.CuentaBancariaResponse;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.services.find.CuentaBancariaFinderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
@AllArgsConstructor
public class CuentaBancariaFindByPaisController {

    private final CuentaBancariaFinderService cuentaBancariaFinderService;

    /**
     * API para obtener una cuenta bancaria a través de un país y su id. Esta API es publica para usuarios
     * autenticados (es decir, para admins/users).
     * URL: ~/api/pagos/cuentas-bancarias/pais/1
     * HttpMethod: GET
     * HttpStatus: OK
     * @param paisId Long id del país a obtener cuenta asociada.
     * @return ResponseEntity con la {@link CuentaBancariaResponse} con datos de la cuenta.
     */
    @GetMapping("/cuentas-bancarias/pais/{paisId}")
    public ResponseEntity<Map<String, Object>> obtenerCuentaBancaria(@PathVariable Long paisId) {
        Map<String, Object> response = new HashMap<>();
        CuentaBancariaResponse cuentaResponse;

        try {
            cuentaResponse = this.cuentaBancariaFinderService.findByPais(paisId);
        } catch (LocalizationException | CuentaBancariaException e) {
            response.put("mensaje", "Error al obtener cuenta bancaria");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("cuenta", cuentaResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
