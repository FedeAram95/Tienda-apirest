package com.deofis.tiendaapirest.pagos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderPayload {

    private String id;
    private String status;
    private String approveUrl;
}
