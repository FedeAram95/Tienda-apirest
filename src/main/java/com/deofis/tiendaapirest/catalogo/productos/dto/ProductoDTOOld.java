package com.deofis.tiendaapirest.catalogo.productos.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductoDTOOld {

    private Long id;

    @CsvBindByName
    private String nombre;

    @CsvBindByName
    private String descripcion;

    @CsvBindByName
    private Double precio;

    @CsvBindByName
    private Long subcategoriaId;

    @CsvBindByName
    private Long marcaId;

    @CsvBindByName
    private Long unidadMedidaId;

    @CsvBindByName
    private Integer disponibilidad;
}
