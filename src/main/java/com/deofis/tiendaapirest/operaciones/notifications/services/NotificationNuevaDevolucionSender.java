package com.deofis.tiendaapirest.operaciones.notifications.services;

import com.deofis.tiendaapirest.notificaciones.dto.Notification;
import com.deofis.tiendaapirest.notificaciones.dto.NotificationConstants;
import com.deofis.tiendaapirest.notificaciones.services.NotificationSender;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import org.springframework.stereotype.Service;

/**
 * Notificación de devolución de una operación enviada al admin.
 */
@Service(value = "nuevaDevolucionSender")
public class NotificationNuevaDevolucionSender extends NotificationSender {

    public NotificationNuevaDevolucionSender(String magicBellApiKey, String magicBellApiSecret) {
        super(magicBellApiKey, magicBellApiSecret);
    }

    @Override
    public Notification doBuild(String title, Object helper, String actionUrl, String user) {
        Operacion operacion = (Operacion) helper;
        Long nroOperacion = operacion.getNroOperacion();
        String cliente = operacion.getCliente().getApellido() + ", " +
                operacion.getCliente().getNombre();
        String content = "El cliente " + cliente + " comenzó el proceso de devolución para la " +
                "operación N° " + nroOperacion;

        return Notification.builder()
                .title(title)
                .content(content)
                .category(NotificationConstants.CATEGORY_NEW_MESSAGE)
                .actionUrl(actionUrl)
                .user(user).build();
    }
}
