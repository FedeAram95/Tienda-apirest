package com.deofis.tiendaapirest.catalogo.controllers.catalogoadmin.imagenes;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.deofis.tiendaapirest.catalogo.productos.entities.Producto;
import com.deofis.tiendaapirest.catalogo.productos.exceptions.ProductoException;
import com.deofis.tiendaapirest.catalogo.services.CatalogoAdminService;
import com.deofis.tiendaapirest.catalogo.skus.entities.Sku;
import com.deofis.tiendaapirest.imagenes.ImageException;
import com.deofis.tiendaapirest.imagenes.entities.Imagen;
import com.deofis.tiendaapirest.utils.files.FileException;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/catalogo/imagenes")
@AllArgsConstructor
public class ImagenesProductoController {

    private final CatalogoAdminService catalogoAdminService;

    /**
     * Sube y asocia foto principal a un {@link Producto} requerido.
     * URL: ~/api/catalogo/imagenes/productos/1/principal
     * HttpMethod: POST
     * HttpStatus: CREATED
     * @param productoId PathVariable Long id del producto a subir foto principal.
     * @param foto MultipartFile archivo que contiene la foto del producto a crear.
     * @return ResponseEntity {@link Imagen} con los datos de la foto creada y subida.
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("/productos/{productoId}/principal")
    public ResponseEntity<?> subirFotoPrincipalProducto(@PathVariable Long productoId,
                                                        @RequestParam(name = "foto")MultipartFile foto) {
        Map<String, Object> response = new HashMap<>();
        Imagen fotoPrincipal;

        try {
            fotoPrincipal = this.catalogoAdminService.subirFotoPpalProducto(productoId, foto);
        } catch (ProductoException | FileException | ImageException | AmazonS3Exception e) {
            response.put("mensaje", "Error al subir foto principal del producto");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("foto", fotoPrincipal);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Sube y asocia foto a un {@link Sku} requerido.
     * URL: ~/api/catalogo/imagenes/productos/skus/1
     * HttpMethod: POST
     * HttpStatus: CREATED
     * @param skuId PathVariable Long id del sku a subir foto.
     * @param foto RequestParam MultipartFile archivo que contiene la foto del sku a crear y subir.
     * @return ResponseEntity {@link Imagen} con los datos de la foto creada y subida.
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("/productos/skus/{skuId}")
    public ResponseEntity<?> subirFotoSku(@PathVariable Long skuId, @RequestParam(name = "foto")MultipartFile foto) {
        Map<String, Object> response = new HashMap<>();
        Imagen fotoSku;

        try {
            fotoSku = this.catalogoAdminService.subirFotoSku(skuId, foto);
        } catch (ProductoException | FileException | ImageException | AmazonS3Exception e) {
            response.put("mensaje", "Error al subir foto del sku");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("foto", fotoSku);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Descarga y obtiene la foto principal asociada a un {@link Producto}.
     * URL: ~/api/catalogo/imagenes/productos/1/principal
     * HttpMethod: GET
     * HttpStatus: OK
     * @param productoId PathVariable Long id del producto a obtener foto principal.
     * @return ResponseEntity con la imagen como recurso.
     */
    @GetMapping("/productos/{productoId}/principal")
    public ResponseEntity<?> obtenerFotoPrincipalProducto(@PathVariable Long productoId) {
        Map<String, Object> response = new HashMap<>();
        byte[] fotoBytes;
        ByteArrayResource fotoAsResource;
        String imagePath;

        try {
            fotoBytes = this.catalogoAdminService.obtenerFotoPpalProducto(productoId);
            imagePath = this.catalogoAdminService.obtenerPathFotoPpalProducto(productoId);
            fotoAsResource = new ByteArrayResource(fotoBytes);
        } catch (ProductoException | FileException | ImageException | AmazonS3Exception e) {
            response.put("mensaje", "Error al obtener foto principal del producto");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity
                .ok()
                .header("Content-Type", "application/octet-stream")
                .header("Content-Disposition", "attachment; filename=\"" + imagePath + "\"")
                .body(fotoAsResource);
    }

    /**
     * Descarga y obtiene la foto asociada a un {@link Sku}.
     * URL: ~/api/catalogo/imagenes/productos/skus/1
     * HttpMethod: GET
     * HttpStatus: OK
     * @param skuId PathVariable Long id del sku a obtener foto.
     * @return ResponseEntity con la imagen como recurso.
     */
    @GetMapping("/productos/skus/{skuId}")
    public ResponseEntity<?> obtenerFotoSku(@PathVariable Long skuId) {
        Map<String, Object> response = new HashMap<>();
        byte[] fotoBytes;
        ByteArrayResource fotoAsResource;
        String imagePath;

        try {
            fotoBytes = this.catalogoAdminService.obtenerFotoSku(skuId);
            imagePath = this.catalogoAdminService.obtenerPathFotoSku(skuId);
            fotoAsResource = new ByteArrayResource(fotoBytes);
        } catch (ProductoException | FileException | ImageException | AmazonS3Exception e) {
            response.put("mensaje", "Error al obtener foto del sku");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity
                .ok()
                .header("Content-Type", "application/octet-stream")
                .header("Content-Disposition", "attachment; filename=\"" + imagePath + "\"")
                .body(fotoAsResource);
    }

    /**
     * Elimina la foto principal asociada a un {@link Producto}.
     * URL: ~/api/catalogo/imagenes/productos/1/principal
     * HttpMethod: DELETE
     * HttpStatus: OK
     * @param productoId PathVariable Long id del producto a eliminar foto principal.
     * @return ResponseEntity con mensaje ??xito/error sobre la eliminaci??n de la foto principal del producto.
     */
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/productos/{productoId}/principal")
    public ResponseEntity<?> eliminarFotoPrincipalProducto(@PathVariable Long productoId) {
        Map<String, Object> respose = new HashMap<>();
        String msg;

        try {
            this.catalogoAdminService.eliminarFotoPpalProducto(productoId);
            msg = "Foto Principal del producto eliminada con ??xito";
        } catch (ProductoException | FileException | ImageException | AmazonS3Exception e) {
            respose.put("mensaje", "Error al eliminar foto principal del producto");
            respose.put("error", e.getMessage());
            return new ResponseEntity<>(respose, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        respose.put("mensaje", msg);
        return new ResponseEntity<>(respose, HttpStatus.OK);
    }

    /**
     * Elimina la foto asociada a un {@link Sku}.
     * URL: ~/api/catalogo/imagenes/productos/skus/1
     * HttpMethod: DELETE
     * HttpStatus: OK
     * @param skuId PathVariable Long id del sku a eliminar foto.
     * @return ResponseEntity con mensaje ??xito/error sobre la eliminaci??n de la foto del sku.
     */
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/productos/skus/{skuId}")
    public ResponseEntity<?> eliminarFotoSku(@PathVariable Long skuId) {
        Map<String, Object> respose = new HashMap<>();
        String msg;

        try {
            this.catalogoAdminService.eliminarFotoSku(skuId);
            msg = "Foto del sku eliminada con ??xito";
        } catch (ProductoException | FileException | ImageException | AmazonS3Exception e) {
            respose.put("mensaje", "Error al eliminar foto del sku");
            respose.put("error", e.getMessage());
            return new ResponseEntity<>(respose, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        respose.put("mensaje", msg);
        return new ResponseEntity<>(respose, HttpStatus.OK);
    }

    /**
     * Sube una imagen secundaria para un {@link Producto} requerido.
     * URL: ~/api/catalogo/imagenes/productos/1
     * HttpMethod: POST
     * HttpStatus: CREATED
     * @param productoId PathVariable Long id del producto a subir foto secundaria.
     * @param foto MultipartFile archivo que contiene la foto secundaria del producto a crear.
     * @return ResponseEntity {@link Imagen} con los datos de la foto creada y subida.
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("/productos/{productoId}")
    public ResponseEntity<?> subirFotoSecundariaProducto(@PathVariable Long productoId,
                                                         @RequestParam(name = "foto") MultipartFile foto) {
        Map<String, Object> response = new HashMap<>();
        Imagen imagenSecundaria;

        try {
            imagenSecundaria = this.catalogoAdminService.subirFotoSecundariaProducto(productoId, foto);
        } catch (ProductoException | FileException | ImageException | AmazonS3Exception e) {
            response.put("mensaje", "Error al subir la imagen secundaria para el producto");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("foto", imagenSecundaria);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * API que cambia una imagen secundaria de un producto existente.
     * URL: ~/api/catalogo/imagenes/productos/1/imagen/1
     * HttpMethod: PUT
     * HttpStatus: OK
     * @param productoId Long id del producto a cambiar alguna imagen secundaria.
     * @param imagenId Long id de la imagen vieja que se desea reemplazar.
     * @param foto MultipartFile con el archivo de la imagen secundaria nueva del producto.
     * @return ResponseEntity con la imagen.
     */
    @Secured("ROLE_ADMIN")
    @PutMapping("/productos/{productoId}/imagen/{imagenId}")
    public ResponseEntity<?> cambiarImagenSecundariaProducto(@PathVariable Long productoId,
                                                             @PathVariable Long imagenId,
                                                             @RequestParam(name = "foto") MultipartFile foto) {
        Map<String, Object> response = new HashMap<>();
        Imagen imagenNueva;

        try {
            imagenNueva = this.catalogoAdminService.cambiarImagenSecundariaProducto(productoId, imagenId, foto);
        } catch (ProductoException | FileException | ImageException | AmazonS3Exception e) {
            response.put("mensaje", "Error al cambiar la imagen secundaria del producto");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("foto", imagenNueva);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Obtiene todas las {@link Imagen}es secundarias del producto requerido.
     * URL: ~/api/catalogo/imagenes/productos/1/secundarias
     * HttpMethod: GET
     * HttpStatus: OK
     * @param productoId Long id del producto a obtener sus fotos secundarias.
     * @return ResponseEntity con List de imagenes secundarias del producto.
     */
    @GetMapping("/productos/{productoId}/secundarias")
    public ResponseEntity<?> obtenerFotosSecundariasProducto(@PathVariable Long productoId) {
        Map<String, Object> response = new HashMap<>();
        List<Imagen> imagenesSecundarias;

        try {
            imagenesSecundarias = this.catalogoAdminService.obtenerFotosSecundariasProducto(productoId);
        } catch (ProductoException | FileException | ImageException e) {
            response.put("mensaje", "Error al obtener las imagenes secundarias del producto");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("fotos", imagenesSecundarias);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Obtiene la {@link Imagen} secundaria de un producto requerido.
     * URL: ~/api/catalogo/imagenes/productos/1/imagen/1
     * HttpMethod: GET
     * HttpStatus: OK
     * @param productoId Long id del producto.
     * @param imagenId Long id de la imagen a obtener.
     * @return ResponseEntity con la {@link Imagen} requerida.
     */
    @GetMapping("/productos/{productoId}/imagen/{imagenId}")
    public ResponseEntity<?> obtenerFotoSecundariaProducto(@PathVariable Long productoId,
                                                           @PathVariable Long imagenId) {
        Map<String, Object> response = new HashMap<>();
        Imagen imagenSecundaria;

        try {
            imagenSecundaria = this.catalogoAdminService.obtenerFotoSecundariaProducto(productoId, imagenId);
        } catch (ProductoException | ImageException e) {
            response.put("mensaje", "Error al obtener la imagen secundaria del producto");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("foto", imagenSecundaria);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Descarga la foto secundaria requerida de un producto solicitado.
     * URL: ~/api/catalogo/imagenes/productos/1/imagen/1
     * HttpMethod: GET
     * HttpStatus: OK
     * @param productoId Long id del producto.
     * @param imagenId Long id de la imagen secundaria a descargar.
     * @return ResponseEntity con los bytes que representan el archivo de la imagen secundaria.
     */
    @GetMapping("/productos/{productoId}/imagen/{imagenId}/descargar")
    public ResponseEntity<?> descargarFotoSecundariaProducto(@PathVariable Long productoId,
                                                             @PathVariable Long imagenId) {
        Map<String, Object> response = new HashMap<>();
        byte[] fotoBytes;
        ByteArrayResource fotoAsResource;
        String imagePath;

        try {
            fotoBytes = this.catalogoAdminService.descargarImagenSecundariaProducto(productoId, imagenId);
            imagePath = this.catalogoAdminService.obtenerPathImagenSecundaria(productoId, imagenId);
            fotoAsResource = new ByteArrayResource(fotoBytes);
        } catch (ProductoException | FileException | ImageException | AmazonS3Exception e) {
            response.put("mensaje", "Error al descargar imagen secundaria del producto");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity
                .ok()
                .header("Content-Type", "application/octet-stream")
                .header("Content-Disposition", "attachment; filename=\"" + imagePath + "\"")
                .body(fotoAsResource);
    }

    /**
     * Toma una imagen y, si pertenece al producto requerido, la elimina.
     * URL: ~/api/catalogo/imagenes/productos/1/imagen/1
     * HttpMethod: DELETE
     * HttpStatus: OK
     * @param productoId Long id del producto al que pertenece la imagen a eliminar.
     * @param imagenId Long id de la imagen a eliminar.
     * @return ResponseEntity con mensaje de ??xito.
     */
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/productos/{productoId}/imagen/{imagenId}")
    public ResponseEntity<?> eliminarFotoSecundariaProducto(@PathVariable Long productoId,
                                                            @PathVariable Long imagenId) {
        Map<String, Object> response = new HashMap<>();
        String msg;

        try {
            this.catalogoAdminService.eliminarFotoSecundariaProducto(productoId, imagenId);
            msg = "??Imagen secundaria eliminado con ??xito!";
        } catch (ProductoException | FileException | ImageException | AmazonS3Exception e) {
            response.put("mensaje", "Error al eliminar la imagen secundaria del producto");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", msg);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
