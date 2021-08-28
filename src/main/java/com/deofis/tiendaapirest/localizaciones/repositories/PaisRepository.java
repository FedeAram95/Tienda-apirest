package com.deofis.tiendaapirest.localizaciones.repositories;

import com.deofis.tiendaapirest.localizaciones.entities.Pais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaisRepository extends JpaRepository<Pais, Long> {

    Optional<Pais> findByNombre(String pais);

    Optional<Pais> findByIso2(String iso2);

    List<Pais> findAllByOrderByNombreAsc();

}
