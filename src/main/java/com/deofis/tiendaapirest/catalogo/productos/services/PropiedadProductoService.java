package com.deofis.tiendaapirest.catalogo.productos.services;

import com.deofis.tiendaapirest.catalogo.productos.entities.PropiedadProducto;
import com.deofis.tiendaapirest.catalogo.productos.entities.ValorPropiedadProducto;
import com.deofis.tiendaapirest.utils.crud.CrudService;

import java.util.List;

/**
 * Este servicio se encarga de las propiedades del producto {@link PropiedadProducto},
 * y de sus valores {@link ValorPropiedadProducto}.
 * Es responsable de la creación de nuevas propiedades y valores, actualización, listado y eliminación de
 * los mismos.
 */

public interface PropiedadProductoService extends CrudService<PropiedadProducto, Long> {

    /**
     * Crea una nueva propiedad de producto.
     * @param propiedadProducto PropiedadProducto a crear.
     * @return PropiedadProducto creada y guardada en la base de datos.
     */
    PropiedadProducto crearPropiedadProducto(PropiedadProducto propiedadProducto);

    /**
     * Obtiene todas las propiedades de producto existentes.
     * @return List de las propiedades producto.
     */
    List<PropiedadProducto> obtenerPropiedadesProducto();

    /**
     * Obtiene una propiedad de producto requerida a través de su ID.
     * @param propiedadId Long id de la propiedad solicitada.
     * @return PropiedadProducto requerida.
     */
    PropiedadProducto obtenerPropiedadProducto(Long propiedadId);

    /**
     * Actualiza una propiedad producto existente y la devuelve actualizada.
     * @param propiedadId Long id de la propiedad a actualizar.
     * @param propiedadProducto PropiedadProducto actualizada.
     * @return PropiedadProducto actualizada y guardada en la base de datos.
     */
    PropiedadProducto actualizarPropiedadProducto(Long propiedadId, PropiedadProducto propiedadProducto);

    /**
     * Elimina una propiedad producto requerida.
     * @param propiedadId Long id de la propiedad a eliminar.
     */
    void eliminarPropiedadProducto(Long propiedadId);

    /**
     * Crea un nuevo valor de propiedad, asociada a una propiedad de producto específica.
     * @param propiedadId Long propiedad id a la cual va a pertenecer el nuevo valor.
     * @param valor ValorPropiedadProducto nueva a crear.
     * @return PropiedadProducto actualizada con el nuevo valor creado y asociado.
     */
    PropiedadProducto crearValorPropiedad(Long propiedadId, ValorPropiedadProducto valor);

    /**
     * Obtiene todos los valores asociados a una propiedad de producto requerida.
     * @param propiedadId Long id de la propiedad a listar sus valores.
     * @return List listado de todos los valores pertenecientes a la propiedad producto.
     */
    List<ValorPropiedadProducto> obtenerValoresDePropiedad(Long propiedadId);

    /**
     * Actualiza un valor de una propiedad requerida.
     * @param propiedadId Long id de la propiedad a la que pertenece el valor a actualizar.
     * @param valorId Long id del valor a actualizar.
     * @param valor ValorPropiedadProducto actualizado.
     * @return PropiedadProducto actualizado y guardado.
     */
    PropiedadProducto actualizarValorPropiedad(Long propiedadId, Long valorId, ValorPropiedadProducto valor);

    /**
     * Elimina un valor de una propiedad.
     * @param propiedadId Long id de la propiedad a la que pertenece el valor a eliminar.
     * @param valorId Long id del valor a eliminar.
     */
    void eliminarValorPropiedad(Long propiedadId, Long valorId);
}
