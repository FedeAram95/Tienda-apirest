package com.deofis.tiendaapirest.compras.services;

import com.deofis.tiendaapirest.operaciones.entities.Operacion;

/**
 * Servicio para la devolución de los items (skus) de una compra, relacionado
 * a un usuario cliente.
 */
public interface DevolverCompraService {

    /**
     * Registra el inicio del proceso de de devolución de los items de productos que pertenecen a una operación.
     * Esta acción es iniciada por el usuario comprador (cliente), y debe ser tratada por un administrador para acordar
     * el reembolso del dinero, y como recibir los productos por parte de la tienda.
     * <br>
     * NO Debe reponer la disponibilidad de los items que se devuelven aun.
     * @param nroOperacion Long nro de operación a devolver.
     * @return {@link Operacion} devuelta.
     */
    Operacion devolver(Long nroOperacion);

}
