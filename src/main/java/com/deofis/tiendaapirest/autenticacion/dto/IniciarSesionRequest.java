package com.deofis.tiendaapirest.autenticacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IniciarSesionRequest {

    @NotNull(message = "Ingresa tu email")
    private String email;
    @NotNull(message = "Ingresa tu contraseña")
    private String password;
}
