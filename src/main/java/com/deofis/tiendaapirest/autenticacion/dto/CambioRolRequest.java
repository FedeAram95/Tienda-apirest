package com.deofis.tiendaapirest.autenticacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CambioRolRequest {

    private Long rolId;
    private String usuarioEmail;
}
