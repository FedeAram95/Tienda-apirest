package com.deofis.tiendaapirest.carrito.repositories;

import com.deofis.tiendaapirest.carrito.entities.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {

}
