package com.deofis.tiendaapirest.autenticacion.services;

import com.deofis.tiendaapirest.autenticacion.dto.AuthResponse;
import com.deofis.tiendaapirest.autenticacion.dto.IniciarSesionRequest;
import com.deofis.tiendaapirest.autenticacion.dto.RefreshTokenRequest;
import com.deofis.tiendaapirest.autenticacion.dto.SignupRequest;
import com.deofis.tiendaapirest.autenticacion.entities.Usuario;

/**
 * Este servicio se encarga de la lógica para: crear un nuevo usuario, iniciar sesión,
 * extender sesión,y cerrar sesión.
 */
public interface AutenticacionService {

    /**
     * Registra una nueva cuenta de usuario en el sistema. Se encarga de crear un usuario
     * y de enviar una notificación para activar la cuenta.
     * @param signupRequest SignupRequest con los datos: email y contraseña.
     */
    void registrarse(SignupRequest signupRequest);

    /**
     * Verifica la cuenta y la vincula al usuario correspondiente.
     * @param token String token de generación que se envia al registrarse.
     */
    void verificarCuenta(String token);

    /**
     * Iniciar sesión en el sistema. Valida que el usuario y contraseña sean correctas,
     * crea la autenticación y genera un JWT para el usuario.
     * @param iniciarSesionRequest IniciarSesionRequest con los datos: email y contraseña.
     * @return AuthResponse con los datos de inicio de sesión (incluido JWT).
     */
    AuthResponse iniciarSesion(IniciarSesionRequest iniciarSesionRequest);

    /**
     * Cierra sesión en el sistema, eliminando el refresh token asociado al usuario actual
     * logueado.
     * @param refreshTokenRequest RefreshTokenRequest (useremail y refresh token) del usuario
     *                            que requiere cerrar sesión.
     */
    void cerrarSesion(RefreshTokenRequest refreshTokenRequest);

    /**
     * Genera un nuevo JWT a partir de un refresh token asociado al usuario que lo solicita.
     * @param refreshTokenRequest RefreshTokenRequest (email y refresh token).
     * @return AuthResponse con los nuevos datos de autenticación (JWT y fecha expiración).
     */
    AuthResponse refrescarToken(RefreshTokenRequest refreshTokenRequest);

    /**
     * Devuelve el usuario actual de la sesión requerida.
     * @return Usuario usuario actual.
     */
    Usuario getUsuarioActual();

    /**
     * Verifica que un usuario esté autenticado en el sistema
     * @return boolean true -> autenticado, false -> no autenticado.
     */
    boolean estaLogueado();
}
