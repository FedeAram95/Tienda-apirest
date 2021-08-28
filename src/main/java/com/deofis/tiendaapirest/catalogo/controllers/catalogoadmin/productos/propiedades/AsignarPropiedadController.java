package com.deofis.tiendaapirest.catalogo.controllers.catalogoadmin.productos.propiedades;

import com.deofis.tiendaapirest.catalogo.productos.exceptions.ProductoException;
import com.deofis.tiendaapirest.catalogo.services.CatalogoAdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/catalogo")
@AllArgsConstructor
public class AsignarPropiedadController {

    private final CatalogoAdminService catalogoAdminService;

    /**
     * API para asignar propiedades existentes a productos.
     * URL: ~/api/catalogo/productos/1/propiedades/1/asignar
     * @param productoId Long id del producto.
     * @param propiedadId Long id de la propiedad a asignar.
     * @return ResponseEntity con mensaje de exito.
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("/productos/{productoId}/propiedades/{propiedadId}/asignar")
    public ResponseEntity<?> asignarPropiedadAProducto(@PathVariable Long productoId,
                                                       @PathVariable Long propiedadId) {
        Map<String, Object> response = new HashMap<>();

        try {
            this.catalogoAdminService.asignarPropiedadAProducto(productoId, propiedadId);
        } catch (ProductoException e) {
            response.put("mensaje", "Error al asignar la propiedad al producto");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "Propiedad asignada con éxito");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * API para asignar propiedad existente a subcategoría.
     * URL: ~/api/catalogo/subcategorias/1/propiedades/1/asignar
     * HttpMethod: GET
     * HttpStatus: OK
     * @param subcategoriaId Long id de la subcategoría.
     * @param propiedadId Long id de la propiedad a asignar.
     * @return ResponseEntity con mensaje de exito.
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("/subcategorias/{subcategoriaId}/propiedades/{propiedadId}/asignar")
    public ResponseEntity<?> asignarPropiedadASubcategoria(@PathVariable Long subcategoriaId,
                                                           @PathVariable Long propiedadId) {
        Map<String, Object> response = new HashMap<>();

        try {
            this.catalogoAdminService.asignarPropiedadASubcategoria(subcategoriaId, propiedadId);
        } catch (ProductoException e) {
            response.put("mensaje", "Error al asignar la propiedad a la subcategoría");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "Propiedad asignada con éxito");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
