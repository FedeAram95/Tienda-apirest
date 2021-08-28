package com.deofis.tiendaapirest.utils.crud;

import java.io.Serializable;
import java.util.List;

/**
 * Servicio CRUD base a implementar por servicios que requieran de operaciones de Alta/Baja/Modificación
 * y Consulta.
 * @param <T> Tipo del objeto a realizar operación CRUD, por lo general son clases entidad del dominio.
 * @param <ID> Tipo del id del objeto, ej.: Long, String, etc...
 */
public interface CrudService<T extends Serializable, ID> {

    /**
     * Busca y devuelve una lista con todos los objetos del tipo T.
     * @return List de objectos T.
     */
    List<T> findAll();

    /**
     * Busca y devuelve un objeto T único a través de su ID.
     * @param id ID del objeto a buscar.
     * @return T objeto.
     */
    T findById(ID id);

    /**
     * Guarda un objeto T en la base de datos.
     * @param object T objeto a guardar.
     * @return T objeto guardado en la base de datos.
     */
    T save(T object);

    /**
     * Elimina un objeto T de la base de datos.
     * @param object T objeto a eliminar.
     */
    void delete(T object);

    /**
     * Elimina un objeto T de la base de datos a través de su ID.
     * @param id ID del objeto a eliminar.
     */
    void deleteById(ID id);
}
