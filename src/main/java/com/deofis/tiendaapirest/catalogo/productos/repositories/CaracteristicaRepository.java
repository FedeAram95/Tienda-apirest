package com.deofis.tiendaapirest.catalogo.productos.repositories;

import com.deofis.tiendaapirest.catalogo.productos.entities.Caracteristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaracteristicaRepository extends JpaRepository<Caracteristica, Long> {

}
