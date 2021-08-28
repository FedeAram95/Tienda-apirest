package com.deofis.tiendaapirest.pagos.cuentasbancarias.services.mapper;

import com.deofis.tiendaapirest.pagos.cuentasbancarias.entities.CuentaBancaria;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.facade.CuentaBancariaResponse;
import org.springframework.stereotype.Service;

@Service
public class CuentaBancariaMapperImpl implements CuentaBancariaMapper {

    @Override
    public CuentaBancariaResponse mapToResponse(CuentaBancaria cuentaBancariaEntity) {
        return CuentaBancariaResponse.builder()
                .id(cuentaBancariaEntity.getId())
                .nroCuenta(cuentaBancariaEntity.getNroCuenta())
                .tipo(cuentaBancariaEntity.getTipo().name())
                .ca(cuentaBancariaEntity.getCa().name())
                .alias(cuentaBancariaEntity.getAlias())
                .titular(cuentaBancariaEntity.getTitular())
                .banco(cuentaBancariaEntity.getBanco())
                .email(cuentaBancariaEntity.getEmail())
                .principal(cuentaBancariaEntity.isPrincipal())
                .pais(cuentaBancariaEntity.getPais().getNombre()).build();
    }
}
