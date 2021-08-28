package com.deofis.tiendaapirest.emails.services;

import com.deofis.tiendaapirest.emails.dto.NotificacionEmail;

/**
 * Clase servicio para el envío de distintos mails, dependiento del tópico necesario. Por cada tipo de email necesario,
 * crear una clase que implemente este servicio para el envío de email requerido.
 */
public interface MailService {

    /**
     * Envía un email al destinatario con la información requerida.
     * @param notificacionEmail {@link NotificacionEmail} con los datos para completar el email.
     */
    void sendEmail(NotificacionEmail notificacionEmail);
}
