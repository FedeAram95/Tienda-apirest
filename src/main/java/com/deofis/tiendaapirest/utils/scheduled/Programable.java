package com.deofis.tiendaapirest.utils.scheduled;

/**
 * Interfaz para indicar que una clase puede tener ejecución de tareas programables, es decir,
 * que se ejecuten asíncronamente con el paso del tiempo, ya sea, indicando un fixedRate, o
 * una función Cron sobre cuando correr.
 * <br>
 * Todas las clases que implementen programable deberán implementar execute(), marcando dicho
 * método como @Scheduled.
 */
public interface Programable {

    /**
     * Ejecuta un algoritmo/proceso de manera programada, indicandole un fixedRate, o una
     * función cron.
     */
    void execute();
}
