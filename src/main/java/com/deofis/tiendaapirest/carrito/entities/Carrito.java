package com.deofis.tiendaapirest.carrito.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "carritos")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Carrito implements Serializable {

    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    private String useremail;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "carrito_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<DetalleCarrito> items;

    public Double getTotal() {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        Double total = 0.00;

        for (DetalleCarrito item: this.items) {
            total += item.getSubtotal();
        }

        return total;
    }
}
