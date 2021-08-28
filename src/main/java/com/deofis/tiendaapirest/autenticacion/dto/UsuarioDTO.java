package com.deofis.tiendaapirest.autenticacion.dto;

import com.deofis.tiendaapirest.autenticacion.entities.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * DataTransferObject del Usuario --> No mostrar información sensible (aunque encriptada)
 * a los administradores del sistema.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDTO {

    private Long id;
    private String email;
    private boolean enabled;
    private Date fechaCreacion;
    private AuthProvider authProvider;
    private String providerId;
    private String rol;
}
