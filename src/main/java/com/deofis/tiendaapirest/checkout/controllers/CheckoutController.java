package com.deofis.tiendaapirest.checkout.controllers;

import com.deofis.tiendaapirest.checkout.dto.CheckoutPayload;
import com.deofis.tiendaapirest.checkout.services.CheckoutService;
import com.deofis.tiendaapirest.operaciones.exceptions.OperacionException;
import com.deofis.tiendaapirest.pagos.exceptions.PaymentException;
import com.deofis.tiendaapirest.pagos.factory.OperacionPagoInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * API's que van a ser accedidas por API's externas de pago, para completar un pago o
 * rechazarlo.
 */
@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Slf4j
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final String clientUrl;

    /**
     * Completa el pago (checkout) para una operación requerida, cuando el pago ha sido
     * APROBADO.
     * <br>
     * Esta API solo debe ser accedida por API's externas que manejan el pago para el
     * sistema de pagos de nuestra aplicación.
     * URL: ~/api/checkout/success
     * HttpMethod: POST
     * HttpStatus: CREATED
     * @param nroOperacion long con el número de operación.
     * @return ResponseEntity con la información del pago completado.
     */
    @GetMapping("/checkout/success")
    public ResponseEntity<?> ejecutarCheckoutSuccess(HttpServletResponse httpResponse,
                                                     @RequestParam("nroOperacion") Long nroOperacion,
                                                     @RequestParam(value = "payment_id", required = false) String paymentId,
                                                     @RequestParam(value = "token", required = false) String token,
                                                     @RequestParam(value = "preference_id", required = false) String preferenceId) {
        Map<String, Object> response = new HashMap<>();
        OperacionPagoInfo pagoInfo;

        CheckoutPayload checkoutPayload;
        if (paymentId != null) {
            checkoutPayload = CheckoutPayload.builder()
                    .nroOperacion(nroOperacion)
                    .paymentId(paymentId)
                    .preferenceId(preferenceId).build();
        } else if (token != null){
            checkoutPayload = CheckoutPayload.builder()
                    .nroOperacion(nroOperacion)
                    .paymentId(token).build();
        } else checkoutPayload = null;

        log.info(String.valueOf(checkoutPayload));

        try {
            // TODO: cambiar redirect dependiendo medio pago...
            pagoInfo = this.checkoutService.ejecutarCheckoutSuccess(checkoutPayload);
            httpResponse.sendRedirect(clientUrl.concat("/payment/redirect/approved"));
        } catch (OperacionException | PaymentException | IOException e) {
            response.put("mensaje", "Error al completar el pago para la operación");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (pagoInfo == null) {
            response.put("mensaje", "Error al completar el pago para la operación");
            response.put("error", "El pago debe ser aprobado por el cliente antes de completarlo");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("pago", pagoInfo);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Procesa el pago (checkout) para una operación requerida cuando el pago ha sido
     * RECHAZADO.
     * <br>
     * Esta API solo debe ser accedida por API's externas que manejan el pago para el
     * sistema de pagos de nuestra aplicación.
     * URL: ~/api/checkout/failure
     * HttpMethod: GET
     * HttpStatus: OK
     * @param nroOperacion long con el numero de operación.
     * @return ResponseEntity con mensaje del pago rechazado.
     */
    @GetMapping("/checkout/failure")
    public ResponseEntity<?> ejecutarCheckoutFailure(HttpServletResponse httpResponse,
                                                     @RequestParam("nroOperacion") Long nroOperacion,
                                                     @RequestParam(value = "payment_id", required = false) String paymentId,
                                                     @RequestParam(value = "token", required = false) String token,
                                                     @RequestParam(value = "preference_id", required = false) String preferenceId) {
        Map<String, Object> response = new HashMap<>();
        String msg;

        CheckoutPayload checkoutPayload;
        if (paymentId != null) {
            checkoutPayload = CheckoutPayload.builder()
                    .nroOperacion(nroOperacion)
                    .paymentId(paymentId)
                    .preferenceId(preferenceId).build();
        } else if (token != null){
            checkoutPayload = CheckoutPayload.builder()
                    .nroOperacion(nroOperacion)
                    .paymentId(token).build();
        } else checkoutPayload = null;

        log.info(String.valueOf(checkoutPayload));

        try {
            this.checkoutService.ejecutarCheckoutFailure(checkoutPayload);
            msg = "Error al completar checkout";
            httpResponse.sendRedirect(clientUrl.concat("/payment/redirect/cancel"));
        } catch (OperacionException | PaymentException | IOException e) {
            response.put("mensaje", "Error al procesar el pago");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", msg);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
