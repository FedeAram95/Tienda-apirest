package com.deofis.tiendaapirest.catalogo.controllers.catalogoadmin.productos;

import com.deofis.tiendaapirest.catalogo.productos.entities.Producto;
import com.deofis.tiendaapirest.catalogo.productos.exceptions.ProductoException;
import com.deofis.tiendaapirest.catalogo.productos.services.ProductoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/catalogo")
@AllArgsConstructor
public class ActualizarProductoController {

    private final ProductoService productoService;

    /**
     * Actualiza los datos de un producto seleccionado: nombre, descripción, subcategoría,
     * marca y unidad de medida.
     * URL: ~/api/catalogo/productos/1
     * HttpMethod: PUT
     * HttpStatus: CREATED
     * @param producto Producto actualizado.
     * @param productoId PathVariable Long del producto a modificar.
     * @return ResponseEntity con el producto actualizado.
     */
    @Secured("ROLE_ADMIN")
    @PutMapping("/productos/{productoId}")
    public ResponseEntity<?> actualizarDatosProducto(@Valid @RequestBody Producto producto,
                                                     @PathVariable Long productoId,
                                                     BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        Producto productoActualizado;

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("error", "Bad Request");
            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            productoActualizado = this.productoService.actualizarDatosProducto(producto, productoId);
        } catch (ProductoException e) {
            response.put("mensaje", "Error al modificar el producto");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("producto", productoActualizado);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Actualiza la disponibilidad general de un Producto y su Sku por defecto.
     * URL: ~/api/catalogo/productos/1/disponibilidad
     * HttpMethod: PUT
     * HttpStatus: OK
     * @param disponibilidad RequestParam Integer nueva disponibilidad.
     * @param productoId PathVariable Long id del producto.
     * @return ResponseEntity con el producto actualizado.
     */
    @PutMapping("/productos/{productoId}/disponibilidad")
    public ResponseEntity<?> actualizarDisponibilidadGeneralProducto(@RequestParam Integer disponibilidad,
                                                                     @PathVariable Long productoId) {
        Map<String, Object> response = new HashMap<>();
        Producto productoActualizado;

        try {
            productoActualizado = this.productoService
                    .actualizarDisponibilidadGeneralProducto(disponibilidad,productoId);
        } catch (ProductoException e) {
            response.put("mensaje", "Error al actualizar disponibilidad del producto");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("producto", productoActualizado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Actualiza el precio base de un Producto y su Sku por defecto.
     * URL: ~/api/catalogo/productos/1/precios/base
     * HttpMethod: PUT
     * HttpStatus: OK
     * @param precio RequestParam Double nuevo precio.
     * @param productoId PathVariable Long id del producto.
     * @return ResponseEntity con el producto actualizado.
     */
    @PutMapping("/productos/{productoId}/precios/base")
    public ResponseEntity<?> actualizarPrecioBaseProducto(@RequestParam Double precio, @PathVariable Long productoId) {
        Map<String, Object> response = new HashMap<>();
        Producto productoActualizado;

        try {
            productoActualizado = this.productoService.actualizarPrecioBaseProducto(precio, productoId);
        } catch (ProductoException e) {
            response.put("mensaje", "Error al actualizar el precio base del producto");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("producto", productoActualizado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
