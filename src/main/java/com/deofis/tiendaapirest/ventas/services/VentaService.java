package com.deofis.tiendaapirest.ventas.services;

import com.deofis.tiendaapirest.imagenes.entities.Imagen;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import com.deofis.tiendaapirest.ventas.dto.VentaPayload;

import java.util.Date;
import java.util.List;

/**
 * Este servicio se encarga de la lógica para el manejo de {@link Operacion}es desde el lado
 * del administrador del sistema, es decir, viendo las {@link Operacion}es como VENTAS.
 */
public interface VentaService {

    /**
     * Como administrador quiero obtener un listado con todas las ventas que fueron realizadas
     * en el sistema.
     * @return List con las ventas registradas, ordenadas por fecha.
     */
    List<VentaPayload> listarVentas();

    /**
     * Obtiene todas las ventas que se encuentren dentro de las fechas requeridas.
     * @param fechaDesde Date fecha inicio.
     * @param fechaHasta Date fecha fin.
     * @return {@link List<VentaPayload>} en la fechas requeridas.
     */
    List<VentaPayload> ventasFecha(Date fechaDesde, Date fechaHasta);

    /**
     * Obtiene las ventas que se encuentran en un estado requerido.
     * @param estado String estado solicitado.
     * @return {@link List<VentaPayload>} en el estado requerido.
     */
    List<VentaPayload> ventasEstado(String estado);

    /**
     * Obtiene las ventas que se encuentran en un rango de fechas requerido, y un estado.
     * @param estado String estado.
     * @param fechaDesde Date fecha inicio.
     * @param fechaHasta Date fehca fin.
     * @return {@link List<VentaPayload>} listado de ventas encontradas.
     */
    List<VentaPayload> ventasFechaYEstado(String estado, Date fechaDesde, Date fechaHasta);

    /**
     * Como administrador quiero ver una venta en particular.
     * @param nroOperacion Long numero de operacion correspondiente a ver.
     * @return Operacion solicitada.
     */
    VentaPayload obtenerVenta(Long nroOperacion);

    /**
     * Obtiene la imagen del comprobante para una operación requerida.
     * @param nroOperacion Long numero de operación a obtener el comprobante de su pago.
     * @return {@link Imagen} del comprobante.
     */
    Imagen obtenerComprobantePagoVenta(Long nroOperacion);
}
