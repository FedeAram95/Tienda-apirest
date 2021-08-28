package com.deofis.tiendaapirest.ecommerce.controllers;

import com.deofis.tiendaapirest.ecommerce.dto.BannerDto;
import com.deofis.tiendaapirest.ecommerce.entities.Banner;
import com.deofis.tiendaapirest.ecommerce.exceptions.BannerException;
import com.deofis.tiendaapirest.ecommerce.services.BannerService;
import com.deofis.tiendaapirest.ecommerce.services.JsonBannerConverter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class BannerController {

    private final BannerService bannerService;
    private final JsonBannerConverter jsonBannerConverter;

    /**
     * API que se encarga de crear un nuevo banner con sus datos correspondientes:
     * action url y orden, y archivo que contiene la imagen del banner a crear.
     * <br>
     * Esta API consume el servicio {@link BannerService} subirBanner para la creación del Banner.
     * URL: ~/api/banners
     * HttpMethod: POST
     * HettpStatus: CREATED
     * @param bannerDto String del banner dto a convertir en JSON, que contiene los datos
     *                  para el nuevo banner.
     * @param foto MultipartFile con el archivo imagen a subir para el nuevo Banner.
     * @return ResponseEntity<Map<String, Object>> con la response: el Banner creado.
     */
    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/banners", consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<?> crearNuevoBanner(@RequestPart String bannerDto,
                                              @RequestParam(name = "foto") MultipartFile foto) {
        Map<String, Object> response = new HashMap<>();
        Banner nuevoBanner;

        try {
            BannerDto banner = this.jsonBannerConverter.getJsonFromDto(bannerDto);
            nuevoBanner = this.bannerService.subirBanner(banner, foto);
        } catch (BannerException e) {
            response.put("mensaje", "Error al crear el nuevo banner");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("banner", nuevoBanner);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * API para actualizar sólo la imagen de un Banner requerido.
     * URL: ~/api/banners/1/imagen
     * HttpMethod: PUT
     * HttpStatus: OK
     * @param foto MultipartFile nueva imagen.
     * @param bannerId Long id del Banner.
     * @return ResponseEntity con el Banner actualizado.
     */
    @Secured("ROLE_ADMIN")
    @PutMapping("/banners/{bannerId}/imagen")
    public ResponseEntity<?> actualizarImagenBanner(@RequestParam(name = "foto") MultipartFile foto,
                                                    @PathVariable Long bannerId) {
        Map<String, Object> response = new HashMap<>();
        Banner bannerActualizado;

        try {
            bannerActualizado = this.bannerService.actualizarImagenBanner(foto, bannerId);
        } catch (BannerException e) {
            response.put("mensaje", "Error al actualizar la imagen del Banner");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("banner", bannerActualizado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * API que se encarga de actualizar el action url de un Banner requerido.
     * URL: ~/api/banners/1/actionUrl
     * HttpMethod: PUT
     * HttpStatus: OK
     * @param bannerId Long id del Banner.
     * @param actionUrl String nuevo action url.
     * @return ResponseEntity con el Banner actualizado.
     */
    @Secured("ROLE_ADMIN")
    @PutMapping("/banners/{bannerId}/actionUrl")
    public ResponseEntity<?> actualizarActionUrlBanner(@PathVariable Long bannerId,
                                                       @RequestParam String actionUrl) {
        Map<String, Object> response = new HashMap<>();
        Banner bannerActualizado;

        try {
            bannerActualizado = this.bannerService.actualizarActionUrl(bannerId, actionUrl);
        } catch (BannerException e) {
            response.put("mensaje", "Error al actualizar el action url del Banner");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("banner", bannerActualizado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * API que obtiene todos los Banners disponibles en la App.
     * URL: ~/api/banners
     * HttpMethods: GET
     * HttpStatus: OK
     * @return ResponseEntity con el List de Banners.
     */
    @GetMapping("/banners")
    public ResponseEntity<?> obtenerBanners() {
        Map<String, Object> response = new HashMap<>();
        List<Banner> banners;

        try {
            banners = this.bannerService.obtenerBanners();
        } catch (BannerException e) {
            response.put("mensaje", "Error al obtener los Banners");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("banners", banners);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * API que obtiene un banner requerido a través de su id.
     * URL: ~/api/banners/1
     * HttpMethod: GET
     * HttpStatus: OK
     * @param bannerId Long id del Banner a obtener.
     * @return ResponseEntity con el Banner.
     */
    @GetMapping("/banners/{bannerId}")
    public ResponseEntity<?> obtenerBanner(@PathVariable Long bannerId) {
        Map<String, Object> response = new HashMap<>();
        Banner banner;

        try {
            banner = this.bannerService.obtenerBanner(bannerId);
        } catch (BannerException e) {
            response.put("mensaje", "Error al obtener el Banner");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("banner", banner);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * API que elimina un Banner requerido.
     * URL: ~/api/banners/1
     * HttpMethod: DELETE
     * HttpStatus: OK
     * @param bannerId Long id del banner a eliminar.
     * @return ResponseEntity con mensaje éxito.
     */
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/banners/{bannerId}")
    public ResponseEntity<?> eliminarBanner(@PathVariable Long bannerId) {
        Map<String, Object> response = new HashMap<>();
        try {
            this.bannerService.eliminarBanner(bannerId);
        } catch (BannerException e) {
            response.put("mensaje", "Error al eliminar el banner");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "Banner eliminado con éxito");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
