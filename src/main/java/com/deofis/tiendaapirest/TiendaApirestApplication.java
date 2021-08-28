package com.deofis.tiendaapirest;

import com.deofis.tiendaapirest.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@Import(SwaggerConfiguration.class)
@EnableConfigurationProperties
public class TiendaApirestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TiendaApirestApplication.class, args);
    }

}
