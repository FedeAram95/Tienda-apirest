package com.deofis.tiendaapirest.catalogo.controllers.catalogoadmin.productos;

import com.deofis.tiendaapirest.catalogo.productos.entities.Producto;
import com.deofis.tiendaapirest.catalogo.productos.exceptions.ProductoException;
import com.deofis.tiendaapirest.catalogo.services.CatalogoAdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/catalogo")
@AllArgsConstructor
public class CrearProductoController {

    private final CatalogoAdminService catalogoAdminService;

    /**
     * API para registrar la creación de un nuevo {@link Producto}, con todos los datos
     * que el administrador solicitó.
     * URL: ~/api/catalgo/productos
     * HttpMethod: POST
     * HttpStatus: CREATED
     * @param producto {@link Producto} nuevo a crear.
     * @param result binding result con los errores de la petición, si es que tiene.
     * @return ResponseEntity con los datos del producto creado.
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("/productos")
    public ResponseEntity<?> crearNuevoProducto(@Valid @RequestBody Producto producto, BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        Producto nuevoProducto;

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
            nuevoProducto = this.catalogoAdminService.crearProducto(producto);
        } catch (ProductoException e) {
            response.put("mensaje", "Error al crear el nuevo producto");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("producto", nuevoProducto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
