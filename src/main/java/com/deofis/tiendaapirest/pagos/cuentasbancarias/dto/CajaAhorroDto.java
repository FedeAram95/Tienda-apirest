package com.deofis.tiendaapirest.pagos.cuentasbancarias.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CajaAhorroDto {

    private String ca;

    public CajaAhorroDto() {

    }

    public CajaAhorroDto(String ca) {
        this.ca = ca;
    }
}
