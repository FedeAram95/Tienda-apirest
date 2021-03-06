package com.deofis.tiendaapirest.catalogo.categorias.services;

import com.deofis.tiendaapirest.catalogo.categorias.entities.Categoria;
import com.deofis.tiendaapirest.catalogo.categorias.entities.Subcategoria;
import com.deofis.tiendaapirest.catalogo.categorias.exceptions.CategoriaException;
import com.deofis.tiendaapirest.catalogo.categorias.repositories.CategoriaRepository;
import com.deofis.tiendaapirest.catalogo.categorias.repositories.SubcategoriaRepository;
import com.deofis.tiendaapirest.imagenes.entities.Imagen;
import com.deofis.tiendaapirest.imagenes.services.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final SubcategoriaRepository subcategoriaRepository;
    private final ImageService imageService;

    @Override
    @Transactional
    public Categoria crear(Categoria categoria) {
        Categoria categoriaNueva = Categoria.builder()
                .nombre(categoria.getNombre())
                .subcategorias(new ArrayList<>())
                .build();

        return this.categoriaRepository.save(categoriaNueva);
    }

    @Override
    @Transactional
    public Categoria actualizar(Categoria categoria, Long id) {
        Categoria categoriaActual = this.obtenerCategoria(id);

        categoriaActual.setNombre(categoria.getNombre());

        return this.categoriaRepository.save(categoriaActual);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> obtenerCategorias() {
        return this.categoriaRepository.findAllByOrderByNombreAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public Categoria obtenerCategoria(Long id) {
        return this.categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaException("Categor??a no existente con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subcategoria> obtenerSubcategorias(Long categoriaId) {
        Categoria categoria = this.categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new CategoriaException("Categoria no existente con id: " + categoriaId));


        return categoria.getSubcategorias();
    }

    @Override
    @Transactional(readOnly = true)
    public Subcategoria obtenerSubcategoriaDeCategoria(Long categoriaId, Long subcategoriaId) {
        Categoria categoria = this.obtenerCategoria(categoriaId);
        Subcategoria subcategoria = null;

        boolean existeSub = false;
        for (Subcategoria sub: categoria.getSubcategorias()) {
            if (sub.getId().equals(subcategoriaId)) {
                existeSub = true;
                subcategoria = sub;
                break;
            }
        }

        if (!existeSub) throw new CategoriaException("La subcategor??a no pertenece a la categor??a: ".concat(categoria.getNombre()));

        return subcategoria;
    }

    @Transactional
    @Override
    public Imagen subirFotoCategoria(Long categoriaId, MultipartFile foto) {
        Categoria categoria = this.obtenerCategoria(categoriaId);

        if (categoria.getFoto() != null)
            this.eliminarFotoCategoria(categoria.getId());

        Imagen fotoCategoria = this.imageService.subirImagen(foto);
        categoria.setFoto(fotoCategoria);
        this.categoriaRepository.save(categoria);
        return fotoCategoria;
    }

    @Transactional(readOnly = true)
    @Override
    public byte[] obtenerFotoCategoria(Long categoriaId) {
        Categoria categoria = this.obtenerCategoria(categoriaId);
        Imagen fotoCategoria = categoria.getFoto();

        if (fotoCategoria == null) throw new CategoriaException("La categor??a: " + categoria.getNombre()
                + " no tiene foto");

        return this.imageService.descargarImagen(fotoCategoria);
    }

    @Transactional(readOnly = true)
    @Override
    public String obtenerPathFotoCategoria(Long categoriaId) {
        Categoria categoria = this.obtenerCategoria(categoriaId);
        return categoria.getFoto().getPath();
    }

    @Transactional
    @Override
    public void eliminarFotoCategoria(Long categoriaId) {
        Categoria categoria = this.obtenerCategoria(categoriaId);

        if (categoria.getFoto() == null) throw new CategoriaException("La categor??a: " + categoria.getNombre()
                + " no tiene foto");

        Imagen fotoCategoria = categoria.getFoto();
        categoria.setFoto(null);
        this.categoriaRepository.save(categoria);
        this.imageService.eliminarImagen(fotoCategoria);
    }

    @Transactional(readOnly = true)
    @Override
    public Categoria obtenerCategoriaPorSubcategoria(Subcategoria subcategoria) {
        return this.categoriaRepository.findBySubcategoriasContaining(subcategoria)
                .orElseThrow(() -> new CategoriaException("La subcategor??a no pertenece a ninguna categor??a"));
    }
}
