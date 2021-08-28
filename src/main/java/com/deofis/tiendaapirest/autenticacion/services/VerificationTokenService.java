package com.deofis.tiendaapirest.autenticacion.services;

import com.deofis.tiendaapirest.autenticacion.entities.Usuario;
import com.deofis.tiendaapirest.autenticacion.entities.VerificationToken;

/**
 * Servicio para generar tokens de validación para completar el registro de usuarios.
 */
public interface VerificationTokenService {

    String generarVerificationToken(Usuario usuario);

    VerificationToken getVerificationToken(String token);

    void delete(VerificationToken verificationToken);
}
