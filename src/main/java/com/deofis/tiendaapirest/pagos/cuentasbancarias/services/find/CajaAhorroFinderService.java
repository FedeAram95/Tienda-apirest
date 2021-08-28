package com.deofis.tiendaapirest.pagos.cuentasbancarias.services.find;

import com.deofis.tiendaapirest.pagos.cuentasbancarias.dto.CajaAhorroDto;

import java.util.List;

public interface CajaAhorroFinderService {
    List<CajaAhorroDto> listarCajasAhorro();
}
