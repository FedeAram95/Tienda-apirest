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

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador que se encarga de actualizar datos acerca de {@link PropiedadProducto}s y
 * de {@link ValorPropiedadProducto}s. Los endpoints de este controlador conectan con la API
 * que maneja las propiedades de productos para modificar sus nombres, y el nombre de sus
 * valores.
 */
@RestController
@RequestMapping("/api/catalogo")
@AllArgsConstructor
public class ActualizarPropiedadesController {

    private final CatalogoAdminService catalogoAdminService;

    /**
     * Se encarga de conectar con la API de catalo admin para actualizar una {@link PropiedadProducto},
     * recibiendo su ID y el nuevo objeto con los datos a modificar. Devuelve response entity con los
     * datos de la propiedad modificada.
     * URL: ~/api/catalogo/productos/propiedades/1
     * HttpMethod: PUT
     * HttpStatus: CREATED
     * @param propiedadId PathVariable Long id de la propiedad a actualizar.
     * @param propiedad RequestBody {@link PropiedadProducto}con datos actualizados.
     * @return ResponseEntity con la {@link PropiedadProducto} actualizada.
     */
    @Secured("ROLE_ADMIN")
    @PutMapping("/productos/propiedades/{propiedadId}")
    public ResponseEntity<?> actualizarPropiedadProducto(@PathVariable Long propiedadId,
                                                         @Valid @RequestBody PropiedadProducto propiedad) {
        Map<String, Object> response = new HashMap<>();
        PropiedadProducto propiedadActualizada;

        try {
            propiedadActualizada = this.catalogoAdminService.actualizarPropiedadProducto(propiedadId, propiedad);
        } catch (ProductoException e) {
            response.put("mensaje", "Error al actualizar los datos de la propiedad");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("propiedadProducto", propiedadActualizada);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Conecta al endpoint con la API de catalogo admin para actualizar el {@link ValorPropiedadProducto} de
     * una {@link PropiedadProducto} requerida.
     * URL: ~/api/catalogo/productos/propiedades/1/valores/1
     * HttpMethod: PUT
     * HttpStatus: CREATED
     * @param propiedadId PathVariable Long id de la propiedad a la que pertenece el valor.
     * @param valorId PathVariable Long id del valor a actualizar.
     * @param valorActualizado RequestBody ValorPropiedadProducto con los datos actualizados.
     * @return ResponseEntity con la {@link PropiedadProducto} actualizada (su valor actualizado)
     * y guardado en la base de datos.
     */
    @Secured("ROLE_ADMIN")
    @PutMapping("/productos/propiedades/{propiedadId}/valores/{valorId}")
    public ResponseEntity<?> actualizarValorPropiedad(@PathVariable Long propiedadId,
                                                      @PathVariable Long valorId,
                                                      @Valid @RequestBody ValorPropiedadProducto valorActualizado) {
        Map<String, Object> response = new HashMap<>();
        PropiedadProducto propiedadActualizada;

        try {
            propiedadActualizada = this.catalogoAdminService
                    .actualizarValorPropiedad(propiedadId, valorId, valorActualizado);
        } catch (ProductoException e) {
            response.put("mensaje", "Error al actualizar los datos del valor");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("propiedadProducto", propiedadActualizada);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
