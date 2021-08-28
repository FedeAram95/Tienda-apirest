package com.deofis.tiendaapirest.reportes.services;

import com.deofis.tiendaapirest.ventas.dto.VentaPayload;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Servicio que provee la lógica de negocio para generar los reportes de ventas de acuerdo a
 * distintos criterios.
 */
public interface ReporteVentasService {

    /**
     * Genera reporte de ventas registradas en el sistema, pudiendo recibir como parámetro: el
     * estado, y la fecha desde y fecha hasta para la generación del reporte de ventas.
     * @param estado String del estado.
     * @param fechaDesde Date de la fecha desde para el reporte.
     * @param fechaHasta Date de la fecha hasta para el reporte.
     * @return List con las ventas del reporte.
     */
    List<VentaPayload> generarReporteVentas(String estado, Date fechaDesde, Date fechaHasta);

    /**
     * Genera reporte de historial de ventas por Sku (desde primer venta hasta fecha actual).
     * Calcula cuantas unidades se vendieron de cierto SKU (producto vendible), y el monto total
     * que se generó con dicho SKU.
     * @param skuId Long id del SKU a generar el reporte.
     * @return Map con los datos del reporte.
     */
    Map<String, Object> generarReporteVentasSku(Long skuId);

    /**
     * Genera reporte de ventas por Sku en el rango de dos fechas. Calcula cuantas unidades se vendieron
     * de cierto SKU, y el monto total.
     * @param skuId Long id del SKU a generar el reporte.
     * @param fechaDesde Date fecha de inicio reporte.
     * @param fechaHasta Date fecha de fin reporte.
     * @return Map con los datos del reporte.
     */
    Map<String, Object> generarReporteVentasSku(Long skuId, Date fechaDesde, Date fechaHasta);

    /**
     * Genera reporte de ventas de items totales en el rango de dos fechas. Por cada SKU vendido, se
     * calcula la cantidad vendida y el monto total de ventas que generó cada SKU.
     * @param fechaDesde Date fecha de inicio reporte.
     * @param fechaHasta Date fecha de fin reporte.
     * @return Map con los datos del reporte.
     */
    Map<String, Object> generarReporteVentasItemsTotales(Date fechaDesde, Date fechaHasta);
}
