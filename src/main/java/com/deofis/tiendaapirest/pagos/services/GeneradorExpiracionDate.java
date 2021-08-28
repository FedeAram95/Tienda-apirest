package com.deofis.tiendaapirest.pagos.services;

import java.util.Date;

/**
 * Esta clase se encarga de generar la fecha de expiración de un pago. Tendrá tantas realizaciones
 * como medios de pago existentes.
 */
public interface GeneradorExpiracionDate {

    Date expirationDate();
}
