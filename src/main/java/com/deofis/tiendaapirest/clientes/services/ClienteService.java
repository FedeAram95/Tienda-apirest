package com.deofis.tiendaapirest.clientes.services;

import com.deofis.tiendaapirest.clientes.entities.Cliente;
import com.deofis.tiendaapirest.clientes.entities.Direccion;
import com.deofis.tiendaapirest.utils.crud.CrudService;

public interface ClienteService extends CrudService<Cliente, Long> {

    /**
     * Obtiene el cliente requerido.
     * @param id Long id del cliente.
     * @return {@link Cliente}.
     */
    Cliente obtenerCliente(Long id);

    /**
     * Crea un nuevo cliente y lo guarda en la base de datos.
     * @param cliente Cliente a crear.
     * @return {@link Cliente} creado y guardado.
     */
    Cliente crear(Cliente cliente);

    /**
     * Actualiza los datos de un cliente existente.
     * @param clienteActualizado Cliente con los datos actualizados.
     * @param clienteId Long id del cliente guardado a actualizar.
     * @return {@link Cliente} con los datos actualizados.
     */
    Cliente actualizar(Cliente clienteActualizado, Long clienteId);

    /**
     * Este servicio actualiza la dirección de un cliente requerido a través de su id.
     * @param cliente {@link Cliente} cliente a actualizar su dirección.
     * @param direccion {@link Direccion} nueva que tendrá el cliente.
     * @return {@link Cliente} cliente con la dirección actualizada.
     */
    Cliente actualizarDireccion(Cliente cliente, Direccion direccion);
}
