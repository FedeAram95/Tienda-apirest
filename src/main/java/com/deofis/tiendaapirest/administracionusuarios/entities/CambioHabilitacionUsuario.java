package com.deofis.tiendaapirest.administracionusuarios.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "cambios_habilitacion_usuarios")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CambioHabilitacionUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "usuario_admin")
    private String usuarioAdmin;
    @Column(name = "usuario_cambio_habilitacion")
    private String usuarioCambioHabilitacion;
    private String accion;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_cambio")
    private Date fechaCambio;
}
