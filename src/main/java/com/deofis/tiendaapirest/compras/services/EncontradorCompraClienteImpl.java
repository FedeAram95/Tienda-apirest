package com.deofis.tiendaapirest.compras.services;

import com.deofis.tiendaapirest.clientes.entities.Cliente;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import com.deofis.tiendaapirest.operaciones.exceptions.OperacionException;
import com.deofis.tiendaapirest.operaciones.repositories.OperacionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class EncontradorCompraClienteImpl implements EncontradorCompraCliente {

    private final OperacionRepository operacionRepository;

    @Transactional(readOnly = true)
    @Override
    public Operacion encontrarCompraCliente(Long nroOperacion, Cliente cliente) throws OperacionException {
        return this.operacionRepository.findByNroOperacionAndCliente(nroOperacion, cliente)
                .orElseThrow(() -> new OperacionException("No existe la operación requerida para el cliente " +
                        "seleccionado"));
    }
}
