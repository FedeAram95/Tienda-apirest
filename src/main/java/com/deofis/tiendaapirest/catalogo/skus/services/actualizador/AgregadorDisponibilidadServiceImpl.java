package com.deofis.tiendaapirest.catalogo.skus.services.actualizador;

import com.deofis.tiendaapirest.catalogo.skus.entities.Sku;
import com.deofis.tiendaapirest.catalogo.skus.exceptions.SkuException;
import com.deofis.tiendaapirest.catalogo.skus.services.SkuService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AgregadorDisponibilidadServiceImpl implements AgregadorDisponibilidadService {

    private final SkuService skuService;

    @Override
    public Sku agregarDisponibilidad(Long skuId, Integer disponibilidadAgregada) {
        if (disponibilidadAgregada <= 0)
            throw new SkuException("La disponibilidad a agregar no puede ser menor o igual a 0");

        Sku skuActual = this.skuService.findById(skuId);
        Integer dispActual = skuActual.getDisponibilidad();

        skuActual.setDisponibilidad(dispActual + disponibilidadAgregada);
        return this.skuService.save(skuActual);
    }
}
