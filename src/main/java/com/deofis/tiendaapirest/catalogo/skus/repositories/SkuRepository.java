package com.deofis.tiendaapirest.catalogo.skus.repositories;

import com.deofis.tiendaapirest.catalogo.skus.entities.Sku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkuRepository extends JpaRepository<Sku, Long> {

}
