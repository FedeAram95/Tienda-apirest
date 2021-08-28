package com.deofis.tiendaapirest.imagenes.services;

import com.deofis.tiendaapirest.catalogo.productos.entities.Producto;
import com.deofis.tiendaapirest.catalogo.skus.entities.Sku;
import com.deofis.tiendaapirest.imagenes.entities.Imagen;
import org.springframework.web.multipart.MultipartFile;

/**
 * Este servicio se encarga de subir las imágenes utilizadas por Objetos que
 * los necesiten ({@link Producto}
 * y {@link Sku}).
 */
public interface ImageService {

    /**
     * Método que se encarga de subir la imágen.
     * @param archivo MultipartFile archivo a subir.
     * @return {@link Imagen} con los datos cargados de la subida.
     */
    Imagen subirImagen(MultipartFile archivo);

    /**
     * Devuelve bytes asociados a una imágen guardada.
     * @param imagen Imagen que contiene el path del archivo imagen a descargar.
     * @return byte[] bytes del archivo imagen.
     */
    byte[] descargarImagen(Imagen imagen);

    /**
     * Obtiene una {@link Imagen} a través de su path (nombre del archivo).
     * @param imagePath String con el image path.
     * @return {@link Imagen}
     */
    Imagen findByPath(String imagePath);

    /**
     * Elimina un archivo de imagen a través de su path.
     * @param imagen Imagen que contiene el path del archivo imagen a eliminar.
     */
    boolean eliminarImagen(Imagen imagen);
}
