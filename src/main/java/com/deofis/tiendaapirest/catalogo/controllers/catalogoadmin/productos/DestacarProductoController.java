package com.deofis.tiendaapirest.catalogo.controllers.catalogoadmin.productos;

import com.deofis.tiendaapirest.catalogo.productos.entities.Producto;
import com.deofis.tiendaapirest.catalogo.productos.exceptions.ProductoException;
import com.deofis.tiendaapirest.catalogo.productos.services.ProductoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/catalogo")
@AllArgsConstructor
public class DestacarProductoController {

    private final ProductoService productoService;

    /**
     * API para destacar/quitar de destacados un producto a través de su id.
     * URL: ~/api/catalogo/productos/destacar/1
     * HttpMethod: POST
     * HttpStatus: OK
     * @param producto {@link Producto} a destacar/quitar.
     * @param id Long id del producto a destacar/quitar.
     * @return ResponseEntity con los datos del producto actualizados.
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("/productos/destacar/{id}")
    public ResponseEntity<?> destacarProducto(@RequestBody Producto producto, @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            this.productoService.destacar(producto, id);
        } catch (ProductoException e) {
            response.put("mensaje", "Error al destacar/quitar al producto");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "Producto destacado/quitado con éxito");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
