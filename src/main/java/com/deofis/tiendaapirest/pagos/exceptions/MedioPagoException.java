package com.deofis.tiendaapirest.pagos.exceptions;

public class MedioPagoException extends RuntimeException {
    public MedioPagoException(String exMensaje) {
        super(exMensaje);
    }
}
