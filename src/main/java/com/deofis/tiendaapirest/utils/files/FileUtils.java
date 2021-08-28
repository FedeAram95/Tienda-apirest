package com.deofis.tiendaapirest.utils.files;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

public class FileUtils {

    public static File convertirMultipartAFile(MultipartFile multipartFile) throws IOException {
        File archivoConvertido = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fileOutputStream = new FileOutputStream(archivoConvertido);
        fileOutputStream.write(multipartFile.getBytes());
        fileOutputStream.close();

        return archivoConvertido;
    }

    public static String generarFileName(MultipartFile multipartFile) {
        return new Date().getTime() + "-" + Objects.requireNonNull(multipartFile.getOriginalFilename())
                .replace(" ", "_")
                .replace("[", "")
                .replace("]", "");
    }
}
