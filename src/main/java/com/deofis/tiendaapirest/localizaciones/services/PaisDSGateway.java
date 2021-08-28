package com.deofis.tiendaapirest.localizaciones.services;

import com.deofis.tiendaapirest.localizaciones.entities.Pais;

import java.util.List;

/**
 * Clase que conecta consume el repositorio de paises (admin. de datos)
 */
public interface PaisDSGateway {

    /**
     * Devuelve un listado con todos los países registrados en el sistema.
     * @return {@link List<Pais>} listado de países.
     */
    List<Pais> findAll();
    /**
     * Encuentra y obtiene un {@link Pais} requerido, a través de su id.
     * @param paisId Long id del país a encontrar.
     * @return {@link Pais}.
     */
    Pais findById(Long paisId);

    /**
     * Encuentra y obtiene un {@link Pais} requerido, por su nombre.
     * @param pais String nombre del país a encontrar.
     * @return {@link Pais}.
     */
    Pais findByNombre(String pais);

    /**
     * Encuentra y obtiene un {@link Pais} requerido, por su código ISO-2.
     * @param code String código del país en formato ISO-2.
     * @return {@link Pais}.
     */
    Pais findByCodigo(String code);
}
