package com.deofis.tiendaapirest.pagos.services.mercadopago;

import com.deofis.tiendaapirest.operaciones.entities.DetalleOperacion;
import com.deofis.tiendaapirest.operaciones.entities.EstadoOperacion;
import com.deofis.tiendaapirest.operaciones.entities.EventoOperacion;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import com.deofis.tiendaapirest.operaciones.statemachine.StateMachineService;
import com.deofis.tiendaapirest.pagos.dto.AmountPayload;
import com.deofis.tiendaapirest.pagos.dto.PayerPayload;
import com.deofis.tiendaapirest.pagos.exceptions.PaymentException;
import com.deofis.tiendaapirest.pagos.factory.OperacionPagoInfo;
import com.deofis.tiendaapirest.pagos.factory.OperacionPagoInfoFactory;
import com.deofis.tiendaapirest.pagos.services.GeneradorExpiracionDate;
import com.deofis.tiendaapirest.pagos.services.strategy.PagoStrategy;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Payment;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.BackUrls;
import com.mercadopago.resources.datastructures.preference.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

/**
 * Implementación de {@link PagoStrategy} que realiza el pago conectándose a la API de Mercado Pago.
 */
@Service
@Slf4j
public class MercadoPagoStrategy implements PagoStrategy {

    private final GeneradorExpiracionDate generadorExpiracionDate;
    private final StateMachineService stateMachineService;

    private final String baseUrl;

    @Autowired
    public MercadoPagoStrategy(@Qualifier("mp") GeneradorExpiracionDate generadorExpiracionDate,
                               StateMachineService stateMachineService,
                               String baseUrl) {
        this.generadorExpiracionDate = generadorExpiracionDate;
        this.stateMachineService = stateMachineService;
        this.baseUrl = baseUrl;
    }

    @Override
    public OperacionPagoInfo crearPago(Operacion operacion) {
        Preference preference = this.buildPreferenceBody(operacion);
        Map<String, Object> atributosPago = new HashMap<>();

        Date expiresIn = this.generadorExpiracionDate.expirationDate();

        Preference response;
        try {
            response = preference.save();
            log.info("Approve url --> " + response.getInitPoint());

            atributosPago.put("preferenceId", response.getId());
            atributosPago.put("nroOperacion", operacion.getNroOperacion());
            atributosPago.put("status", "CREATED");
            atributosPago.put("expiresIn", expiresIn);
            atributosPago.put("initPoint", response.getInitPoint());
            atributosPago.put("amount", null);
            atributosPago.put("payer", null);
        } catch (MPException e) {
            throw new PaymentException("Error al crear el pago con Mercado Pago: " +
                    e.getMessage());
        }

        log.info("Pago con MP creado con éxito");
        return OperacionPagoInfoFactory
                .getOperacionPagoInfo(String.valueOf(operacion.getMedioPago().getNombre()), atributosPago);
    }

