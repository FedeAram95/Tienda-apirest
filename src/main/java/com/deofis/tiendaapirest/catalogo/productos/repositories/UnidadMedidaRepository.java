package com.deofis.tiendaapirest.catalogo.productos.repositories;

import com.deofis.tiendaapirest.catalogo.productos.entities.UnidadMedida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnidadMedidaRepository extends JpaRepository<UnidadMedida, Long> {

    Optional<UnidadMedida> findByNombre(String nombre);
}
