package com.deofis.tiendaapirest.ventas.controllers;

import com.deofis.tiendaapirest.imagenes.entities.Imagen;
import com.deofis.tiendaapirest.operaciones.exceptions.OperacionException;
import com.deofis.tiendaapirest.ventas.dto.VentaPayload;
import com.deofis.tiendaapirest.ventas.services.VentaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API para listar las ventas registradas en el sistema.
 */
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class VentasController {

    private final VentaService ventaService;

    /**
     * API para obtener todos las ventas registradas en el sistema al momento.
     * URL: ~/api/ventas
     * HttpMethod: GET
     * HttpStatus: OK
     * @return ResponseEntity con el listado completo de ventas.
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("/ventas")
    public ResponseEntity<?> listarVentas() {
        Map<String, Object> response = new HashMap<>();
        List<VentaPayload> ventas;

        try {
            ventas = this.ventaService.listarVentas();
        } catch (OperacionException e) {
            response.put("mensaje", "Error al obtener listado de ventas");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        response.put("ventas", ventas);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * API para obtener una venta en particular, a través del número de operación.
     * URL: ~/api/ventas/1
     * HttpMethod: GET
     * HttpStatus: OK
     * @param nroOperacion Long número de operación de la venta a obtener.
     * @return ResponseEntity con la venta.
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("/ventas/{nroOperacion}")
    public ResponseEntity<?> obtenerVenta(@PathVariable Long nroOperacion) {
        Map<String, Object> response = new HashMap<>();
        VentaPayload venta;

        try {
            venta = this.ventaService.obtenerVenta(nroOperacion);
        } catch (OperacionException e) {
            response.put("mensaje", "Error al obtener la venta");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        response.put("venta", venta);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * API para obtener, como administrador, el comprobante de pago para una operación requerida.
     * URL: ~/api/ventas/1/comprobante
     * HttpMethod: GET
     * HttpStatus: OK
     * @param nroOperacion Long numero de operación a obtener el comprobante de su pago.
     * @return ResponseEntity con el comprobante de pago.
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("/ventas/{nroOperacion}/comprobante")
    public ResponseEntity<?> obtenerComprobantePagoVenta(@PathVariable Long nroOperacion) {
        Map<String, Object> response = new HashMap<>();
        Imagen comprobantePago;

        try {
            comprobantePago = this.ventaService.obtenerComprobantePagoVenta(nroOperacion);
        } catch (OperacionException e) {
            response.put("mensaje", "Error al obtener el comprobante de pago");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("comprobante", comprobantePago);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