    @Override
    public OperacionPagoInfo completarPago(Operacion operacion, String paymentId, String preferenceId) {

        // Si el pago ya fue efectuado, tiramos excepción
        if (operacion.getPago().getStatus().equalsIgnoreCase("approved"))
            throw new PaymentException("El pago para esta operación ya fue completado");

        Map<String, Object> atributosPago = new HashMap<>();
        Payment payment;

        try {
            payment = Payment.findById(paymentId);
        } catch (MPException e) {
            throw new PaymentException("Error al completar el pago con Mercado Pago: " +
                    e.getMessage());
        }

        if (payment.getStatus() == null) {
            throw new PaymentException("Error al completar el pago con Mercado Pago: " +
                    "No se encontró el pago con el id: " + paymentId);
        }

        if (preferenceId == null)
            throw new PaymentException("La preference_id es necesaria para hacer la validación del pago");

        // Si el payment pasado tiene distinto preferenceId del Pago creado al momento
        // de crear el pago para la operación, tiramos excepción
        if (!operacion.getPago().getId().equals(preferenceId)) {
            log.info("pago.preference_id --> " + operacion.getPago().getId());
            log.info("preference_id --> " + preferenceId);
            throw new PaymentException("El ID del pago solicitado no esta asociado al pago de la operación");
        }

        PayerPayload payer;
        if (payment.getPayer().getId() != null  && payment.getPayer().getFirstName() != null) {
            payer = PayerPayload.builder()
                    .payerId(payment.getPayer().getId())
                    .payerEmail(payment.getPayer().getEmail())
                    .payerFullName(payment.getPayer().getLastName().concat(" ")
                            .concat(payment.getPayer().getFirstName())).build();
        } else {
            payer = PayerPayload.builder()
                    .payerId(operacion.getCliente().getId().toString())
                    .payerEmail(operacion.getCliente().getEmail())
                    .payerFullName(operacion.getCliente().getApellido().concat(" ")
                            .concat(operacion.getCliente().getNombre())).build();
        }

        AmountPayload amount = AmountPayload.builder()
                .totalBruto(String.valueOf(payment.getTransactionDetails().getTotalPaidAmount()))
                .totalNeto(String.valueOf(payment.getTransactionDetails().getNetReceivedAmount()))
                .fee(String.valueOf(payment.getFeeDetails().get(0).getAmount())).build();

        atributosPago.put("preferenceId", operacion.getPago().getId());
        atributosPago.put("nroOperacion", operacion.getNroOperacion());
        atributosPago.put("status", String.valueOf(payment.getStatus()));
        atributosPago.put("payer", payer);
        atributosPago.put("amount", amount);

        // Lógica transición de estados al completar el checkout (pago)
        this.transitarEstado(operacion);

        log.info("payer --> " + payer.toString());
        log.info("amount -->" + amount.toString());

        log.info("Pago completado con éxito");
        return OperacionPagoInfoFactory
                .getOperacionPagoInfo(String.valueOf(operacion.getMedioPago().getNombre()), atributosPago);
    }

    private Preference buildPreferenceBody(Operacion operacion) {
        Preference preference = new Preference();

        String targetCancelUrl = this.baseUrl.concat("/api/checkout/failure");
        String CANCEL_REDIRECT_URL = UriComponentsBuilder.fromUriString(targetCancelUrl)
                .queryParam("nroOperacion", operacion.getNroOperacion()).build().toString();
        String targetSuccessUrl = this.baseUrl.concat("/api/checkout/success");
        String APPROVED_REDIRECT_URL = UriComponentsBuilder.fromUriString(targetSuccessUrl)
                .queryParam("nroOperacion", operacion.getNroOperacion()).build().toString();

        preference.setBackUrls(new BackUrls()
                .setFailure(CANCEL_REDIRECT_URL)
                .setSuccess(APPROVED_REDIRECT_URL));

        List<DetalleOperacion> items = operacion.getItems();

        // Creamos array list con los items de Mercado Pago, y por cada
        // item de la operación, lo añadimos al array list de items MP.
        ArrayList<Item> mpItems = new ArrayList<>();

        for (DetalleOperacion item: items) {
            Item itemMp = new Item();

            itemMp.setId(String.valueOf(item.getId()))
                    .setTitle(item.getSku().getNombre())
                    .setQuantity(item.getCantidad())
                    .setUnitPrice(item.getPrecioVenta().floatValue());
            mpItems.add(itemMp);
        }

        preference.setItems(mpItems);
        preference.setAutoReturn(Preference.AutoReturn.all);

        return preference;
    }

    private void transitarEstado(Operacion operacion) {
        StateMachine<EstadoOperacion, EventoOperacion> sm = this.stateMachineService.build(operacion.getNroOperacion());
        sm.getExtendedState().getVariables().put("operacion", operacion);
        sm.getExtendedState().getVariables().put("useremail", operacion.getCliente().getEmail());
        this.stateMachineService.enviarEvento(operacion.getNroOperacion(), sm, EventoOperacion.CONFIRMAR_PAGO);
    }
}
