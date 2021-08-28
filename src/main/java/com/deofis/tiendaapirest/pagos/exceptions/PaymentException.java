package com.deofis.tiendaapirest.pagos.exceptions;

public class PaymentException extends RuntimeException {
    public PaymentException(String exMensaje) {
        super(exMensaje);
    }
}
