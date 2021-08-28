package com.deofis.tiendaapirest.operaciones.notifications.services;

import com.deofis.tiendaapirest.notificaciones.dto.Notification;
import com.deofis.tiendaapirest.notificaciones.dto.NotificationConstants;
import com.deofis.tiendaapirest.notificaciones.services.NotificationSender;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import org.springframework.stereotype.Service;

@Service(value = "anularOperacionSender")
public class NotificationAnularOperacionSender extends NotificationSender {

    public NotificationAnularOperacionSender(String magicBellApiKey, String magicBellApiSecret) {
        super(magicBellApiKey, magicBellApiSecret);
    }

    @Override
    public Notification doBuild(String title, Object helper, String actionUrl, String user) {
        Operacion operacion = (Operacion) helper;
        Long nroOperacion = operacion.getNroOperacion();
        String content = "Tu compra con N° " + nroOperacion + "fue anulada";

        return Notification.builder()
                .title(title)
                .content(content)
                .category(NotificationConstants.CATEGORY_NEW_MESSAGE)
                .actionUrl(actionUrl)
                .user(user).build();
    }
}
