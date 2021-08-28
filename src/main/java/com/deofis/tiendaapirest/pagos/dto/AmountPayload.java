package com.deofis.tiendaapirest.pagos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AmountPayload {

    private String totalBruto;
    private String totalNeto;
    private String fee;
}
