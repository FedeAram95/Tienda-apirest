package com.deofis.tiendaapirest.carrito.repositories;

import com.deofis.tiendaapirest.carrito.entities.DetalleCarrito;
import com.deofis.tiendaapirest.catalogo.skus.entities.Sku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleCarritoRepository extends JpaRepository<DetalleCarrito, Long> {

    void deleteBySku(Sku sku);

}
