package com.deofis.tiendaapirest.reportes.controllers;

import com.deofis.tiendaapirest.catalogo.skus.exceptions.SkuException;
import com.deofis.tiendaapirest.operaciones.exceptions.OperacionException;
import com.deofis.tiendaapirest.reportes.services.ReporteVentasService;
import com.deofis.tiendaapirest.ventas.dto.VentaPayload;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API para la generación de reportes de ventas para la muestra web/mobile.
 * <br>
 * NOTA: La API de generación de reportes con salida de archivos es otra.
 */
@RestController
@RequestMapping("/api/reportes")
@AllArgsConstructor
@Slf4j
public class ReporteVentasController {

    private final ReporteVentasService reporteVentasService;

    /**
     * Obtener un listado de todas las operaciones (ventas) registradas en el sistema.
     * URL: ~/api/reportes/ventas
     * HttpMethod: GET
     * HttpStatus: OK
     * @return ResponseEntity List de todas las operaciones ordenadas por fecha.
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("/ventas")
    public ResponseEntity<?> reporteVentas(@RequestParam(name = "estado", required = false) String estado,
                                          @RequestParam(name = "fechaDesde", required = false)
                                               @DateTimeFormat(pattern = "dd/MM/yyyy") Date fechaDesde,
                                          @RequestParam(name = "fechaHasta", required = false)
                                               @DateTimeFormat(pattern = "dd/MM/yyyy") Date fechaHasta) {
        Map<String, Object> response = new HashMap<>();
        List<VentaPayload> ventas;
        Double montoTotal;

        log.info(estado);
        log.info(String.valueOf(fechaDesde));
        log.info(String.valueOf(fechaHasta));

        if (fechaDesde != null && fechaHasta == null) {
            response.put("mensaje", "Error al generar reporte de ventas");
            response.put("error", "Si desea limitar el reporte por fechas, debe ingresar ambas (desde y hasta)");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            ventas = this.reporteVentasService.generarReporteVentas(estado, fechaDesde, fechaHasta);
        } catch (OperacionException e) {
            response.put("mensaje", "Error al generar reporte de ventas");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (ventas == null) {
            response.put("mensaje", "Error al obtener el listado de ventas");
            response.put("error", "El estado solicitado no existe, o ha sido tipeado de manera equivocada");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        montoTotal = 0.00;
        for (VentaPayload venta: ventas) {
            montoTotal += venta.getTotal();
        }

        response.put("totalVentas", ventas.size());
        response.put("montoTotal", montoTotal);
        response.put("estado", estado);
        response.put("fechaDesde", fechaDesde);
        response.put("fechaHasta", fechaHasta);
        response.put("ventas", ventas);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * API para generar reporte del total de items (skus) vendidos en todas las
     * ventas registradas en el sistema.
     * URL: ~/api/reportes/ventas/items
     * HttpMethod: GET
     * HttpStatus: OK
     * @param fechaDesde Date fecha desde del reporte.
     * @param fechaHasta Date fecha hasta del reporte.
     * @return ResponseEntity con el reporte de los items vendidos.
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("/ventas/items")
    public ResponseEntity<?> reporteVentasItems(@RequestParam(name = "fechaDesde")
                                                    @DateTimeFormat(pattern = "dd/MM/yyyy") Date fechaDesde,
                                                @RequestParam(name = "fechaHasta")
                                                    @DateTimeFormat(pattern = "dd/MM/yyyy") Date fechaHasta) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> reporte;

        log.info(fechaDesde.toString());
        log.info(fechaHasta.toString());

        try {
            reporte = this.reporteVentasService.generarReporteVentasItemsTotales(fechaDesde, fechaHasta);
        } catch (OperacionException | SkuException e) {
            response.put("mensaje", "Error al generar reporte");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("reporte", reporte);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
