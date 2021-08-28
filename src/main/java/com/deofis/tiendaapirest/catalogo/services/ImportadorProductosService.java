package com.deofis.tiendaapirest.catalogo.services;

import com.deofis.tiendaapirest.catalogo.productos.entities.Producto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Esta clase se encarga de importar Productos a partir de distintos archivos. Este
 * servicio utiliza como ayuda la clase ProductoArchivosHandler, que se encarga
 * de recibir los archivos y convertirlos.
 */
@Deprecated
public interface ImportadorProductosService {

    /**
     *  Importa una lista de productos a partir de un archivo .csv.
     * @param archivo MultipartFile archivo CSV.
     * @return List listado de productos guardados.
     */
    List<Producto> importarDeCSV(MultipartFile archivo);

    /**
     * Importa una lista de productos a partir de un archivo .xlsx.
     * @param archivo MultipartFile archivo Excel.
     * @return List listado de productos guardados.
     */
    List<Producto> importarDeExcel(MultipartFile archivo);

    /**
     * Actualiza el stock de un listado de productos a partir de un archivo .xlsx.
     * @param archivo MultipartFile archivo Excel.
     * @return List listado de productos con stock actuzliado.
     */
    List<Producto> actualizarStockDeExcel(MultipartFile archivo);
}
