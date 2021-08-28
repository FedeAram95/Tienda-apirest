package com.deofis.tiendaapirest.catalogo.productos.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "producto_valores_propiedad")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValorPropiedadProducto implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "El valor de la propiedad de producto es obligatorio")
    private String valor;

}
