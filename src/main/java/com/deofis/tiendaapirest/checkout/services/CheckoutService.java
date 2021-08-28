package com.deofis.tiendaapirest.checkout.services;

import com.deofis.tiendaapirest.checkout.dto.CheckoutPayload;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import com.deofis.tiendaapirest.pagos.factory.OperacionPagoInfo;

/**
 * Servicio que se encarga de ejecutar el checkout de una operación, completando el pago
 * de la misma.
 */
public interface CheckoutService {

    /**
     * Este servicio inicia el flujo del Checkout, obteniendo una estrategia para el pago
     * y creando el mismo, de acuerdo al medio de pago de la operación.
     * <br>
     * Una vez creado el pago, a la operación se le asigna un objeto de OperacionPago
     * con la información del pago pendiente con el link para aprobar el pago: approveUrl.
     * @param operacion {@link Operacion} a iniciar su checkout y crear el pago.
     * @return {@link OperacionPagoInfo} con los datos del pago PENDIENTE DE PAGO.
     */
    OperacionPagoInfo iniciarCheckout(Operacion operacion);

    /**
     * Completa el pago para una operación, lo que implica hacer una petición a la API de pago
     * correspondiente para obtener los datos y completar el pago.
     * <br>
     * Una vez completado el pago, se le asigna a la operación el objeto OperacionPago con
     * la información del pago completado, y se debe transitar al estado de operación
     * correspondiente.
     * @param checkoutPayload {@link CheckoutPayload} con el nroOperacion y el paymentId, que será
     *                                               distinto según medio de pago.
     * @return {@link OperacionPagoInfo} con los datos del pago COMPLETADO/APROBADO.
     */
    OperacionPagoInfo ejecutarCheckoutSuccess(CheckoutPayload checkoutPayload);

    /**
     * Ejecuta las transiciones de estado cuando el pago de una operación es rechazado.
     * <br>
     * Se espera que una operación con pago rechazado transite al estado PAGO_PENDIENTE
     * @param checkoutPayload objeto que tiene los datos que envía la entidad de pagos.
     */
    void ejecutarCheckoutFailure(CheckoutPayload checkoutPayload);
}
