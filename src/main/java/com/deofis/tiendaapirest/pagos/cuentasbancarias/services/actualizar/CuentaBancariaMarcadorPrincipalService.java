package com.deofis.tiendaapirest.pagos.cuentasbancarias.services.actualizar;

import com.deofis.tiendaapirest.pagos.cuentasbancarias.exceptions.CuentaBancariaException;

public interface CuentaBancariaMarcadorPrincipalService {

    /**
     * Servicio que marca una cuenta requerida, a través de su id, como principal. Su comportamiento
     * esperado también es mantener la integridad de que otras cuentas se desmarquen como principal.
     * @param cuentaId Long id de la cuenta a marcar principal.
     */
    void marcarCuentaPrincipal(Long cuentaId) throws CuentaBancariaException;
}
