package com.deofis.tiendaapirest.utils.rounding;

/**
 * Servicio que se encarga del round/truncate de números. Útil para los precios/precios oferta de
 * productos y los montos subtotales/totales del carrito/operación.
 */
public interface RoundService {

    /**
     * Redondea un Double en una escala de 2 dígitos, y redondea hacia ARRIBA.
     * Ej.: 15.99 --> 16.00; 15.169897 --> 15.17.
     * @param number Double número a redondear.
     * @return Double número redondeado.
     */
    Double round(Double number);

    /**
     * Trunca un Double en una escala de 2 dígitos, es decir, recibe un número y
     * deja 2 decimales.
     * Ej.: 15.99 --> 15.99; 15.169897 --> 15.16.
     * @param number Double número a truncar.
     * @return Double número truncado
     */
    Double truncate(Double number);
}
