package com.deofis.tiendaapirest.pagos.services.cash;

import com.deofis.tiendaapirest.pagos.config.ExpiracionPagoConstants;
import com.deofis.tiendaapirest.pagos.services.GeneradorExpiracionDate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * Implementación para generar expiration date para el pago en efectivo (cash).
 */
@Service
@Qualifier("cash")
public class GeneradorExpiracionDateCash implements GeneradorExpiracionDate {

    @Override
    public Date expirationDate() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, ExpiracionPagoConstants.EXPIRACION_CASH_DATE);

        date = calendar.getTime();
        return date;
    }
}
