package com.deofis.tiendaapirest.autenticacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {

    @NotNull
    private String refreshToken;
    @NotNull
    private String userEmail;
}
