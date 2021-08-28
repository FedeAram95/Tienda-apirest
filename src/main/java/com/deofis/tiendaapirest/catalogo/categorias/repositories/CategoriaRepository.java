package com.deofis.tiendaapirest.catalogo.categorias.repositories;

import com.deofis.tiendaapirest.catalogo.categorias.entities.Categoria;
import com.deofis.tiendaapirest.catalogo.categorias.entities.Subcategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findAllByOrderByNombreAsc();

    Optional<Categoria> findByNombre(String nombre);

    Optional<Categoria> findBySubcategoriasContaining(Subcategoria subcategoria);
}
