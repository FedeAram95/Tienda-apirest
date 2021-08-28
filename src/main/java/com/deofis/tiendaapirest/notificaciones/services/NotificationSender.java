package com.deofis.tiendaapirest.notificaciones.services;

import com.deofis.tiendaapirest.notificaciones.dto.Notification;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase que se encarga de enviar notificaciones, según el evento que lo requiera. Por ej.: al registra
 * nueva operación.
 * <br>
 * La implementación actual de este servicio utiliza una API de notificaciones llamada
 * MagicBell, que provee servicio de notificaciones instantaneas para utilizar, que
 * relaciona a usuarios con sus notificaciones.
 * <br>
 * Esta clase es abstracta, que implementa el patrón Template Method: tenemos un método concreto
 * que denota el algoritmo para el envío de notificaciones => sendNotification().
 * Tenemos un método abstracto primitivo, que debe ser implementado por cada subclase, que se encarga
 * de la creación del objeto Notification, con los datos de la misma => doBuild ().
 */
public abstract class NotificationSender {

    private final Logger log = LoggerFactory.getLogger(NotificationSender.class);

    private final String magicBellApiKey;
    private final String magicBellApiSecret;

    @Autowired
    public NotificationSender(String magicBellApiKey, String magicBellApiSecret) {
        this.magicBellApiKey = magicBellApiKey;
        this.magicBellApiSecret = magicBellApiSecret;
    }

    @Async
    public void sendNotification(String title, Object helper, String actionUrl, String user) {
        Notification notification = doBuild(title, helper, actionUrl, user);

        // Map con los datos de la notificación...
        Map<String, Object> data = new HashMap<>();
        data.put("title", notification.getTitle());
        data.put("content", notification.getContent());
        data.put("category", notification.getCategory());
        data.put("action_url", notification.getActionUrl());
        data.put("recipient", Map.of("email", notification.getUser()));

        // POST aa la API. Guardamos la response por si necesitamos...
        HttpResponse<JsonNode> response = Unirest.post("https://api.magicbell.io/notifications")
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .header("X-MAGICBELL-API-KEY", magicBellApiKey)
                .header("X-MAGICBELL-API-SECRET", magicBellApiSecret)
                .body(Map.of("notification", data)).asJson();

        log.info(String.valueOf(response.getBody()));
    }

    public abstract Notification doBuild(String title, Object helper, String actionUrl, String user);
}
