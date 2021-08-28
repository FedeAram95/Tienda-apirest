package com.deofis.tiendaapirest.catalogo.skus.services.validadores;

import com.deofis.tiendaapirest.utils.validators.ValidadorExtensionArchivo;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * Implementación de archivos EXCEL de {@link ValidadorExtensionArchivo}.
 */
@Service
@Qualifier("validadorExtensionesExcel")
public class ValidadorExtensionExcel implements ValidadorExtensionArchivo {

    @Override
    public boolean validar(MultipartFile file) {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        List<String> extensionesValidas = Arrays.asList("xlsx", "xls");

        return extensionesValidas.contains(fileExtension);
    }
}
