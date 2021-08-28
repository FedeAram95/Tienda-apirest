package com.deofis.tiendaapirest.operaciones.notifications.services;

import com.deofis.tiendaapirest.notificaciones.dto.Notification;
import com.deofis.tiendaapirest.notificaciones.dto.NotificationConstants;
import com.deofis.tiendaapirest.notificaciones.services.NotificationSender;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import com.deofis.tiendaapirest.pagos.config.ExpiracionPagoConstants;
import org.springframework.stereotype.Service;

/**
 * Notificación para el registro de una nueva operación por medio de pago efectivo o
 * transferencia bancaria.
 * <br>
 * Sender orientado a usuarios compradores.
 */
@Service(value = "nuevaCompraCashSender")
public class NotificationNuevaCompraCashSender extends NotificationSender {

    public NotificationNuevaCompraCashSender(String magicBellApiKey, String magicBellApiSecret) {
        super(magicBellApiKey, magicBellApiSecret);
    }

    @Override
    public Notification doBuild(String title, Object helper, String actionUrl, String user) {
        Operacion operacion = (Operacion) helper;
        Long nroOperacion = operacion.getNroOperacion();
        int maxDiasCash = ExpiracionPagoConstants.EXPIRACION_CASH_DATE;
        int maxDiasTB = ExpiracionPagoConstants.EXPIRACION_TB_DATE;
        String content = "¡Tu compra con N° " + nroOperacion + " se registró con éxito! \n" +
                "Límite para retirar en tienda: " + maxDiasCash + " días. \n" +
                "Límite para completar transferencia: " + maxDiasTB + " días.";

        return Notification.builder()
                .title(title)
                .content(content)
                .category(NotificationConstants.CATEGORY_NEW_MESSAGE)
                .actionUrl(actionUrl)
                .user(user).build();
    }
}
