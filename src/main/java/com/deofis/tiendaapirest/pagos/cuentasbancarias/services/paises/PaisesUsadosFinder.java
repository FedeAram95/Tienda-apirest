package com.deofis.tiendaapirest.pagos.cuentasbancarias.services.paises;

import com.deofis.tiendaapirest.pagos.cuentasbancarias.dto.PaisDto;

import java.util.List;

public interface PaisesUsadosFinder {

    /**
     * Encuentra y devuelve listado de paises que están siendo usados realmente por cuentas
     * bancarias.
     * @return {@link List<PaisDto>} listado de paises.
     */
    List<PaisDto> findPaisesEnCuentas();
}
