package com.deofis.tiendaapirest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Clase de configuración para la API/SECRET Key de MagicBell (para el módulo de notificaciones)
 */
@ConfigurationProperties(prefix = "magicbell")
@Configuration
@Data
public class MagicBellConfig {

    private String apiKey;
    private String apiSecret;

    @Bean
    public String magicBellApiKey() {
        return this.apiKey;
    }

    @Bean
    public String magicBellApiSecret() {
        return this.apiSecret;
    }
}
