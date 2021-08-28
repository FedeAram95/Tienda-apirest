package com.deofis.tiendaapirest.catalogo.productos.repositories;

import com.deofis.tiendaapirest.catalogo.productos.entities.ValorPropiedadProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValorPropiedadProductoRepository extends JpaRepository<ValorPropiedadProducto, Long> {

}
