package com.deofis.tiendaapirest.pagos.services;

import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import com.deofis.tiendaapirest.pagos.entities.OperacionPago;

/**
 * Clase que se encarga de manejar los pagos incompletos: valida si el mismo ha expirado, en cuyo caso, crea
 * un nuevo pago para completar una operación.
 */
public interface PagoHandler {

    /**
     * Procesa un {@link OperacionPago} de una operación, validando su expiración previo a devolverlo
     * (o creando uno nuevo en caso de haber expirado).
     * @param operacion {@link Operacion} a procesar su pago.
     * @return {@link OperacionPago} manejado.
     */
    OperacionPago procesarPago(Operacion operacion);
}
