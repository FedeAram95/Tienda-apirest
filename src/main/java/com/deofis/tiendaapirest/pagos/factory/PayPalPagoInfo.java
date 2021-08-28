package com.deofis.tiendaapirest.pagos.factory;

import com.deofis.tiendaapirest.pagos.dto.AmountPayload;
import com.deofis.tiendaapirest.pagos.dto.PayerPayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.Map;

@JsonIgnoreProperties({"atributos"})
public class PayPalPagoInfo extends OperacionPagoInfo {

    public PayPalPagoInfo(Map<String, Object> atributos) {
        super(atributos);
    }

    @Override
    public String getId() {
        return (String) atributos.get("orderId");
    }

    @Override
    public Long getNroOperacion() {
        return (Long) atributos.get("nroOperacion");
    }

    @Override
    public String getStatus() {
        return (String) atributos.get("status");
    }

    @Override
    public Date getExpiraEn() {
        return (Date) atributos.get("expiresIn");
    }

    @Override
    public String getApproveUrl() {
        return (String) atributos.get("approveUrl");
    }

    @Override
    public AmountPayload getAmount() {
        return (AmountPayload) atributos.get("amount");
    }

    @Override
    public PayerPayload getPayer() {
        return (PayerPayload) atributos.get("payer");
    }
}
