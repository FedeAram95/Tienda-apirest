package com.deofis.tiendaapirest.catalogo.productos.services;

import com.deofis.tiendaapirest.catalogo.productos.entities.Marca;
import com.deofis.tiendaapirest.utils.crud.CrudService;

import java.util.List;

public interface MarcaService extends CrudService<Marca, Long> {

    /**
     * Método para registrar una nueva marca.
     * @param marca Marca a guardar.
     * @return Marca guardada en la BD.
     */
    Marca crearMarca(Marca marca);

    /**
     * Método para obtener todas las marcas ordenadas por nombre asc.
     * @return Lista de todas las marcas en la BD.
     */
    List<Marca> obtenerMarcas();

    /**
     * Método para obtener una marca específica por su Id.
     * @param id Long id de la marca a obtener.
     * @return Marca en la BD con el id solicitado.
     */
    Marca obtenerMarca(Long id);

    /**
     * Método para actualizar una marca.
     * @param marca Marca modificada.
     * @param id Long id de la marca a modificar.
     * @return Marca actualizada.
     */
    Marca actualizar(Marca marca, Long id);

    /**
     * Elimina una {@link Marca} en caso de que no exista ninguna referencia a la misma.
     * @param marca Marca a eliminar.
     */
    void eliminarMarca(Marca marca);

    /**
     * Elimina una {@link Marca}, a través de su id, en caso de que no existe ninguna
     * referencia a la misma.
     * @param marcaId Long id de la marca a eliminar.
     */
    void eliminarMarca(Long marcaId);
}
