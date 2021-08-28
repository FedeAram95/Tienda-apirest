package com.deofis.tiendaapirest.ecommerce.services;

import com.deofis.tiendaapirest.ecommerce.dto.BannerDto;
import com.deofis.tiendaapirest.ecommerce.entities.Banner;
import com.deofis.tiendaapirest.utils.crud.CrudService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Servicio que se encarga de subir y obtener las imágenes de los distintos
 * Banners cargados en el sistema.
 * <br>
 * Sólo pueden existir 4 (Cuatro) Banners dentro del Banner Config.
 */
public interface BannerService extends CrudService<Banner, Long> {

    /**
     * Crea un nuevo Banner y le carga la imagen pasada por parámetro. Para la
     * creación del Banner se debe verificar:
     *                  - BannerConfig no posee más de 4 Banners
     *                  - Si BannerConfig.banners > 4 se larga excepción para
     *                  sugerir utilizar el servicio de actualizar banner
     * @param bannerDto {@link BannerDto} a mapear que contiene los datos de action url
     *                                   y orden del banner.
     * @param imagen MultipartFile con la imagen a cargar en el banner.
     * @return {@link Banner} creado, con su imagen subida y guardado en la base de datos.
     */
    Banner subirBanner(BannerDto bannerDto, MultipartFile imagen);

    /**
     * Actualiza la imagen que posee un banner ya creado.
     * @param imagen MultipartFile archivo con la nueva imagen.
     * @param bannerId Long id del banner a actualizar.
     * @return Banner actualizado y guardado en la base de datos.
     */
    Banner actualizarImagenBanner(MultipartFile imagen, Long bannerId);

    /**
     * Actualiza los el action url del banner.
     * @param bannerId Long id del Banner.
     * @param actionUrl String nuevo action url asociado al banner.
     * @return {@link Banner} actualizado y guardado en la base de datos.
     */
    Banner actualizarActionUrl(Long bannerId, String actionUrl);

    /**
     * Intercambia el orden de dos Banners entre sí.
     * @param bannerId1 Long id del banner 1.
     * @param bannerId2 Long id del banner 2.
     */
    void cambiarOrdenbanners(Long bannerId1, Long bannerId2);

    /**
     * Obtiene todos los banners creados.
     * @return List de todos los banners
     */
    List<Banner> obtenerBanners();

    /**
     * Obtiene un banner requerido a través de su id.
     * @param bannerId Long id del banner a obtener.
     * @return {@link Banner}.
     */
    Banner obtenerBanner(Long bannerId);

    /**
     * Elimina un banner.
     * @param bannerId Long id del banner.
     */
    void eliminarBanner(Long bannerId);
}
