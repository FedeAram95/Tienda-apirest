package com.deofis.tiendaapirest.catalogo.controllers.catalogoadmin.productos.propiedades;

import com.deofis.tiendaapirest.catalogo.productos.entities.PropiedadProducto;
import com.deofis.tiendaapirest.catalogo.productos.entities.ValorPropiedadProducto;
import com.deofis.tiendaapirest.catalogo.productos.exceptions.ProductoException;
import com.deofis.tiendaapirest.catalogo.services.CatalogoAdminService;
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
public class CrearValorPropiedadController {

    private final CatalogoAdminService catalogoAdminService;

    /**
     * API para crear un nuevo {@link ValorPropiedadProducto} para una propiedad existente.
     * URL: ~/api/catalogo/productos/propiedades/1/valores
     * HttpMethod: POST
     * HttpStatus: CREATED
     * @param propiedadId Long id de la propeidad a la que pertenecerá el nuevo valor.
     * @param valorPropiedadProducto {@link ValorPropiedadProducto} a crear.
     * @return ResponseEntity con el valor creado.
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("/productos/propiedades/{propiedadId}/valores")
    public ResponseEntity<?> crearValorAPropiedad(@PathVariable Long propiedadId,
                                                  @RequestBody ValorPropiedadProducto valorPropiedadProducto) {
        Map<String, Object> response = new HashMap<>();
        PropiedadProducto propiedadActualizada;

        try {
            propiedadActualizada = this.catalogoAdminService.crearValorPropiedad(propiedadId, valorPropiedadProducto);
        } catch (ProductoException e) {
            response.put("mensaje", "Error al crear el nuevo valor para la propiedad");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("propiedadProducto", propiedadActualizada);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
