package com.deofis.tiendaapirest.autenticacion.exceptions;

public class MailSenderException extends RuntimeException {
    public MailSenderException(String exMensaje) {
        super(exMensaje);
    }
}
