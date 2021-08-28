package com.deofis.tiendaapirest.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BannerDto {
    private String actionUrl;
    private Integer orden;
}
