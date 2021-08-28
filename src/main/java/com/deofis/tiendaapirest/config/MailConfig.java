package com.deofis.tiendaapirest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "mail")
@Component
@Data
public class MailConfig {
    private String MAIL_FROM;

    @Bean
    public String mailFrom() {
        return this.MAIL_FROM;
    }
}
