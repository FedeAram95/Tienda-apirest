package com.deofis.tiendaapirest.ecommerce.services;

import com.deofis.tiendaapirest.ecommerce.entities.EcommerceConfig;
import com.deofis.tiendaapirest.ecommerce.repositories.EcommerceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EcommerceConfigServiceImpl implements EcommerceConfigService {

    private final EcommerceRepository ecommerceRepository;

    @Override
    public void cambiarColorNav(String color) {
        EcommerceConfig config = this.ecommerceRepository.getOne(1L);
        config.setColorNav(color);
    }

    @Override
    public void cambiarColorFondo(String color) {
        EcommerceConfig config = this.ecommerceRepository.getOne(1L);
        config.setColorFondo(color);
    }

    @Override
    public void cambiarLogo() {
        EcommerceConfig config = this.ecommerceRepository.getOne(1L);

    }
}
