package com.deofis.tiendaapirest.localizaciones.repositories;

import com.deofis.tiendaapirest.localizaciones.entities.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CiudadRepository extends JpaRepository<Ciudad, Long> {

}
