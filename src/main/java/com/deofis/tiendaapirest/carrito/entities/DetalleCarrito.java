package com.deofis.tiendaapirest.carrito.entities;

import com.deofis.tiendaapirest.catalogo.skus.entities.Sku;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "carrito_detalles")
@AllArgsConstructor
@NoArgsConstructor
public class DetalleCarrito implements Serializable {

    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sku_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Sku sku;
    private Integer cantidad;

    public Double getSubtotal() {
        // Si existe promoción vigente, el subtotal se calcula acorde al precio oferta.
        if (sku.getPromocion() != null && sku.getPromocion().getEstaVigente()) {
            return cantidad.doubleValue() * sku.getPromocion().getPrecioOferta();
        }

        return cantidad.doubleValue() * sku.getPrecio();
    }
}
