package com.deofis.tiendaapirest.pagos.cuentasbancarias.services.mapper;

import com.deofis.tiendaapirest.pagos.cuentasbancarias.entities.CuentaBancaria;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.facade.CuentaBancariaResponse;

/**
 * Clase que se encarga de mappear los objetos de entidad -> facade y viceversa.
 */
public interface CuentaBancariaMapper {
    CuentaBancariaResponse mapToResponse(CuentaBancaria cuentaBancariaEntity);
}
