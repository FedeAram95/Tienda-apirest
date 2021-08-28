package com.deofis.tiendaapirest.catalogo.controllers.catalogoadmin.productos.marcas;

import com.deofis.tiendaapirest.catalogo.productos.entities.Marca;
import com.deofis.tiendaapirest.catalogo.productos.exceptions.ProductoException;
import com.deofis.tiendaapirest.catalogo.productos.services.MarcaService;
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
public class ActualizarMarcaController {

    private final MarcaService marcaService;

    /**
     * Actualizar una marca.
     * URL: ~/api/catalogo/productos/marcas/1
     * HttpMethod: PUT
     * HttpStatus: CREATED
     * @param marca Marca actualizada.
     * @param marcaId PathVariable id de la marca a actualizar.
     * @return ResponseEntity con la marca actualizada.
     */
    @Secured("ROLE_ADMIN")
    @PutMapping("/productos/marcas/{marcaId}")
    public ResponseEntity<?> actualizar(@Valid @RequestBody Marca marca, @PathVariable Long marcaId, BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        Marca marcaActualizada;

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
            marcaActualizada = this.marcaService.actualizar(marca, marcaId);
        } catch (ProductoException e) {
            response.put("mensaje", "Error al actualizar la marca");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(marcaActualizada, HttpStatus.CREATED);
    }

}
