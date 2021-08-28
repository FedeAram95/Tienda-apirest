package com.deofis.tiendaapirest.notificaciones.services.factory;

import com.deofis.tiendaapirest.notificaciones.services.NotificationSender;

public interface NotificationSenderFactory {

    NotificationSender get(String type);

}
