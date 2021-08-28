package com.deofis.tiendaapirest.pagos.repositories;

import com.deofis.tiendaapirest.pagos.entities.OperacionPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperacionPagoRepository extends JpaRepository<OperacionPago, String> {

}
