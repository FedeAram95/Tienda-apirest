package com.deofis.tiendaapirest.pagos.cuentasbancarias.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaisDto implements Serializable {
    private Long id;
    private String nombre;
    private String codigo;
}
