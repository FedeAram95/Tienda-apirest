package com.deofis.tiendaapirest.catalogo.categorias.repositories;

import com.deofis.tiendaapirest.catalogo.categorias.entities.Subcategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubcategoriaRepository extends JpaRepository<Subcategoria, Long> {

    List<Subcategoria> findAllByNombreContainingIgnoringCase(String nombre);
}
