package com.deofis.tiendaapirest.compras.services;

import com.deofis.tiendaapirest.clientes.entities.Cliente;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import com.deofis.tiendaapirest.operaciones.exceptions.OperacionException;

/**
 * Encuentra una {@link Operacion} (compra) para un {@link Cliente} requerido.
 */
public interface EncontradorCompraCliente {

    /**
     * Método que se encarga de cumplir la responsabilidad de encontrar una operación
     * para un cliente puntual.
     * @param nroOperacion Long numero de operación a encontrar.
     * @param cliente {@link Cliente} a quien debe pertenecer la operación.
     * @return {@link Operacion} del cliente requerido.
     */
    Operacion encontrarCompraCliente(Long nroOperacion, Cliente cliente) throws OperacionException;

}
