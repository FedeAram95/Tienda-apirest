package com.deofis.tiendaapirest.catalogo.controllers.catalogoadmin.skus;

import com.deofis.tiendaapirest.catalogo.productos.exceptions.ProductoException;
import com.deofis.tiendaapirest.catalogo.skus.entities.Sku;
import com.deofis.tiendaapirest.catalogo.skus.exceptions.SkuException;
import com.deofis.tiendaapirest.catalogo.skus.services.actualizador.ActualizadorMasivoService;
import com.deofis.tiendaapirest.catalogo.skus.services.archivoshandler.SkuExcelHandler;
import com.deofis.tiendaapirest.catalogo.skus.services.validadores.ValidadorExtensionExcel;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/catalogo")
@AllArgsConstructor
public class ActualizarPreciosDisponibilidadMasivoController {

    private final ActualizadorMasivoService actualizadorMasivoService;

    /**
     * API para actualizar de forma masiva los precios de Skus a través de un archivo
     * EXCEL, con los precios actualizados.
     * URL: ~/api/catalogo/productos/skus/precios/excel
     * HttpMethod: POST
     * HttpStatus: OK
     * @param file MultipartFile con el archivo EXCEL.
     * @return ResponseEntity con listado de skus actualizados.
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("/productos/skus/actualizar/excel")
    public ResponseEntity<?> actualizarPreciosSkusExcel(@RequestParam(name = "file")MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        ValidadorExtensionExcel validadorExtensionExcel = new ValidadorExtensionExcel();
        List<Sku> skusActualizados;

        if (!validadorExtensionExcel.validar(file)) {
            response.put("mensaje", "Error al actualizar precios/disponibilidad con excel");
            response.put("error", "El archivo que intenta importar no es formato EXCEL");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        try {
            skusActualizados = this.actualizadorMasivoService.actualizarPreciosDisponibilidadSkus(new SkuExcelHandler(), file);
        } catch (ProductoException | SkuException e) {
            response.put("mensaje", "Error al actualizar precios y disponibilidad de skus con excel");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("skus", skusActualizados);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
