package com.deofis.tiendaapirest.pagos.cuentasbancarias.controllers.actualizar;

import com.deofis.tiendaapirest.pagos.cuentasbancarias.exceptions.CuentaBancariaException;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.facade.CuentaBancariaRequest;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.facade.CuentaBancariaResponse;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.facade.CuentaBancariaUpdateRequest;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.services.actualizar.CuentaBancariaActualizadorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pagos")
@AllArgsConstructor
public class CuentaBancariaActualizarDatosController {

    private final CuentaBancariaActualizadorService actualizadorService;

    /**
     * API para actualizar los datos de una cuenta bancaria existente. Recurso solo para usuarios admins.
     * URL: ~/api/pagos/cuentas-bancarias/1
     * HttpMethod: PUT
     * HttpStatus: OK
     * @param cuentaId Long id de la cuenta a actualizar sus datos.
     * @param cuentaBancariaRequest {@link CuentaBancariaRequest} con los datos a actualizar.
     * @return ResponseEntity con {@link CuentaBancariaResponse} con sus datos actualizados.
     */
    @Secured("ROLE_ADMIN")
    @PutMapping("/cuentas-bancarias/{cuentaId}")
    public ResponseEntity<Map<String, Object>> actualizarDatosCuentaBancaria(@PathVariable Long cuentaId,
                                                                             @Valid @RequestBody CuentaBancariaUpdateRequest cuentaBancariaRequest,
                                                                             BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        CuentaBancariaResponse cuentaResponse;

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("error", "Bad Request");
            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            cuentaResponse = this.actualizadorService.actualizarDatos(cuentaId, cuentaBancariaRequest);
        } catch (CuentaBancariaException e) {
            response.put("mensaje", "Error al actualizar datos de la cuenta bancaria");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("cuenta", cuentaResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
