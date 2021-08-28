package com.deofis.tiendaapirest.pagos.cuentasbancarias.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class TipoCuentaBancariaDto implements Serializable {
    private String tipo;

    public TipoCuentaBancariaDto() {

    }

    public TipoCuentaBancariaDto(String tipo) {
        this.tipo = tipo;
    }
}
