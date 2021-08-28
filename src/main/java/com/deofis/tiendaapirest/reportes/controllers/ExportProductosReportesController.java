package com.deofis.tiendaapirest.reportes.controllers;

import com.deofis.tiendaapirest.reportes.services.ExportProductoReporteService;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Deprecated
public class ExportProductosReportesController {

    private final ExportProductoReporteService exportProductoReporteService;

    @GetMapping("/reportes/productos/productos.excel")
    public ResponseEntity<?> generarReporteListaProductosExcel() {
        Map<String, Object> response = new HashMap<>();
        ByteArrayInputStream stream;

        try {
            stream = this.exportProductoReporteService.generarReporteExcel();
        } catch (IOException | JRException e) {
            response.put("mensaje", "Error al generar el reporte del listado de productos en Excel");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity
                .ok()
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8")
                .header("Content-Disposition", "attachment;filename=productos.xlsx")
                .body(new InputStreamResource(stream));
    }

}
