package com.deofis.tiendaapirest.compras.services;

import com.deofis.tiendaapirest.compras.dto.CompraPayload;
import com.deofis.tiendaapirest.imagenes.entities.Imagen;
import org.springframework.web.multipart.MultipartFile;

/**
 * Servicio que utiliza el usuario para registrar el comprobante de pago para una transferencia
 * bancaria, subiendo una imagen con dicho comprobante.
 */
public interface ComprobantePagoService {

    /**
     * Sube imagen de comprobante y la registra para el pago de la operación requerida.
     * @param comprobante {@link MultipartFile} archivo con la imagen del comprobante.
     * @param nroOperacion Long numero de operación del pago a comprobar.
     * @return {@link CompraPayload} datos actualizados de la compra (operacion).
     */
    CompraPayload subirComprobantePago(MultipartFile comprobante, Long nroOperacion);

    /**
     * Obtiene la imagen vinculada al pago de una compra (operación) del cliente actual.
     * @param nroOperacion Long numero de operacion a obtener la imagen de su comprobante de pago.
     * @return {@link Imagen} comprobante del pago.
     */
    Imagen obtenerImagenComprobantePago(Long nroOperacion);
}
