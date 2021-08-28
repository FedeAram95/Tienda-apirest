package com.deofis.tiendaapirest.ecommerce.repositories;

import com.deofis.tiendaapirest.ecommerce.entities.EcommerceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EcommerceRepository extends JpaRepository<EcommerceConfig, Long> {

}
