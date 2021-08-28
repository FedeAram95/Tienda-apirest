package com.deofis.tiendaapirest.operaciones.services.anulador;

import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import com.deofis.tiendaapirest.utils.scheduled.Programable;

/**
 * Servicio que se encarga de anular las operaciones en el estado EN_ENTREGA, cuando el medio
 * de pago es EFECTIVO y expiró el pago. El objeto pago de operación guarda una referencia sobre
 * la expiración de dicho pago. Cumplido ese tiempo, la operación transita automáticamente al estado ANULADO.
 * <br>
 * Este servicio gestiona los pagos en estado EN_ENTREGA, validando así su expiración, y, en caso
 * de sobrepasado el tiempo establecido para la tienda implementada, la operación se anula.
 */
public interface OperacionAnuladorAutomatico extends Programable {

    /**
     * Valida si el pago (siempre y cuando sea efectivo) de una operación ha expirado.
     * @param operacion {@link Operacion} a validar expiración de su pago en efectivo.
     * @return boolean.
     */
    boolean expired(Operacion operacion);

    /**
     * Se encarga de transitar al estado ANULADO para aquellas operaciones requeridas.
     * @param nroOperacion Long nro de la operación a anular.
     */
    void anularOperacion(Long nroOperacion);
}
