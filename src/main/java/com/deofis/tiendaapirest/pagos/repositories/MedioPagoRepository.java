package com.deofis.tiendaapirest.pagos.repositories;

import com.deofis.tiendaapirest.pagos.entities.MedioPago;
import com.deofis.tiendaapirest.pagos.entities.MedioPagoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedioPagoRepository extends JpaRepository<MedioPago, Long> {

    Optional<MedioPago> findByNombre(MedioPagoEnum medioPago);
}
