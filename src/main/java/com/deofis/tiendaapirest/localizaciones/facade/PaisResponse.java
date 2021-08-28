package com.deofis.tiendaapirest.localizaciones.facade;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaisResponse {
    private Long id;
    private String nombre;
    private String codigo;
}
