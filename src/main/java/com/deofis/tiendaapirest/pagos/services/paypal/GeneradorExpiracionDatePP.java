package com.deofis.tiendaapirest.pagos.services.paypal;

import com.deofis.tiendaapirest.pagos.config.ExpiracionPagoConstants;
import com.deofis.tiendaapirest.pagos.services.GeneradorExpiracionDate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * Implementación para generar expiration date para el pago con la API de PayPal (payPal).
 */
@Service
@Qualifier("pp")
public class GeneradorExpiracionDatePP implements GeneradorExpiracionDate {

    @Override
    public Date expirationDate() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, ExpiracionPagoConstants.EXPIRACION_PP_DATE);

        date = calendar.getTime();
        return date;
    }
}
