package com.deofis.tiendaapirest.compras.controllers.checkout;

import com.deofis.tiendaapirest.autenticacion.exceptions.AutenticacionException;
import com.deofis.tiendaapirest.compras.dto.CompraPayload;
import com.deofis.tiendaapirest.compras.services.ComprobantePagoService;
import com.deofis.tiendaapirest.imagenes.entities.Imagen;
import com.deofis.tiendaapirest.operaciones.exceptions.OperacionException;
import com.deofis.tiendaapirest.perfiles.exceptions.PerfilesException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/perfil")
@AllArgsConstructor
public class ComprobantePagoController {

    private final ComprobantePagoService comprobantePagoService;

    /**
     * API para subir la imagen del comprobante de pago, por parte del usuario, para comprobar
     * que se completó el pago de una operación por transferencia bancaria.
     * URL: ~/api/perfil/compras/1/comprobante
     * HttpMethod: POST
     * HttpStatus: CREATED
     * @param nroOperacion Long numero de operación a subir el comprobante para su pago.
     * @param comprobante {@link MultipartFile} con el archivo que contiene la imgen del comprobante.
     * @return ResponseEntity con la compra (operación) actualizada.
     */
    @PostMapping("/compras/{nroOperacion}/comprobante")
    public ResponseEntity<?> subirComprobantePago(@PathVariable Long nroOperacion,
                                                  @RequestParam(name = "comprobante")MultipartFile comprobante) {
        Map<String, Object> response = new HashMap<>();
        CompraPayload compraActualizada;

        try {
            compraActualizada = this.comprobantePagoService.subirComprobantePago(comprobante, nroOperacion);
        } catch (AutenticacionException | PerfilesException | OperacionException e) {
            response.put("mensaje", "Error al subir imagen del comprobante de pago");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("compra", compraActualizada);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * API para obtener la imagen del comprobante de pago para la compra requerida, del cliente del
     * perfil con sesión actual.
     * URL: ~/api/perfil/compras/1/comprobante
     * HttpMethod: GET
     * HttpStatus: OK
     * @param nroOperacion Long numero de operación del pago a obtener imagen de comprobante.
     * @return ResponseEntity con la imagen del comprobante.
     */
    @GetMapping("/compras/{nroOperacion}/comprobante")
    public ResponseEntity<?> obtenerImagenComprobantePago(@PathVariable Long nroOperacion) {
        Map<String, Object> response = new HashMap<>();
        Imagen comprobantePago;

        try {
            comprobantePago = this.comprobantePagoService.obtenerImagenComprobantePago(nroOperacion);
        } catch (AutenticacionException | PerfilesException | OperacionException e) {
            response.put("mensaje", "Error al obtener la imagen del comprobante de pago");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("comprobante", comprobantePago);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
