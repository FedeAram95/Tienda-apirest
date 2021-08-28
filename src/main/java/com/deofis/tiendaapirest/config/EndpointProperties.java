package com.deofis.tiendaapirest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "endpoints")
@Configuration
@Data
public class EndpointProperties {

    private String baseUrl;
    private String clientUrl;

    @Bean
    public String baseUrl() {
        return this.baseUrl;
    }

    @Bean
    public String clientUrl() {
        return this.clientUrl;
    }

}
