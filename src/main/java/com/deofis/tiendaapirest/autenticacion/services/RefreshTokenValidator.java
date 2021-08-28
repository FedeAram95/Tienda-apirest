package com.deofis.tiendaapirest.autenticacion.services;

import com.deofis.tiendaapirest.autenticacion.entities.RefreshToken;
import com.deofis.tiendaapirest.utils.scheduled.Programable;

import java.util.List;

/**
 * Servicio automático que valida la expiración de los refresh tokens, y en caso
 * de haber expirado, los elimina de la base de datos, para liberar espacio sin uso.
 * <br>
 * Extiende {@link Programable} para la tarea programada que hace la validación, y, en
 * caso de requerirlo, la eliminación de los refresh tokens expirados.
 * Los refresh tokens que se borran son solo aquellos sin uso.
 */
public interface RefreshTokenValidator extends Programable {

    /**
     * Obtiene listado de todos los refresh token que hayan expirado al
     * momento actual de la invocación al método.
     * @return {@link List<RefreshToken>} refresh tokens expirados.
     */
    List<RefreshToken> findAllExpired();

    /**
     * Elimina de la base de datos el {@link RefreshToken} que expiró al momento
     * de la verificación.
     * @param refreshToken {@link RefreshToken} a eliminar.
     */
    void delete(RefreshToken refreshToken);

}
