package com.deofis.tiendaapirest.pagos.cuentasbancarias.controllers.find;

import com.deofis.tiendaapirest.localizaciones.exceptions.LocalizationException;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.dto.PaisDto;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.services.paises.PaisesUsadosFinder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
@AllArgsConstructor
public class PaisesEnCuentasFinderController {

    private final PaisesUsadosFinder paisesUsadosFinder;

    /**
     * API para obtener paises usados para las cuentas bancarias registradas en el sistema.
     * URL: ~/api/pagos/cuentas-bancarias/paises
     * HttpMethod: GET
     * HttpStatus: OK
     * @return ResponseEntity con listado de paises usados en cuentas bancarias.
     */
    @GetMapping("/cuentas-bancarias/paises")
    public ResponseEntity<Map<String, Object>> obtenerPaisesDeCuentas() {
        Map<String, Object> response = new HashMap<>();
        List<PaisDto> paisesEnCuentas;

        try {
            paisesEnCuentas = this.paisesUsadosFinder.findPaisesEnCuentas();
        } catch (LocalizationException e) {
            response.put("mensaje", "Error al obtener listado de países usados");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("paises", paisesEnCuentas);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
