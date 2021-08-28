package com.deofis.tiendaapirest.catalogo.controllers.catalogoadmin.skus;

import com.deofis.tiendaapirest.catalogo.productos.exceptions.ProductoException;
import com.deofis.tiendaapirest.catalogo.services.CatalogoAdminService;
import com.deofis.tiendaapirest.catalogo.skus.entities.Sku;
import com.deofis.tiendaapirest.catalogo.skus.exceptions.SkuException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/catalogo")
@AllArgsConstructor
public class CrearSkuController {

    private final CatalogoAdminService catalogoAdminService;

    /**
     * API para crear un nuevo {@link Sku} para un producto requerido.
     * URL: ~/api/catalogo/productos/1/skus
     * HttpMethod: POST
     * HttpStatus: CREATED
     * @param productoId Long id del producto a crear un sku.
     * @param sku {@link Sku} nuevo a crear.
     * @return ResponseEntity con el nuevo sku creado.
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("/productos/{productoId}/skus")
    public ResponseEntity<?> crearNuevoSkuProducto(@PathVariable Long productoId, @Valid @RequestBody Sku sku) {
        Map<String, Object> response = new HashMap<>();
        Sku nuevoSku;

        try {
            nuevoSku = this.catalogoAdminService.crearSku(productoId, sku);
        } catch (ProductoException | SkuException e) {
            response.put("mensaje", "Error al crear nuevo Sku de producto");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("sku", nuevoSku);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
