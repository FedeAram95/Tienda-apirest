package com.deofis.tiendaapirest.catalogo.productos.repositories;

import com.deofis.tiendaapirest.catalogo.categorias.entities.Subcategoria;
import com.deofis.tiendaapirest.catalogo.productos.entities.Marca;
import com.deofis.tiendaapirest.catalogo.productos.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findAllByOrderByNombreAsc();

    List<Producto> findAllByDestacadoIsTrueAndActivoIsTrue();

    List<Producto> findAllByNombreContainingIgnoringCaseAndActivoIsTrueOrderByNombreAsc(String termino);

    List<Producto> findAllBySubcategoriaAndActivoIsTrue(Subcategoria subcategoria);

    List<Producto> findAllBySubcategoria(Subcategoria subcategoria);

    List<Producto> findAllByMarcaAndActivoIsTrue(Marca marca);

    List<Producto> findAllByMarca(Marca marca);

    List<Producto> findAllByOrderByPrecioAsc();

    List<Producto> findAllByOrderByPrecioDesc();

    List<Producto> findAllByPrecioBetween(Double min, Double max);
}
