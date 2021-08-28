package com.deofis.tiendaapirest.localizaciones.services;

import com.deofis.tiendaapirest.localizaciones.entities.Ciudad;
import com.deofis.tiendaapirest.localizaciones.entities.Estado;
import com.deofis.tiendaapirest.localizaciones.entities.Pais;
import com.deofis.tiendaapirest.localizaciones.facade.PaisResponse;

import java.util.List;

/**
 * Servicio que provee la lógica para listar los paises y estados cargados en la base
 * de datos.
 */

public interface LocalizacionService {

    /**
     * Lista los paises de forma ordenada de menor a mayor por nombre.
     * @return List de paises.
     */
    List<Pais> listarPaises();

    /**
     * Obtener un pais por su id.
     * @param id Long id del pais.
     * @return Pais en la base de datos.
     */
    Pais obtenerPais(Long id);

    /**
     * Obtener un pais por su nombre.
     * @param nombrePais String nombre del pais.
     * @return Pais en la base de datos.
     */
    Pais obtenerPais(String nombrePais);

    /**
     * Obtiene un país por su nombre.
     * @param nombrePais String nombre del país.
     * @return {@link PaisResponse}.
     */
    PaisResponse obtenerPaisResponse(String nombrePais);

    /**
     * Obtiene un país a través de su codigo ISO-2.
     * @param codigo String código del país.
     * @return {@link PaisResponse}.
     */
    PaisResponse obtenerPaisByCodigo(String codigo);

    /**
     * Lista los estados que pertenecen a un país seleccionado por su id.
     * @param paisId Long id del país.
     * @return List de estados del país.
     */
    List<Estado> estadosDePais(Long paisId);

    /**
     * Lista los estados que pertenecen a un país seleccionoado.
     * @param nombrePais String del nombre del pais seleccionado.
     * @return List de estados del país.
     */
    List<Estado> estadosDePais(String nombrePais);

    /**
     * Obtiene un {@link Estado} requerido.
     * @param nombreEstado String nombre del estado.
     * @return Estado.
     */
    Estado obtenerEstado(String nombreEstado);

    /**
     * Obtiene un {@link Estado} a través de su id.
     * @param estadoId Long id del estado.
     * @return Estado.
     */
    Estado obtenerEstado(Long estadoId);

    /**
     * Lista las ciudades pertenecientes a un estado de un país.
     * @param nombrePais String nombre del país.
     * @param nombreEstado String nombre del estado.
     * @return List listado de ciudades del estado requerido.
     */
    List<Ciudad> ciudadesEstado(String nombrePais, String nombreEstado);

    /**
     *
     * Lista las ciudades pertenecientes a un estado de un país por id.
     * @param paisId Long id del país.
     * @param estadoId Long id del estado.
     * @return List listado de ciudades del estado requerido.
     */
    List<Ciudad> ciudadesEstado(Long paisId, Long estadoId);
}
