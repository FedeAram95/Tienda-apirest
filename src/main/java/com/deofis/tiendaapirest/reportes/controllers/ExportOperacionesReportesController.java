package com.deofis.tiendaapirest.reportes.controllers;

import com.deofis.tiendaapirest.reportes.services.ExportOperacionReporteService;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Deprecated
public class ExportOperacionesReportesController {

    private final ExportOperacionReporteService exportOperacionReporteService;

    @GetMapping("/reportes/operaciones/operaciones.pdf")
    public ResponseEntity<?> generarReportePDF() {
        Map<String, Object> response = new HashMap<>();
        byte[] bytes;

        try {
            bytes = this.exportOperacionReporteService.generarReportPDF();
        } catch (FileNotFoundException | JRException e) {
            response.put("mensaje", "Error al generar el reporte en PDF");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity
                .ok()
                .header("Content-Type", "application/pdf; charset=UTF-8")
                .header("Content-Disposition", "attachment; filename=\"" + "operaciones.pdf\"")
                .body(bytes);
    }

    @GetMapping("/reportes/operaciones/operaciones.excel")
    public ResponseEntity<?> generarReporteEXCEL() {
        Map<String, Object> response = new HashMap<>();
        ByteArrayInputStream bytes;

        try {
            bytes = this.exportOperacionReporteService.generarReportEXCEL();
        } catch (IOException | JRException e) {
            response.put("mensaje", "Error al generar el reporte en EXCEL");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity
                .ok()
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8")
                .header("Content-Disposition", "attachment;filename=operaciones.xlsx")
                .body(new InputStreamResource(bytes));
    }

}
