package com.deofis.tiendaapirest.emails.dto;

public interface NotificacionEmail {

    String getSubject();

    String getRecipient();

    Object getBody();

    String getRedirectUrl();
}
