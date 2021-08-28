package com.deofis.tiendaapirest.pagos.cuentasbancarias.services.eliminar;

import com.deofis.tiendaapirest.pagos.cuentasbancarias.exceptions.CuentaBancariaException;

public interface CuentaBancariaEliminadorService {

    /**
     * Servicio para eliminar una cuenta bancaria existente, a través de su id.
     * <br>
     * También deberá marcar como principal a la primer cuenta que se encuentre.
     * @param cuentaId Long id de la cuenta a eliminar.
     * @throws CuentaBancariaException si no existe la cuenta con id requerido.
     */
    void eliminarCuentaBancaria(Long cuentaId) throws CuentaBancariaException;
}
