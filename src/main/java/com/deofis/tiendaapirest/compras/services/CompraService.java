package com.deofis.tiendaapirest.compras.services;

import com.deofis.tiendaapirest.compras.dto.CompraPayload;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;

import java.util.List;

/**
 * Este servicio se encarga de la lógica para el manejo de {@link Operacion}es desde el lado
 * del usuario comprador de productos del sistema, es decir, viendo las {@link Operacion}es como COMPRAS.
 */
public interface CompraService {

    /**
     * Obtener un listado con el historial de compras del cliente del perfil ordenado por fecha de
     * menor a mayor.
     * @return {@link List<CompraPayload>} historial de compras del usuario.
     */
    List<CompraPayload> historialCompras();

    /**
     * Obtiene un listado con todas las compras que cumplen con cierto estado requerido por el usuario.
     * @param estado String estado requerido.
     * @return {@link List<CompraPayload>} con las compras en dicho estado.
     */
    List<CompraPayload> comprasEstado(String estado);

    /**
     * Obtiene un listado con las compras del usuario realizadas en el año requerido.
     * @param year Integer año solicitado.
     * @return {@link List<CompraPayload>} listado de compras del usuario actual en el año solicitada.
     */
    List<CompraPayload> comprasYear(Integer year);

    /**
     * Obtiene un listado con las compras del usuario realizadas en el mes requerido del año actual.
     * @param month Integer mes solicitado.
     * @return {@link List<CompraPayload>} listado de compras del usuario actual en el mes solicitado.
     */
    List<CompraPayload> comprasMonth(Integer month);

    /**
     * Obtiene una compra del usuario logueado, a través de su número de operación.
     * @param nroOperacion Long numero de operación.
     * @return {@link CompraPayload} con los datos de la operación.
     */
    CompraPayload verCompra(Long nroOperacion);

}
