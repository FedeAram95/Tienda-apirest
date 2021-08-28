package com.deofis.tiendaapirest.pagos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentPayload {

    private String orderId;
    private String status;
    private PayerPayload payer;
    private AmountPayload amount;
}
