package com.deofis.tiendaapirest.ecommerce.services;

import com.deofis.tiendaapirest.ecommerce.dto.BannerDto;
import com.deofis.tiendaapirest.ecommerce.exceptions.BannerException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class JsonBannerConverterImpl implements JsonBannerConverter {

    @Override
    public BannerDto getJsonFromDto(String bannerDto) {
        BannerDto bannerJson;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            bannerJson = objectMapper.readValue(bannerDto, BannerDto.class);
        } catch (IOException e) {
            throw new BannerException("Error al convertir banner dto en json: ".concat(e.getMessage()));
        }

        return bannerJson;
    }
}
