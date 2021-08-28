package com.deofis.tiendaapirest.clientes.entities;

import com.deofis.tiendaapirest.localizaciones.entities.Ciudad;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Data
@Table(name = "direcciones")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Direccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La ciudad es obligatoria.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ciudad_id")
    @JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
    private Ciudad ciudad;

    @NotNull(message = "El nombre de la calle es obligatorio.")
    private String calle;

    @NotNull(message = "El número de la calle es obligatorio.")
    @Column(name = "numero_calle")
    private String numeroCalle;

    private String piso;

    @NotNull(message = "El código postal es obligatorio.")
    @Column(name = "codigo_postal")
    private String codigoPostal;
}
