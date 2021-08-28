package com.deofis.tiendaapirest.imagenes.services.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.deofis.tiendaapirest.imagenes.ImageException;
import com.deofis.tiendaapirest.imagenes.awss3.AmazonS3DataAccessObject;
import com.deofis.tiendaapirest.imagenes.entities.Imagen;
import com.deofis.tiendaapirest.imagenes.repositories.ImagenRepository;
import com.deofis.tiendaapirest.imagenes.services.ImageService;
import com.deofis.tiendaapirest.imagenes.services.validadores.ValidadorExtensionImages;
import com.deofis.tiendaapirest.utils.files.FileException;
import com.deofis.tiendaapirest.utils.validators.ValidadorExtensionArchivo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Profile({"staging", "prod"})
@AllArgsConstructor
@Slf4j
public class AmazonS3ImageService implements ImageService {

    private final UploadFileAmazonS3Service uploadFileAmazonS3Service;
    private final AmazonS3DataAccessObject amazonS3DataAccessObject;
    private final AmazonS3 amazonS3;
    private final ImagenRepository imagenRepository;

    @Transactional
    @Override
    public Imagen subirImagen(MultipartFile archivo) {

        if (!validarExtensionesImagen(archivo))
            throw new FileException("Extension de imagen invalida");

        String path = this.uploadFileAmazonS3Service.subirArchivoMultipart(archivo);

        Imagen imagen = Imagen.builder()
                .imageUrl(this.amazonS3DataAccessObject.getEndpoint().concat(path))
                .path(path).build();

        return this.imagenRepository.save(imagen);
    }

    @Override
    public byte[] descargarImagen(Imagen imagen) {
        byte[] content;

        S3Object s3Object = this.amazonS3.getObject(this.amazonS3DataAccessObject.getBucketName(), imagen.getPath());
        S3ObjectInputStream stream = s3Object.getObjectContent();

        log.info("S3 Object Key --> ".concat(s3Object.getKey()));

        try {
            content = IOUtils.toByteArray(stream);
            s3Object.close();
            log.info("Imagen descargada exitosamente");
        } catch (IOException e) {
            log.warn(e.getMessage());
            throw new FileException("Error al descargar imagen");
        }

        return content;
    }

    @Transactional(readOnly = true)
    @Override
    public Imagen findByPath(String imagePath) {
        return this.imagenRepository.findByPath(imagePath)
                .orElseThrow(() -> new ImageException("No existe imagen con el path: " + imagePath));
    }

    @Transactional
    @Override
    public boolean eliminarImagen(Imagen imagen) {
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(
                this.amazonS3DataAccessObject.getBucketName(),
                imagen.getPath());

        this.amazonS3.deleteObject(deleteObjectRequest);
        this.imagenRepository.deleteById(imagen.getId());
        return true;
    }

    private boolean validarExtensionesImagen(MultipartFile archivo) {
        ValidadorExtensionArchivo validadorExtensionArchivo = new ValidadorExtensionImages();
        return validadorExtensionArchivo.validar(archivo);
    }
}
