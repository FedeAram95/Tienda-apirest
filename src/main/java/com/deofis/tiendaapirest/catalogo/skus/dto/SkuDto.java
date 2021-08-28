package com.deofis.tiendaapirest.catalogo.skus.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SkuDto {

    private Long id;
    private String nombre;
    private String valores;
    private Double precio;
    private Integer disponibilidad;

}
