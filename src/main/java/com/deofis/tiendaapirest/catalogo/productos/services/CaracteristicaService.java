package com.deofis.tiendaapirest.catalogo.productos.services;

import com.deofis.tiendaapirest.catalogo.productos.entities.Caracteristica;
import com.deofis.tiendaapirest.utils.crud.CrudService;

/**
 * Este servicio maneja las caracteristicas, las cuales van a ser parte de los productos.
 */
public interface CaracteristicaService extends CrudService<Caracteristica, Long> {

    /**
     * Método que se encarga de crear una nueva característica y de guardarla en la base de datos.
     * @param caracteristica {@link Caracteristica} nueva a crear.
     * @return {@link Caracteristica} creada y guardada en la base de datos.
     */
    Caracteristica crearCaracteristica(Caracteristica caracteristica);

    /**
     * Busca y devuelve una característica requerida a través de su id.
     * @param caracteristicaId Long id de la caracteristica.
     * @return {@link Caracteristica}.
     */
    Caracteristica obtenerCaracteristica(Long caracteristicaId);

    /**
     * Método que se encarga de eliminar una característica existente de la base de datos,
     * a través de su id.
     * @param caracteristicaId Long id de la característica a eliminar.
     */
    void eliminarCaracteristica(Long caracteristicaId);
}
