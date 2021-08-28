package com.deofis.tiendaapirest.imagenes.controllers.download;

import com.deofis.tiendaapirest.imagenes.ImageException;
import com.deofis.tiendaapirest.imagenes.entities.Imagen;
import com.deofis.tiendaapirest.imagenes.services.ImageService;
import com.deofis.tiendaapirest.utils.files.FileException;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * API para la descarga de imágenes.
 */
@RestController
@RequestMapping("/api/uploads")
@AllArgsConstructor
public class DescargarImagenController {

    private final ImageService imageService;
    /**
     * API para descargar una imagen, a través del image path (nombre del archivo) correspondiente.
     * URL: ~/api/uploads/img/nombreArchivo.png
     * HttpMethod: GET
     * HttpStatus: OK
     * @param imagePath String con el nombre del archivo
     * @return ResponseEntity con el recurso en {@link Resource}.
     */
    @GetMapping("/img/{imagePath:.+}")
    public ResponseEntity<?> downloadImage(@PathVariable String imagePath) {
        Imagen imagen;
        byte[] imgBytes;
        Resource imgAsResource;
        String fileName;

        try {
            imagen = this.imageService.findByPath(imagePath);
            imgBytes = this.imageService.descargarImagen(imagen);
            imgAsResource = new ByteArrayResource(imgBytes);
            fileName = imagePath;
        } catch (FileException | ImageException e) {
            System.out.println(e.getMessage());

            Path rutaNotFound = Paths.get("src/main/resources/static/images")
                    .resolve("not_found.png").toAbsolutePath();
            try {
                imgAsResource = new UrlResource(rutaNotFound.toUri());
                fileName = imgAsResource.getFilename();
            } catch (MalformedURLException malformedURLException) {
                malformedURLException.printStackTrace();
                imgAsResource = null;
                fileName = "";
            }
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                .body(imgAsResource);
    }
}
