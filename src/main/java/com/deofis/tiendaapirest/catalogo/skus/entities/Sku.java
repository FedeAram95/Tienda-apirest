package com.deofis.tiendaapirest.catalogo.skus.entities;

import com.deofis.tiendaapirest.catalogo.productos.entities.Producto;
import com.deofis.tiendaapirest.catalogo.productos.entities.ValorPropiedadProducto;
import com.deofis.tiendaapirest.catalogo.promociones.entities.Promocion;
import com.deofis.tiendaapirest.imagenes.entities.Imagen;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.Nullable;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "skus")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sku implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Nullable
    @Lob
    private String descripcion;

    @NotNull(message = "El precio del Sku es obligatorio")
    private Double precio;

    private boolean activo;

    @NotNull(message = "La disponibilidad del Sku es obligatoria")
    private Integer disponibilidad;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "promocion_id")
    private Promocion promocion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "foto_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Imagen foto;

    @Column(name = "valores_data")
    private String valoresData;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinTable(name = "sku_x_valores_propiedades_producto",
            joinColumns = @JoinColumn(name = "sku_id"),
            inverseJoinColumns = @JoinColumn(name = "producto_valores_propiedad_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"sku_id", "producto_valores_propiedad_id"})})
    private List<ValorPropiedadProducto> valores;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "default_producto_id")
    @JsonIgnoreProperties(value = {"skus", "defaultSku" , "hibernateLazyInitializer", "handler"}, allowSetters = true)
    @ToString.Exclude
    private Producto defaultProducto;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "producto_id")
    @JsonIgnoreProperties(value = {"skus", "defaultSku" , "hibernateLazyInitializer", "handler"}, allowSetters = true)
    @ToString.Exclude
    private Producto producto;
}
