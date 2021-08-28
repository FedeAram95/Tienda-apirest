package com.deofis.tiendaapirest.operaciones.services;

import com.deofis.tiendaapirest.operaciones.dto.OperacionRequest;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import com.deofis.tiendaapirest.pagos.factory.OperacionPagoInfo;
import com.deofis.tiendaapirest.utils.crud.CrudService;

/**
 * Servicio que se encarga de la lógica de las {@link Operacion}es y sus transacciones: Registrar un nuevo pedido, y
 * registrar las distintas transiciones de Estados de la {@link Operacion}.
 */
public interface OperacionService extends CrudService<Operacion, Long> {

    /**
     * Registra, como usuario cliente,  una nueva operación con los datos:
     * Cliente, Productos y cantidades, fecha de operación y el estado inicial CREADO.
     * <br>
     * Crea el pago a realizar y lo devuelve con su información correspondiente.
     * @param operacion Operacion a registrar.
     * @return {@link OperacionPagoInfo} con la información del pago creado.
     */
    OperacionPagoInfo registrarNuevaOperacion(Operacion operacion);

    /**
     * Servicio que registra una nueva operación a través del "Comprar Ya", la cual
     * no es generada por un carrito, si no que a partir de un item único con un solo
     * SKU y cantidad.
     * <br>
     * En su implementación por defecto, solo se encarga de crear la {@link Operacion} y
     * registrarla a usando {@link OperacionService} registrarNuevaOperacion.
     * @param operacionRequest {@link OperacionRequest} con los datos de la nueva operación a crear.
     * @return {@link OperacionPagoInfo} con la información del pago creado.
     */
    OperacionPagoInfo registrarComprarYa(OperacionRequest operacionRequest);

    /**
     * Registra, por parte de un usuario administrador, el envio de una operación compra/venta hacia el cliente.
     * @param nroOperacion Long con el nro de operación correspondiente al envio.
     * @return Operación con su nuevo estado (SENT)
     */
    Operacion enviar(Long nroOperacion);

    /**
     * Registra que los items de una operación requerida han sido entregados. Un administrador de la tienda es quien
     * puede registrar una operación como entregada. En caso del medio de pago EFECTIVO, se completa el checkout (pago)
     * para validar que el cliente final fue a la tienda física y se retiró, completando el ciclo de la operación.
     * <br>
     * Su comportamiento esperado es: envío de email al usuario comprador, transición de estados. Estos comportamientos
     * son delegados a otros objetos:
     *  - stateMachineConfig
     *  - checkoutServiceImpl (sólo si medio pago es efectivo).
     * @param nroOperacion Long nro de operación a registrar su entrega.
     */
    Operacion entregar(Long nroOperacion);

    /**
     * Registra que el pago de una transferencia bancaria ha sido confirmado. Solo
     * será llamado para operaciones con el medio de pago TRANSFERENCIA_BANCARIA, para
     * confirmar, como administrador, que el pago de una operación se completó.
     * <br>
     * Comportamiento esperado: transitar al estado PAGO_CONFIRMADO, activando el método
     * confirmarPagoTransferencia() que completa el pago y realiza envíos de email correspondientes.
     * @param nroOperacion Long numero de operación a confirmar el pago.
     * @return {@link Operacion} con pago confirmado.
     */
    Operacion confirmarPago(Long nroOperacion);

    /**
     * Registra que el comprobante de pago para el pago de una operación por transferencia bancaria
     * ha sido rechazado por un administrador, transitando de vuelta al estado EN RESERVA, debiendo
     * enviar email de aviso al usuario para informarlo.
     * @param nroOperacion Long numero de operación a rechazar el comprobante de su pago.
     * @return {@link Operacion} con comprobante rechazado (de vuelta EN RESERVA).
     */
    Operacion rechazarComprobantePago(Long nroOperacion);

    /**
     * Registra la cancelación de una operación requerida.
     * @param nroOperacion Long nro de operacion a cancelar.
     * @return {@link Operacion} cancelada.
     */
    Operacion cancelar(Long nroOperacion);

    /**
     * Registra que una operación, por decisión de un administrador o por el paso del tiempo, ha sido anulada. Este
     * proceso solo puede darse en operaciones con medio de pago EFECTIVO, donde se descuenta stock pero aun no se recibe
     * el pago (hasta que el cliente final vaya a la tienda física).
     * <br>
     * Su comportamiento esperado es: envío de emails, reintegro de disponibilidad, transición de estado. Este comportamiento
     * es delegado a otros objetos: pagoStrategy y stateMachineConfig.
     * @param nroOperacion Long nro de operación a anular.
     * @return {@link Operacion} abortada.
     */
    Operacion anular(Long nroOperacion);

    /**
     * Registra que el flujo del proceso de devolución de una operación se a completado. El administrador de la tienda
     * es quien marca este estado.
     * <br>
     * Cuando el administrador marca registrada la devolución se debe reintegrar el stock (disponibilidad) de todos los
     * skus que formaban parte de los items de la operación devuelta.
     * @param nroOperacion Long nro de operación que se registra como devuelta.
     * @return {@link Operacion} con devolución completada.
     */
    Operacion registrarDevolucion(Long nroOperacion);

    /**
     * Obtiene una {@link Operacion} a través de su número de operación.
     * @param nroOperacion Long nro de operación.
     * @return Operacion.
     */
    Operacion findById(Long nroOperacion);
}
