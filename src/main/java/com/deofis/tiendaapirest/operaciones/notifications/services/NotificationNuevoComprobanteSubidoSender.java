package com.deofis.tiendaapirest.operaciones.notifications.services;

import com.deofis.tiendaapirest.notificaciones.dto.Notification;
import com.deofis.tiendaapirest.notificaciones.dto.NotificationConstants;
import com.deofis.tiendaapirest.notificaciones.services.NotificationSender;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import org.springframework.stereotype.Service;

@Service(value = "nuevoComprobanteSubidoSender")
public class NotificationNuevoComprobanteSubidoSender extends NotificationSender {

    public NotificationNuevoComprobanteSubidoSender(String magicBellApiKey, String magicBellApiSecret) {
        super(magicBellApiKey, magicBellApiSecret);
    }

    @Override
    public Notification doBuild(String title, Object helper, String actionUrl, String user) {
        Operacion operacion = (Operacion) helper;
        Long nroOperacion = operacion.getNroOperacion();

        String cliente = operacion.getCliente().getApellido() + ", " +
                operacion.getCliente().getNombre();
        String builderContent = "Se registró un nuevo comprobante de pago para la operación N° " +
                nroOperacion + " del cliente " + cliente;
        return Notification.builder()
                .title(title)
                .content(builderContent)
                .category(NotificationConstants.CATEGORY_NEW_MESSAGE)
                .actionUrl(actionUrl)
                .user(user).build();
    }
}
