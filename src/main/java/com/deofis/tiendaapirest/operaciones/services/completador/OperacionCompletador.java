package com.deofis.tiendaapirest.operaciones.services.completador;

import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import com.deofis.tiendaapirest.utils.scheduled.Programable;

/**
 * Se encarga de completar, de manera programática, aquellas {@link Operacion}es en estado ENTREGADO
 * que, pasado cierto tiempo de su fecha de entrega, deben dar por terminado el ciclo de la operación.
 */
public interface OperacionCompletador extends Programable {

    /**
     * Completa la operacion requerida.
     * <br>
     * Pasado X cantidad de tiempo en estado ENTREGADO, la operación debe transitar al estado
     * COMPLETADO, evitando así que puedan devolverse operaciones pasado X tiempo.
     * @param operacion {@link Operacion} a completar.
     */
    void completarOperacion(Operacion operacion);

}
