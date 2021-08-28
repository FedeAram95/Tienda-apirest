package com.deofis.tiendaapirest.imagenes.repositories;

import com.deofis.tiendaapirest.imagenes.entities.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen, Long> {
    Optional<Imagen> findByPath(String path);
}
