package com.deofis.tiendaapirest.pagos.cuentasbancarias.controllers.find;

import com.deofis.tiendaapirest.pagos.cuentasbancarias.facade.CuentaBancariaResponse;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.services.find.CuentaBancariaFinderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
@AllArgsConstructor
public class CuentaBancariaFindAllController {

    private final CuentaBancariaFinderService cuentaBancariaFinderService;

    /**
     * API para obtener el listado completo de cuentas bancarias registradas. Esta API es para usuarios
     * autenticados como administradores.
     * URL: ~/api/pagos/cuentas-bancarias
     * HttpMethod: GET
     * HttpStatus: OK
     * @return ResponseEntity con la {@link List<CuentaBancariaResponse>} con listado las cuentas.
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("/cuentas-bancarias")
    public ResponseEntity<Map<String, Object>> obtenerCuentasBancarias() {
        Map<String, Object> response = new HashMap<>();
        List<CuentaBancariaResponse> cuentas = this.cuentaBancariaFinderService.findCuentas();

        if (cuentas.size() == 0) {
            response.put("mensaje", "Error al obtener las cuentas");
            response.put("error", "No existen cuentas registradas en el sistema");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response.put("cuentas", cuentas);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
