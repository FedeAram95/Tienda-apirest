package com.deofis.tiendaapirest.pagos.cuentasbancarias.repositories;

import com.deofis.tiendaapirest.localizaciones.entities.Pais;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.entities.CuentaBancaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuentaBancariaRepository extends JpaRepository<CuentaBancaria, Long> {
    Optional<CuentaBancaria> findByPais(Pais pais);
    Optional<CuentaBancaria> findFirstByPrincipal(boolean principal);
    boolean existsByPais(Pais pais);
}
