package com.deofis.tiendaapirest.operaciones.notifications;

/**
 * Contiene ENUMS que se utilizan para instanciar  las distintas implementaciones
 * de {@link com.deofis.tiendaapirest.notificaciones.services.NotificationSender}
 */
public enum NotificationSenderEnum {
    nuevaOperacionSender,
    pagoRegistradoSender,
    ventaSender,
    operacionEnviadaSender,
    operacionEntregadaSender,
    nuevaCompraCashSender,
    nuevaVentaCashSender,
    anularOperacionSender,
    cancelarOperacionSender,
    comprobarPagoSender,
    nuevoComprobanteSubidoSender,
    confirmarComprobanteSender,
    rechazarComprobanteSender,
    devolverOperacionSender,
    nuevaDevolucionSender,
    confirmarDevolucionSender
}
