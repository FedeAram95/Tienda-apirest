package com.deofis.tiendaapirest.catalogo.controllers.catalogoadmin.productos.propiedades;

import com.deofis.tiendaapirest.catalogo.productos.entities.PropiedadProducto;
import com.deofis.tiendaapirest.catalogo.productos.entities.ValorPropiedadProducto;
import com.deofis.tiendaapirest.catalogo.productos.exceptions.ProductoException;
import com.deofis.tiendaapirest.catalogo.services.CatalogoAdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/catalogo")
@AllArgsConstructor
public class ObtenerPropiedadesProductoController {

    private final CatalogoAdminService catalogoAdminService;

    /**
     * Obtiene todas las propiedades de producto existentes.
     * URL: ~/api/catalogo/productos/propiedades
     * HttpMethod: GET
     * HttpStatus: OK
     * @return ResponseEntity listado de propiedades.
     */
    @GetMapping("/productos/propiedades")
    public ResponseEntity<?> obtenerPropiedadesProducto() {
        Map<String, Object> response = new HashMap<>();
        List<PropiedadProducto> propiedades;

        try {
            propiedades = this.catalogoAdminService.obtenerPropiedadesProducto();
        } catch (ProductoException e) {
            response.put("mensaje", "Error al obtener las propiedades de producto");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("propiedades", propiedades);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Obtiene una propiedad producto por su id.
     * URL: ~/api/catalogo/productos/propiedades/1
     * HttpMethod: GET
     * HttpStatus: OK
     * @param propiedadId Long id de la propiedad a obtener.
     * @return ResponseEntity con la propiedad.
     */
    @GetMapping("/productos/propiedades/{propiedadId}")
    public ResponseEntity<?> obtenerPropiedadProducto(@PathVariable Long propiedadId) {
        Map<String, Object> response = new HashMap<>();
        PropiedadProducto propiedad;

        try {
            propiedad = this.catalogoAdminService.obtenerPropiedadProducto(propiedadId);
        } catch (ProductoException e) {
            response.put("mensaje", "Error al obtener la propiedad de producto");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("propiedad", propiedad);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Obtiene los valores de una propiedad requerida.
     * URL: ~/api/catalogo/productos/propiedades/1/valores
     * HttpMethod: GET
     * HttpStatus: OK
     * @param propiedadId Long id de la propiedad requerida.
     * @return ResponseEntity con el listado de valores.
     */
    @GetMapping("/productos/propiedades/{propiedadId}/valores")
    public ResponseEntity<?> obtenerValoresDePropiedad(@PathVariable Long propiedadId) {
        Map<String, Object> response = new HashMap<>();
        List<ValorPropiedadProducto> valores;

        try {
            valores = this.catalogoAdminService.obtenerValoresDePropiedad(propiedadId);
        } catch (ProductoException e) {
            response.put("mensaje", "Error al obtener los valores de la propiedad");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("valores", valores);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
