package com.deofis.tiendaapirest.imagenes.services.s3;

import org.springframework.web.multipart.MultipartFile;

/**
 * Servicio que se encarga directamente de subir un archivo al bucket de AWS S3 referenciado
 * en las configuraciones.
 */

public interface UploadFileAmazonS3Service {

    /**
     * Subir archivo Multipartfile, previamente convertido a File (requerido por API AWS), al bucket
     * referenciado en las configuraciones.
     * @param multipartFile Multipartfile archivo a convertir y subir al bucket.
     * @return String con el nombre del archivo generado (sin espacios).
     */
    String subirArchivoMultipart(MultipartFile multipartFile);
}
