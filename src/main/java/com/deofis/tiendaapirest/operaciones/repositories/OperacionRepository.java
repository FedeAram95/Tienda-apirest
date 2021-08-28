package com.deofis.tiendaapirest.operaciones.repositories;

import com.deofis.tiendaapirest.clientes.entities.Cliente;
import com.deofis.tiendaapirest.operaciones.entities.EstadoOperacion;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OperacionRepository extends JpaRepository<Operacion, Long> {

    List<Operacion> findAllByOrderByFechaOperacionDesc();

    List<Operacion> findAllByClienteOrderByFechaOperacionAsc(Cliente cliente);

    List<Operacion> findAllByEstado(EstadoOperacion estado);

    Optional<Operacion> findByNroOperacionAndCliente(Long nroOperacion, Cliente cliente);

    List<Operacion> findAllByEstadoAndCliente(EstadoOperacion estado, Cliente cliente);

    List<Operacion> findAllByFechaOperacionBetween(Date date1, Date date2);

    @Query(value = "SELECT * FROM operaciones op WHERE cliente_id = ?1 AND YEAR(fecha_operacion) = ?2",
            nativeQuery = true)
    List<Operacion> operacionesByYear(Long clienteId, Integer year);

    @Query(value = "SELECT * FROM operaciones op WHERE cliente_id = ?1 AND YEAR(fecha_operacion) = ?2 AND MONTH(fecha_operacion) = ?3",
            nativeQuery = true)
    List<Operacion> operacionesByMonth(Long clienteId, Integer year, Integer month);
}
