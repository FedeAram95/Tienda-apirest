package com.deofis.tiendaapirest.imagenes.services.local;

import com.deofis.tiendaapirest.imagenes.ImageException;
import com.deofis.tiendaapirest.imagenes.entities.Imagen;
import com.deofis.tiendaapirest.imagenes.repositories.ImagenRepository;
import com.deofis.tiendaapirest.imagenes.services.ImageService;
import com.deofis.tiendaapirest.imagenes.services.validadores.ValidadorExtensionImages;
import com.deofis.tiendaapirest.utils.files.FileException;
import com.deofis.tiendaapirest.utils.files.FileUtils;
import com.deofis.tiendaapirest.utils.validators.ValidadorExtensionArchivo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Profile({"dev", "qa", "devdocker"})
@Slf4j
public class LocalImageService implements ImageService {

    private static final String API_UPLOAD_URI = "/api/uploads/img/";

    private final String DIR_UPLOAD;
    private final String baseUrl;
    private final ImagenRepository imagenRepository;

    @Autowired
    public LocalImageService(String baseUrl, ImagenRepository imagenRepository, String uploadsDir) {
        this.baseUrl = baseUrl;
        this.imagenRepository = imagenRepository;
        DIR_UPLOAD = uploadsDir;
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(DIR_UPLOAD));
        } catch (IOException e) {
            throw new FileException("No se pudo crear directorio");
        }
    }

    @Transactional
    @Override
    public Imagen subirImagen(MultipartFile archivo) {
        if (!validarExtension(archivo))
            throw new FileException("Extension de imagen invalida");

        String imagePath = FileUtils.generarFileName(archivo);
        Path rutaArchivo = this.getPath(imagePath);

        try {
            log.info(DIR_UPLOAD);
            Files.copy(archivo.getInputStream(), rutaArchivo);
        } catch (IOException e) {
            throw new ImageException(e.getMessage());
        }

        Imagen imagen = Imagen.builder()
                .imageUrl(baseUrl.concat(API_UPLOAD_URI + imagePath))
                .path(imagePath).build();
        return this.imagenRepository.save(imagen);
    }

    @Override
    public byte[] descargarImagen(Imagen imagen) {
        Path rutaImg = this.getPath(imagen.getPath());

        byte[] content;
        Resource recurso;
        try {
            recurso = new UrlResource(rutaImg.toUri());
        } catch (MalformedURLException e) {
            throw new ImageException(e.getMessage());
        }

        try (InputStream inputStream = recurso.getInputStream()) {
            content = inputStream.readAllBytes();
        } catch (IOException e) {
            throw new ImageException(e.getMessage());
        }

        if (!recurso.exists() && recurso.isReadable()) {
            throw new ImageException("No existe la imagen cargada de manera local");
        }

        return content;
    }

    @Transactional(readOnly = true)
    @Override
    public Imagen findByPath(String imagePath) {
        return this.imagenRepository.findByPath(imagePath)
                .orElseThrow(() -> new ImageException("No existe imagen con path: " + imagePath));
    }

    @Transactional
    @Override
    public boolean eliminarImagen(Imagen imagen) {
        System.out.println("## ELIMINANDO IMAGEN ACTUAL ##");
        String imagePath = imagen.getPath();

        boolean eliminado = false;
        if (imagePath != null && imagePath.length() > 0) {
            Path rutaImgAnterior = this.getPath(imagePath);
            File archivoImgAnterior = rutaImgAnterior.toFile();
            if (archivoImgAnterior.exists() && archivoImgAnterior.canRead())
                eliminado = archivoImgAnterior.delete();

        }

        log.info("Eliminado? : "  + eliminado);
        if (eliminado) this.imagenRepository.delete(imagen);
        return eliminado;
    }

    private boolean validarExtension(MultipartFile archivo) {
        ValidadorExtensionArchivo validadorExtensionArchivo = new ValidadorExtensionImages();
        return validadorExtensionArchivo.validar(archivo);
    }

    private Path getPath(String nombreArchivo) {
        return Paths.get(DIR_UPLOAD).resolve(nombreArchivo).toAbsolutePath();
    }
}
