package com.deofis.tiendaapirest.utils.validators;

import org.springframework.web.multipart.MultipartFile;

/**
 * Clase que se encarga de validar las extensiones de un archivo recibido por parámetro.
 */
public interface ValidadorExtensionArchivo {

    /**
     * Valida que el archivo recibido sea contenido por un listado de extensiones
     * establecido.
     * @param file {@link MultipartFile} a validar su extensión.
     * @return true/false de acuerdo a validación.
     */
    boolean validar(MultipartFile file);
}
