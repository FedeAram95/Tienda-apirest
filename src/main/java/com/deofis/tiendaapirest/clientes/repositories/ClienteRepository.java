package com.deofis.tiendaapirest.clientes.repositories;

import com.deofis.tiendaapirest.clientes.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
