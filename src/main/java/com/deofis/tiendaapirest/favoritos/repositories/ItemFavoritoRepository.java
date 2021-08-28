package com.deofis.tiendaapirest.favoritos.repositories;

import com.deofis.tiendaapirest.catalogo.productos.entities.Producto;
import com.deofis.tiendaapirest.favoritos.entities.ItemFavorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemFavoritoRepository extends JpaRepository<ItemFavorito, Long> {

    void deleteByProducto(Producto producto);

}
