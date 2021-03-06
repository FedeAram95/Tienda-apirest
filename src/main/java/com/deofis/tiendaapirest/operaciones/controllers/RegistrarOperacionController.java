package com.deofis.tiendaapirest.operaciones.controllers;

import com.deofis.tiendaapirest.autenticacion.exceptions.AutenticacionException;
import com.deofis.tiendaapirest.catalogo.productos.exceptions.ProductoException;
import com.deofis.tiendaapirest.catalogo.skus.exceptions.SkuException;
import com.deofis.tiendaapirest.operaciones.dto.OperacionRequest;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import com.deofis.tiendaapirest.operaciones.exceptions.OperacionException;
import com.deofis.tiendaapirest.operaciones.services.OperacionService;
import com.deofis.tiendaapirest.pagos.factory.OperacionPagoInfo;
import com.deofis.tiendaapirest.perfiles.exceptions.PerfilesException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class RegistrarOperacionController {

    private final OperacionService operacionService;

    /**
     * Registra una nueva operación de compra/venta para el cliente que se pasa como parte de la
     * operación en sí.
     * URL: ~/api/operaciones/nueva
     * HttpMethod: POST
     * HttpStatus: CREATED
     * @param operacion RequestBody con la operacion a registrar.
     * @return ResponseEntity con la operacion registrada y OrderPayload con los datos que vienen en
     *                          respuesta a la API de PayPal (incluyendo la URL para aprobar el pago).
     */
    @PostMapping("/operaciones/nueva")
    public ResponseEntity<?> registrarOperacion(@Valid @RequestBody Operacion operacion) {
        Map<String, Object> response = new HashMap<>();
        OperacionPagoInfo pagoInfo;

        try {
            pagoInfo = this.operacionService.registrarNuevaOperacion(operacion);
        } catch (OperacionException | ProductoException | PerfilesException | AutenticacionException | SkuException e) {
            response.put("mensaje", "Error al registrar la nueva compra");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("pago", pagoInfo);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Registra una nueva operación de compra/venta con la funcionalidad de Comprar Ya,
     * lo que no requiere el paso previo de agregar items al carrito.
     * URL: ~/api/operaciones/comprar/ya
     * HttpMethod: POST
     * HttpStatus: CREATED
     * @param operacionRequest {@link OperacionRequest} con los datos de la compra.
     * @return ResponseEntity con el {@link OperacionPagoInfo} creado con la info del pago.
     */
    @PostMapping("/operaciones/comprar/ya")
    public ResponseEntity<?> comprarYa(@RequestBody OperacionRequest operacionRequest) {
        Map<String, Object> response = new HashMap<>();
        OperacionPagoInfo pagoInfo;

        try {
            pagoInfo = this.operacionService.registrarComprarYa(operacionRequest);
        } catch (OperacionException | ProductoException | PerfilesException | AutenticacionException | SkuException e) {
            response.put("mensaje", "Error al registrar la nueva compra");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("pago", pagoInfo);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
