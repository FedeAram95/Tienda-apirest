package com.deofis.tiendaapirest.catalogo.categorias.entities;

import com.deofis.tiendaapirest.catalogo.productos.entities.PropiedadProducto;
import com.deofis.tiendaapirest.imagenes.entities.Imagen;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "subcategorias")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subcategoria implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String codigo;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "foto_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Imagen foto;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinTable(name = "subcategorias_x_propiedades",
            joinColumns = @JoinColumn(name = "subcategoria_id"),
            inverseJoinColumns = @JoinColumn(name = "producto_propiedad_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"subcategoria_id", "producto_propiedad_id"})})
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<PropiedadProducto> propiedades;
}
