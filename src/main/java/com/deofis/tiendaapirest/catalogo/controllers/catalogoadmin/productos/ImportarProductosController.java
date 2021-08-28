package com.deofis.tiendaapirest.catalogo.controllers.catalogoadmin.productos;

import com.deofis.tiendaapirest.catalogo.productos.entities.Producto;
import com.deofis.tiendaapirest.catalogo.productos.exceptions.ProductoException;
import com.deofis.tiendaapirest.catalogo.services.ImportadorProductosService;
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
@RequestMapping("/api")
@AllArgsConstructor
@Deprecated
public class ImportarProductosController {

    private final ImportadorProductosService importadorProductosService;

    /**
     * Importa un listado de productos nuevos y los guarda en la base de datos a partir de un archivo .csv.
     * URL: ~/api/productos/importar-csv
     * HttpMethod: POST
     * HttpStatus: CREATED
     * @param archivo @RequestParam MultipartFile archivo en formato CSV a importar.
     * @return ResponseEntity List con el listado de productos importados guardados.
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("/productos/importar-csv")
    public ResponseEntity<?> importarProductosDeCSV(@RequestParam MultipartFile archivo) {
        Map<String, Object> response = new HashMap<>();
        List<Producto> productos;

        try {
            productos = this.importadorProductosService.importarDeCSV(archivo);
        } catch (ProductoException e) {
            response.put("mensaje", "Error al importar archivo CSV");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("productos", productos);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Importa un listado de productos nuevos y los guarda en la base de datos a partir de un archivo .xlsx.
     * URL: ~/api/productos/importar-excel
     * HttpMethod: POST
     * HttpStatus: CREATED
     * @param archivo @RequestParam MultipartFile archivo en formato Excel a importar.
     * @return ResponseEntity List con el listado de productos importados guardados.
     */
    @PostMapping("/productos/importar-excel")
    public ResponseEntity<?> importarProductosDeExcel(@RequestParam MultipartFile archivo) {
        Map<String, Object> response = new HashMap<>();
        List<Producto> productos;

        try {
            productos = this.importadorProductosService.importarDeExcel(archivo);
        } catch (ProductoException e) {
            response.put("mensaje", "Error al importar archivo Excel");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("productos", productos);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Actualiza un listado de productos a partir de un archivo .xlsx
     * URL: ~/api/productos/actualizar-excel
     * HttpMethod: POST
     * HttpStatus: CREATED
     * @param archivo @RequestParam MultipartFile archivo en formato Excel a utilizar para la actualización.
     * @return ResponseEntity listado de productos actualizados.
     */
    @PostMapping("/productos/actualizar-excel")
    public ResponseEntity<?> actualizarProductosDeExcel(@RequestParam MultipartFile archivo) {
        Map<String, Object> response = new HashMap<>();
        List<Producto> productos;

        try {
            productos = this.importadorProductosService.actualizarStockDeExcel(archivo);
        } catch (ProductoException e) {
            response.put("mensaje", "Error al actualizar los productos a partir del archivo Excel");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("productos", productos);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
