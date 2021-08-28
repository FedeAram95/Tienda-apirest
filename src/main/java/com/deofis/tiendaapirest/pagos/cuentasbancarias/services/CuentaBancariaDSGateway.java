package com.deofis.tiendaapirest.pagos.cuentasbancarias.services;

import com.deofis.tiendaapirest.localizaciones.entities.Pais;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.entities.CuentaBancaria;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.exceptions.CuentaBancariaException;

import java.util.List;
import java.util.Optional;

/**
 * Esta clase delega la responsabilidad del manejo de datos (data source) a la clase repositorio pasada por parámetro
 * en su constructor. (por defecto: jpa/hibernate).
 */
public interface CuentaBancariaDSGateway {

    /**
     * Guarda una {@link CuentaBancaria} requerida.
     * @param cuentaBancaria {@link CuentaBancaria}.
     * @return la cuenta guardada.
     */
    CuentaBancaria save(CuentaBancaria cuentaBancaria);

    /**
     * Busca y encuentra una {@link CuentaBancaria} a través de un id requerido.
     * @param cuentaId Long id de la cuenta a buscar.
     * @return {@link CuentaBancaria}.
     * @throws CuentaBancariaException si no existe cuenta con id.
     */
    CuentaBancaria findById(Long cuentaId) throws CuentaBancariaException;

    /**
     * Encuentra la primer cuenta bancaria marcada como principal (y única).
     * @return {@link CuentaBancaria} encontrada.
     */
    CuentaBancaria findByPrincipal() throws CuentaBancariaException;

    /**
     * Valida si una {@link CuentaBancaria} existe para un país requerido.
     * @param pais {@link Pais} a validar.
     * @return boolean --> true: existe, false: no existe cuenta p/ dicho país.
     */
    boolean existsByPais(Pais pais);

    /**
     * Busca y devuelve una {@link CuentaBancaria} a través del id de un país requerido.
     * @param paisId Long id requerido,
     * @return {@link CuentaBancaria}.
     * @throws CuentaBancariaException si no existe la cuenta para dicho pais.
     */
    CuentaBancaria findByPais(Long paisId) throws CuentaBancariaException;

    /**
     * Este método deberia ser responsabilidad del DS gateway de paises. Al rediseñar, eliminar
     * este método y sus usos.
     * @param paisId pais id.
     * @return {@link Pais}.
     */
    Pais findPaisById(Long paisId);

    /**
     * Busca y devuelve listado con todas las {@link CuentaBancaria}s existentes.
     * @return lista de cuentas.
     */
    List<CuentaBancaria> findAll();

    /**
     * Elimina una cuenta bancaria, a través de su id.
     * @param cuentaId Long id de la cuenta a eliminar.
     * @throws CuentaBancariaException si no existe cuenta bancaria con el id requerido.
     */
    void deleteById(Long cuentaId) throws CuentaBancariaException;

    /**
     * Encuentra y devuelve la primer cuenta bancaria que se encuentra.
     * @return {@link CuentaBancaria}.
     * @throws CuentaBancariaException si no existe ninguna cuenta.
     */
    Optional<CuentaBancaria> findFirst();
}
