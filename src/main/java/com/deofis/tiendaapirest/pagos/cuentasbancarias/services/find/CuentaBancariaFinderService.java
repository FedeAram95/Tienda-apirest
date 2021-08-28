package com.deofis.tiendaapirest.pagos.cuentasbancarias.services.find;

import com.deofis.tiendaapirest.pagos.cuentasbancarias.exceptions.CuentaBancariaException;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.facade.CuentaBancariaResponse;

import java.util.List;

/**
 * Servicio para encontrar cuentas bancarias (por id, o por pais).
 */
public interface CuentaBancariaFinderService {

    /**
     * Encuentra y devuelve la cuenta bancaria a través de su id.
     * @param cuentaId long id de la cuenta a buscar.
     * @return {@link CuentaBancariaResponse} con los datos de la cuenta bancaria.
     */
    CuentaBancariaResponse find(Long cuentaId) throws CuentaBancariaException;

    /**
     * Encuentra y devuelve la cuenta bancaria principal.
     * @return {@link CuentaBancariaResponse} con los datos de la cuenta bancaria.
     */
    CuentaBancariaResponse findPrincipal() throws CuentaBancariaException;

    /**
     * Encuentra y devuelve la cuenta bancaria de un país requerido, a través del id del país.
     * @param paisId long id del país.
     * @return {@link CuentaBancariaResponse} con los datos de la cuenta bancaria.
     */
    CuentaBancariaResponse findByPais(Long paisId) throws CuentaBancariaException;

    /**
     * Encuentra y devuelve el listado completo de cuentas registradas.
     * @return {@link List<CuentaBancariaResponse>} listado de cuentas.
     */
    List<CuentaBancariaResponse> findCuentas();
}
