package com.deofis.tiendaapirest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "file.uploads")
@Configuration
@Data
public class UploadsConfig {

    private String UPLOADS_DIR;

    @Bean
    public String uploadsDir() {
        return this.UPLOADS_DIR;
    }

}
