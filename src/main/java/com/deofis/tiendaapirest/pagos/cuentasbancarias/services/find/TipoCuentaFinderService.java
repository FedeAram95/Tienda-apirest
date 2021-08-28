package com.deofis.tiendaapirest.pagos.cuentasbancarias.services.find;

import com.deofis.tiendaapirest.pagos.cuentasbancarias.dto.TipoCuentaBancariaDto;

import java.util.List;

public interface TipoCuentaFinderService {
    List<TipoCuentaBancariaDto> listarTiposCuenta();
}
