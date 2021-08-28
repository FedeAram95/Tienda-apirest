package com.deofis.tiendaapirest.imagenes.services.validadores;

import com.deofis.tiendaapirest.utils.validators.ValidadorExtensionArchivo;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * Implementación para archivos de imágenes de {@link ValidadorExtensionArchivo}.
 */
@Service
@Qualifier("validadorExtensionImages")
public class ValidadorExtensionImages implements ValidadorExtensionArchivo {

    @Override
    public boolean validar(MultipartFile file) {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        List<String> extensionesValidas = Arrays.asList("jpeg", "jpg", "png", "mp4", "pdf", "webp");

        return extensionesValidas.contains(fileExtension);
    }
}
