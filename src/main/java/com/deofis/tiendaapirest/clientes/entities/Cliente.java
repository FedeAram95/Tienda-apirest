package com.deofis.tiendaapirest.clientes.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "clientes")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cliente implements Serializable {

    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El nombre es obligatorio.")
    private String nombre;

    @NotNull(message = "El apellido es obligatorio.")
    private String apellido;

    private Long dni;

    private String email;

    private String telefono;

    @Column(name = "fecha_nacimiento")
    private Date fechaNacimiento;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "direccion_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Direccion direccion;
}
