package com.deofis.tiendaapirest.autenticacion.dto;

import com.deofis.tiendaapirest.clientes.entities.Cliente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {

    @NotNull(message = "El email del usuario es obligatorio.")
    private String email;
    @NotNull(message = "La contraseña es obligatoria.")
    private String password;
    @NotNull(message = "Los datos del cliente son obligatorios")
    private Cliente cliente;
}
