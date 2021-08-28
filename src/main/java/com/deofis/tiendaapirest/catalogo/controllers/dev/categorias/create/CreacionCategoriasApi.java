package com.deofis.tiendaapirest.catalogo.controllers.dev.categorias.create;

import com.deofis.tiendaapirest.catalogo.categorias.entities.Categoria;
import com.deofis.tiendaapirest.catalogo.categorias.entities.Subcategoria;
import com.deofis.tiendaapirest.catalogo.categorias.exceptions.CategoriaException;
import com.deofis.tiendaapirest.catalogo.categorias.repositories.CategoriaRepository;
import com.deofis.tiendaapirest.catalogo.categorias.services.CategoriaService;
import com.deofis.tiendaapirest.catalogo.categorias.services.SubcategoriaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

/**
 * API para la creación de categorias/subcategorias, destinada a desarrolladores solamente.
 */
@RestController
@RequestMapping("/api/dev")
@AllArgsConstructor
@Slf4j
public class CreacionCategoriasApi {

    private final CategoriaService categoriaService;
    private final CategoriaRepository categoriaRepository;
    private final SubcategoriaService subcategoriaService;

    @Secured("ROLE_ADMIN")
    @PostMapping("/categorias")
    public ResponseEntity<Categoria> createCategory(@RequestBody Categoria categoria) {
        Categoria nuevaCategoria = this.categoriaService.crear(categoria);
        return new ResponseEntity<>(nuevaCategoria, HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/categorias/{categoriaId}/subcategorias")
    public ResponseEntity<Subcategoria> createSubcategory(@RequestBody Subcategoria subcategoria,
                                                          @PathVariable Long categoriaId) {
        Categoria categoria;
        Subcategoria nuevaSubcategoria;

        try {
            categoria = this.categoriaService.obtenerCategoria(categoriaId);
            nuevaSubcategoria = this.subcategoriaService.crear(subcategoria);
            categoria.getSubcategorias().add(nuevaSubcategoria);
            this.categoriaRepository.save(categoria);
        } catch (CategoriaException e) {
            log.error(e.getMessage());
            return null;
        }

        return new ResponseEntity<>(nuevaSubcategoria, HttpStatus.CREATED);
    }

}
