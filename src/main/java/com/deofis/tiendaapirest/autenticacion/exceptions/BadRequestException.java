package com.deofis.tiendaapirest.autenticacion.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    public BadRequestException(String exMensaje) {
        super(exMensaje);
    }

    public BadRequestException(String exMensaje, Throwable causa) {
        super(exMensaje, causa);
    }
}
