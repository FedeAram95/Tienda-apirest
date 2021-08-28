package com.deofis.tiendaapirest.catalogo.promociones.repositories;

import com.deofis.tiendaapirest.catalogo.promociones.entities.Promocion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromocionRepository extends JpaRepository<Promocion, Long> {

}
