package com.deofis.tiendaapirest.administracionusuarios.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * Esta clase sirve para DataTransfer de los distintos registros que se hacen al cambiar
 * el rol de un Usuario.
 */

@Entity
@Data
@Table(name = "cambios_rol")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CambioRol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "usuario_admin")
    private String usuarioAdmin;
    @Column(name = "usuario_cambio_rol")
    private String usuarioCambioRol;
    @Column(name = "nuevo_rol")
    private String nuevoRol;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_cambio")
    private Date fechaCambio;
}
