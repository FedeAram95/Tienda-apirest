package com.deofis.tiendaapirest.catalogo.skus.services.actualizador;

import com.deofis.tiendaapirest.catalogo.skus.dto.SkuDto;
import com.deofis.tiendaapirest.catalogo.skus.entities.Sku;
import com.deofis.tiendaapirest.catalogo.skus.services.SkuService;
import com.deofis.tiendaapirest.catalogo.skus.services.archivoshandler.SkuArchivosHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ActualizadorMasivoServiceImpl implements ActualizadorMasivoService {

    private final SkuService skuService;

    @Override
    public List<Sku> actualizarPreciosDisponibilidadSkus(SkuArchivosHandler skuArchivosHandler, MultipartFile file) {
        List<SkuDto> skuDtos = skuArchivosHandler.importar(file);
        log.info(skuDtos.toString());
        List<Sku> skusActualizados = new ArrayList<>();

        for (SkuDto skuDto: skuDtos) {
            Sku skuBD = this.skuService.obtenerSku(skuDto.getId());

            this.skuService.actualizarPrecio(skuBD.getId(), skuDto.getPrecio());
            this.skuService.actualizarDisponibilidad(skuBD.getId(), skuDto.getDisponibilidad());

            skusActualizados.add(skuBD);
        }

        return skusActualizados;
    }
}
