package com.deofis.tiendaapirest.pagos.services;

import com.deofis.tiendaapirest.checkout.services.CheckoutService;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import com.deofis.tiendaapirest.pagos.entities.OperacionPago;
import com.deofis.tiendaapirest.pagos.mapper.OperacionPagoMapper;
import com.deofis.tiendaapirest.pagos.repositories.OperacionPagoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@AllArgsConstructor
public class PagoHandlerImpl implements PagoHandler {

    private final CheckoutService checkoutService;
    private final OperacionPagoMapper operacionPagoMapper;
    private final OperacionPagoRepository pagoRepository;

    @Transactional
    @Override
    public OperacionPago procesarPago(Operacion operacion) {
        OperacionPago pagoManejado;
        OperacionPago pagoActual = operacion.getPago();
        if (pagoActual.isExpired()) {
            this.pagoRepository.delete(pagoActual);
            pagoManejado = this.operacionPagoMapper.mapToOperacionPago(
                    this.checkoutService.iniciarCheckout(operacion));
            pagoManejado.setFechaCreacion(new Date());
            return pagoManejado;
        }

        return pagoActual;
    }
}
