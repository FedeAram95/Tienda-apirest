package com.deofis.tiendaapirest.catalogo.categorias.entities;

import com.deofis.tiendaapirest.imagenes.entities.Imagen;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "categorias")
@AllArgsConstructor
@Builder
public class Categoria implements Serializable {
    private final static long serialVersioUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "foto_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Imagen foto;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "categoria_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Subcategoria> subcategorias;

    public Categoria() {
        this.subcategorias = new ArrayList<>();
    }
}
