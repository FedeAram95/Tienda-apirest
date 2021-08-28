package com.deofis.tiendaapirest.autenticacion.services;

import com.deofis.tiendaapirest.autenticacion.entities.RefreshToken;

/**
 * Servicio para generar un token random que sirve para extender la sesión.
 * Este token no es igual al JsonWebToken (JWT) final que será generado para extender la sesión,
 * sino que debe ser un token genérico para identificar al usuario que hace petición de extender
 * la sesión y validar que la petición es autentica.
 * NOTA: Este servicio no se encarga de la identificación en sí, sino del manejo del
 * refresh token.
 */

public interface RefreshTokenService {

    /**
     * Genera el token a travéz de algún algoritmo para generar un String único.
     * @return RefreshToken objeto con los datos: token y nuevo Date de expiración.
     */
    RefreshToken generarRefreshToken();

    /**
     * Extiende la validez del refresh token.
     * @param token String refresh token a extender.
     */
    void extenderRefreshToken(String token);

    /**
     * Valida que el token existe y es autentico.
     * @param token String con el token a validar.
     */
    void validarRefreshToken(String token);

    /**
     * Elimina el refresh token en el caso de que se quiera cerrar la sesión.
     * @param token String con el token a eliminar.
     */
    void eliminarRefreshToken(String token);
}
