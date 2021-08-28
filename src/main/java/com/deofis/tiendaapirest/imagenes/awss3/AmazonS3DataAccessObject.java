package com.deofis.tiendaapirest.imagenes.awss3;

import lombok.Data;

/**
 * Esta clase se usa para crear instancia SINGLETON para el manejo de transacciones
 * que requieran conexion con la API de AWS S3.
 */
@Data
public class AmazonS3DataAccessObject {
    private String endpoint;
    private String bucketName;

    private static AmazonS3DataAccessObject amazonS3DataAccessObject;

    private AmazonS3DataAccessObject(String endpoint, String bucketName) {
        this.endpoint = endpoint;
        this.bucketName = bucketName;
    }

    public static AmazonS3DataAccessObject getInstance(String endpoint, String bucketName) {
        if (amazonS3DataAccessObject == null)
            amazonS3DataAccessObject = new AmazonS3DataAccessObject(endpoint, bucketName);
        return amazonS3DataAccessObject;
    }

}
