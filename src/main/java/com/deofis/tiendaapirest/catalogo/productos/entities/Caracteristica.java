package com.deofis.tiendaapirest.catalogo.productos.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "producto_caracteristicas")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Caracteristica implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "El nombre de la característica es obligatorio")
    private String nombre;
    @NotNull(message = "El valor de la característica es obligatorio")
    private String valor;
}
