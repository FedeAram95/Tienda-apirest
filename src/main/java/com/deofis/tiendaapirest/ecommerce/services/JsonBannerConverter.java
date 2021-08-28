package com.deofis.tiendaapirest.ecommerce.services;

import com.deofis.tiendaapirest.ecommerce.dto.BannerDto;

/**
 * Servicio que se encarga de convertir {@link BannerDto} en Json.
 */
public interface JsonBannerConverter {

    /**
     * Convierte {@link BannerDto} en objeto Json.
     * @param bannerDto String del Banner dto a convertir.
     * @return BannerDto convertido en Json.
     */
    BannerDto getJsonFromDto(String bannerDto);
}
