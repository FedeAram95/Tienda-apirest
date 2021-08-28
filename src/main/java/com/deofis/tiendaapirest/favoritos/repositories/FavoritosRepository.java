package com.deofis.tiendaapirest.favoritos.repositories;

import com.deofis.tiendaapirest.favoritos.entities.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoritosRepository extends JpaRepository<Favorito, Long> {

}
