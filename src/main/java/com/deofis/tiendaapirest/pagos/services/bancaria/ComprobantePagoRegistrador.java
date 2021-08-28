package com.deofis.tiendaapirest.pagos.services.bancaria;

import com.deofis.tiendaapirest.pagos.entities.OperacionPago;
import org.springframework.web.multipart.MultipartFile;

/**
 * Se encarga de registrar el comprobante de pago para una {@link OperacionPago}.
 */
public interface ComprobantePagoRegistrador {

    /**
     * Registra y guarda el comprobante de pago para un pago requerido.
     * @param comprobante {@link MultipartFile} archivo del comprobante a subir.
     * @param pago {@link OperacionPago} a vincular el comprobante.
     * @return {@link OperacionPago} actualizado, con el comprobante.
     */
    OperacionPago registrarComprobantePago(MultipartFile comprobante, OperacionPago pago);
}
