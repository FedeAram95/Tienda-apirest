package com.deofis.tiendaapirest.autenticacion.exceptions;

public class TokenException extends RuntimeException {
    public TokenException(String exMensaje) {
        super(exMensaje);
    }
}
