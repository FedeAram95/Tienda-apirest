package com.deofis.tiendaapirest.pagos.controllers;

import com.deofis.tiendaapirest.pagos.entities.MedioPago;
import com.deofis.tiendaapirest.pagos.exceptions.MedioPagoException;
import com.deofis.tiendaapirest.pagos.services.MedioPagoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ObtenerMediosPagoController {

    private final MedioPagoService medioPagoService;

    /**
     * Lista todos los medios de pago disponibles en el sistema.
     * URL: ~/api/medios-pago
     * HttpMethod: GET
     * HttpStatus: OK
     * @return ResponseEntity listado de medios de pago.
     */
    @GetMapping("/medios-pago")
    public ResponseEntity<?> obtenerMediosPago() {
        Map<String, Object> response = new HashMap<>();
        List<MedioPago> mediosDePago;

        try {
            mediosDePago = this.medioPagoService.findAll();
        } catch (MedioPagoException e) {
            response.put("mensaje","Error al obtener los medios de pago");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mediosPago", mediosDePago);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Obtiene un medio de pago requerido a través de su ID.
     * URL: ~/api/medios-pago/1
     * HttpMethod: GET
     * HttpStatus: OK
     * @param medioPagoId PathVariable Long id del medio de pago.
     * @return ResponseEntity con el medio de pago.
     */
    @GetMapping("/medios-pago/{medioPagoId}")
    public ResponseEntity<?> obtenerMedioPago(@PathVariable Long medioPagoId) {
        Map<String, Object> response = new HashMap<>();
        MedioPago medioPago;

        try {
            medioPago = this.medioPagoService.findById(medioPagoId);
        } catch (MedioPagoException e) {
            response.put("mensaje","Error al obtener el medio de pago");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("medioPago", medioPago);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
