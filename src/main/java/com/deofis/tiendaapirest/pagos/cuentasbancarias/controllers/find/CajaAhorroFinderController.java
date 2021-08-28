package com.deofis.tiendaapirest.pagos.cuentasbancarias.controllers.find;

import com.deofis.tiendaapirest.pagos.cuentasbancarias.services.find.CajaAhorroFinderService;
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
public class CajaAhorroFinderController {

    private final CajaAhorroFinderService finderService;

    /**
     * API para listar todas los tipos de caja de ahorro (currencies) disponibles.
     * URL: ~/api/pagos/cuentas-bancarias/ca
     * HttpMethod: GET
     * HttpStatus: OK
     * @return ResponseEntity con listado de cajas ahorro.
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("/cuentas-bancarias/ca")
    public ResponseEntity<Map<String, Object>> listarCajasAhorro() {
        Map<String, Object> response = new HashMap<>();
        response.put("ca", this.finderService.listarCajasAhorro());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
