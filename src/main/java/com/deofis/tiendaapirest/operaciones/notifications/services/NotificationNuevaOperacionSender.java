package com.deofis.tiendaapirest.operaciones.notifications.services;

import com.deofis.tiendaapirest.notificaciones.dto.Notification;
import com.deofis.tiendaapirest.notificaciones.dto.NotificationConstants;
import com.deofis.tiendaapirest.notificaciones.services.NotificationSender;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "nuevaOperacionSender")
public class NotificationNuevaOperacionSender extends NotificationSender {

    @Autowired
    public NotificationNuevaOperacionSender(String magicBellApiKey, String magicBellApiSecret, String clientUrl) {
        super(magicBellApiKey, magicBellApiSecret);
    }

    @Override
    public Notification doBuild(String title, Object helper, String actionUrl, String user) {
        Operacion operacion = (Operacion) helper;
        Long nroOperacion = operacion.getNroOperacion();
        String content = "¡Tu compra con N° " + nroOperacion + " se registró con éxito! Recordá completar\n" +
                "el pago si aún no lo hiciste.";

        return Notification.builder()
                .title(title)
                .content(content)
                .category(NotificationConstants.CATEGORY_NEW_MESSAGE)
                .actionUrl(actionUrl)
                .user(user).build();
    }
}
