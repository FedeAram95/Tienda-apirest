package com.deofis.tiendaapirest.catalogo.productos.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "producto_propiedades")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropiedadProducto implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "El nombre de la propiedad del producto es obligatorio")
    private String nombre;
    /**
     * Este atributo será true si se quiere utilizar la propiedad de producto para
     * generar combinaciones. Si es false, no se usará para combinaciones, lo que implica
     * que será solo una propiedad visible, pero no será seleccionable para vender.
     */
    private boolean variable;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "producto_propiedad_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<ValorPropiedadProducto> valores;
}
