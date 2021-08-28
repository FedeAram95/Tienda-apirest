package com.deofis.tiendaapirest.reportes.controllers;

import com.deofis.tiendaapirest.catalogo.skus.exceptions.SkuException;
import com.deofis.tiendaapirest.operaciones.exceptions.OperacionException;
import com.deofis.tiendaapirest.reportes.services.ReporteVentasService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Deprecated
public class ReporteVentasControllerOld {

    private final ReporteVentasService reporteVentasService;

    @GetMapping("/reportes/ventas/skus/{skuId}")
    public ResponseEntity<?> generarReporteVentasSku(@PathVariable Long skuId) {
        return new ResponseEntity<>("En proceso de implementación...", HttpStatus.OK);
    }

    @GetMapping("/reportes/ventas/skus/{skuId}/entre")
    public ResponseEntity<?> generarReporteVentasSku(@PathVariable Long skuId,
                                                     @RequestParam Date fechaDesde,
                                                     @RequestParam Date fechaHasta) {
        return new ResponseEntity<>("En proceso de implementación...", HttpStatus.OK);
    }

    @GetMapping("/reportes/ventas/skus")
    public ResponseEntity<?> generarReporteVentasSkusTotales(@RequestParam Date fechaDesde,
                                                             @RequestParam Date fechaHasta) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> reporte;

        try {
            reporte = this.reporteVentasService.generarReporteVentasItemsTotales(fechaDesde, fechaHasta);
        } catch (OperacionException | SkuException e) {
            response.put("mensaje", "Error al generar el reporte");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("reporte", reporte);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
