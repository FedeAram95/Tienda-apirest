package com.deofis.tiendaapirest.pagos.cuentasbancarias.services.actualizar;

import com.deofis.tiendaapirest.pagos.cuentasbancarias.entities.CuentaBancaria;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.exceptions.CuentaBancariaException;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.facade.CuentaBancariaResponse;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.facade.CuentaBancariaUpdateRequest;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.services.CuentaBancariaDSGateway;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.services.mapper.CuentaBancariaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CuentaBancariaActualizador implements CuentaBancariaActualizadorService {

    private final CuentaBancariaDSGateway cuentaBancariaDSGateway;
    private final CuentaBancariaMapper mapper;

    @Autowired
    public CuentaBancariaActualizador(CuentaBancariaDSGateway cuentaBancariaDSGateway, CuentaBancariaMapper mapper) {
        this.cuentaBancariaDSGateway = cuentaBancariaDSGateway;
        this.mapper = mapper;
    }

    @Override
    public CuentaBancariaResponse actualizarDatos(Long cuentaId, CuentaBancariaUpdateRequest cuentaBancariaRequest)
            throws CuentaBancariaException {
        CuentaBancaria currentCuenta = this.cuentaBancariaDSGateway.findById(cuentaId);
        currentCuenta.setNroCuenta(cuentaBancariaRequest.getNroCuenta());
        currentCuenta.setTipo(cuentaBancariaRequest.getTipo());
        currentCuenta.setCa(cuentaBancariaRequest.getCa());
        currentCuenta.setAlias(cuentaBancariaRequest.getAlias());
        currentCuenta.setEmail(cuentaBancariaRequest.getEmail());
        currentCuenta.setTitular(cuentaBancariaRequest.getTitular());
        currentCuenta.setBanco(cuentaBancariaRequest.getBanco());
        return this.mapper.mapToResponse(this.cuentaBancariaDSGateway.save(currentCuenta));
    }
}
