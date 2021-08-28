package com.deofis.tiendaapirest.pagos.cuentasbancarias.controllers.find;

import com.deofis.tiendaapirest.pagos.cuentasbancarias.services.find.TipoCuentaFinderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
@AllArgsConstructor
public class TipoCuentaBancariaFinderController {

    private final TipoCuentaFinderService finderService;

    /**
     * API para listar todos los tipos de cuenta bancaria.
     * URL: ~/api/pagos/cuentas-bancarias/tipos
     * HttpMethod: GET
     * HttpStatus: OK
     * @return ResponseEntity con listado de tipos de cuenta bancaria.
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("/cuentas-bancarias/tipos")
    public ResponseEntity<Map<String, Object>> listarTiposCuentaBancaria() {
        Map<String, Object> response = new HashMap<>();
        response.put("tipos", this.finderService.listarTiposCuenta());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
