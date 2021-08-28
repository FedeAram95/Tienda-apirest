package com.deofis.tiendaapirest.catalogo.skus.services;

import com.deofis.tiendaapirest.catalogo.productos.entities.Producto;
import com.deofis.tiendaapirest.catalogo.productos.entities.PropiedadProducto;
import com.deofis.tiendaapirest.catalogo.productos.entities.ValorPropiedadProducto;
import com.deofis.tiendaapirest.catalogo.productos.exceptions.ProductoException;
import com.deofis.tiendaapirest.catalogo.productos.repositories.ValorPropiedadProductoRepository;
import com.deofis.tiendaapirest.catalogo.promociones.entities.Promocion;
import com.deofis.tiendaapirest.catalogo.skus.entities.Sku;
import com.deofis.tiendaapirest.catalogo.skus.exceptions.SkuException;
import com.deofis.tiendaapirest.catalogo.skus.repositories.SkuRepository;
import com.deofis.tiendaapirest.utils.rounding.RoundService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class SkuServiceImpl implements SkuService {

    private final SkuRepository skuRepository;
    private final ValorPropiedadProductoRepository valorPropiedadProductoRepository;
    private final GeneradorSkus generadorSkus;
    private final ValidadorCombinacionesSkus validadorCombinacionesSkus;

    private final RoundService roundService;

    @Transactional
    @Override
    public Sku crearNuevoSku(Sku sku, Producto producto) {

        if (sku.getDisponibilidad() < 0)
            throw new SkuException("La disponibilidad no puede ser menor a 0");

        if (producto.getPropiedades().size() == 0)
            throw new SkuException("El producto debe tener al menos una propiedad a la cual generar SKU");

        Sku nuevoSku = Sku.builder()
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(this.roundService.truncate(sku.getPrecio()))
                .promocion(null)
                .disponibilidad(sku.getDisponibilidad())
                .activo(true)
                .fechaCreacion(new Date())
                .foto(null)
                .defaultProducto(null)
                .producto(producto)
                .valores(new ArrayList<>())
                .valoresData("").build();

        String valoresData = "";
        List<ValorPropiedadProducto> valoresReales = new ArrayList<>();

        for (ValorPropiedadProducto valor: sku.getValores()) {
            ValorPropiedadProducto valorActual = this.getValor(valor.getId());

            valoresReales.add(valorActual);
            valoresData = valoresData.concat(valorActual.getValor().concat("/"));
        }

        nuevoSku.setValores(valoresReales);
        nuevoSku.setValoresData(valoresData);

        boolean valorValido = this.validarValores(nuevoSku, producto.getPropiedades());

        // Validamos que no exista el SKU para el producto
        if (this.existeSku(nuevoSku, producto.getSkus()))
            throw new SkuException("Error al crear Sku: Ya existe un Sku con los valores seleccionados");

        // Si existe al menos un valor no valido (no es parte de propiedad producto), tiramos excepción
        if (!valorValido)
            throw new SkuException("Existe al menos un valor de propiedad que no pertenece a a las propiedades " +
                    "del producto para el cual se desea crear nuevo sku");

        // Por ultimo, validamos que no sean valores de la misma propiedad al crear sku
        // TODO: implementar --> this.existeValoresMismaPropiedad(nuevoSku, producto.getPropiedades());

        return this.save(nuevoSku);
    }

    @Override
    public Map<String, Object> generarSkusProducto(Producto producto) {
        Map<String, Object> map = new HashMap<>();
        int cantCombinacionesGeneradas = this.generadorSkus.generarSkusProducto(producto);

        if (cantCombinacionesGeneradas == 0)
            throw new SkuException("No se generaron combinaciones: Ya existían todas las combinaciones" +
                    " posibles");

        if (cantCombinacionesGeneradas == -1) {
            throw new SkuException("No se generaron combinaciones: El producto seleccionado" +
                    " no posee propiedades");
        }

        if (cantCombinacionesGeneradas == -2) {
            throw new SkuException("No se generaron combinaciones: Las propiedades de el producto " +
                    "seleccionado no poseen valores asociados");
        }

        map.put("combinaciones", cantCombinacionesGeneradas);
        map.put("skus", producto.getSkus());
        return map;
    }

    @Transactional(readOnly = true)
    @Override
    public Sku obtenerSku(Long skuId) {
        return this.findById(skuId);
    }

    @Transactional
    @Override
    public Sku actualizarSku(Long skuId, Sku sku) {
        Sku skuActual = this.obtenerSku(skuId);

        skuActual.setNombre(sku.getNombre());
        skuActual.setDescripcion(sku.getDescripcion());
        skuActual.setPrecio(this.roundService.truncate(sku.getPrecio()));
        skuActual.setDisponibilidad(sku.getDisponibilidad());
        return this.save(skuActual);
    }

    @Transactional
    @Override
    public Sku actualizarDisponibilidad(Long skuId, Integer disponibilidad) {
        Sku skuActual = this.obtenerSku(skuId);

        skuActual.setDisponibilidad(disponibilidad);
        return this.save(skuActual);
    }

    @Transactional
    @Override
    public Sku actualizarPrecio(Long skuId, Double precio) {
        Sku sku = this.obtenerSku(skuId);
        sku.setPrecio(this.roundService.truncate(precio));
        // Seteamos el precio del default producto/producto del último sku actualizado.
        if (sku.getDefaultProducto() != null) sku.getDefaultProducto().setPrecio(this.roundService.round(precio));
        else if (sku.getProducto() != null) sku.getProducto().setPrecio(this.roundService.round(precio));
        // Si el SKU tiene promo --> actualizar el precio de oferta.
        if (sku.getPromocion() != null) this.arreglarPromocion(sku, sku.getPrecio());
        return this.save(sku);
    }

    private void arreglarPromocion(Sku sku, Double precio) {
        Promocion promoActual = sku.getPromocion();
        Double porcPromo = promoActual.getPorcentaje();

        // Calculamos el nuevo precio de promoción
        Double nuevoPrecioPromo = precio - (precio * porcPromo);
        promoActual.setPrecioOferta(this.roundService.truncate(nuevoPrecioPromo));
    }

    @Transactional
    @Override
    public void eliminarSku(Long skuId) {
        this.deleteById(skuId);
    }

    @Override
    public void darDeBaja(Sku sku) {
        if (!sku.isActivo())
            throw new SkuException("El sku con id: " + sku.getId() + " ya se encuentra dado de baja");

        sku.setActivo(false);
        this.save(sku);
    }

    @Override
    public void darDeAlta(Sku sku) {
        if (sku.isActivo())
            throw new SkuException("El sku con id: " + sku.getId() + " ya se encuentra activo");

        sku.setActivo(true);
        this.save(sku);
    }

    private ValorPropiedadProducto getValor(Long valorId) {
        return this.valorPropiedadProductoRepository.findById(valorId)
                .orElseThrow(() -> new ProductoException("No existe el valor de propiedad"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Sku> findAll() {
        return this.skuRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Sku findById(Long aLong) {
        return this.skuRepository.findById(aLong)
                .orElseThrow(() -> new SkuException("No existe el sku con id: " + aLong));
    }

    @Transactional
    @Override
    public Sku save(Sku object) {
        return this.skuRepository.save(object);
    }

    @Transactional
    @Override
    public void delete(Sku object) {
        this.deleteById(object.getId());
    }

    @Transactional
    @Override
    public void deleteById(Long aLong) {
        if (this.skuRepository.findById(aLong).isEmpty())
            throw new SkuException("No existe el sku con id: " + aLong);

        try {
            this.skuRepository.deleteById(aLong);
        } catch (DataAccessException e) {
            throw new SkuException("No se pudo eliminar el sku con id: " + aLong + " porque" +
                    " tiene referencias con otros objetos : " + e.getMessage());
        }
    }

    /**
     * Valida la existencia de un Sku, gracias a sus {@link ValorPropiedadProducto}es. Esta implementación utiliza
     * la implementación por defecto de {@link ValidadorCombinacionesSkus} para cumplir su responsabilidad.
     * @param sku {@link Sku} a validar si sus valores existen.
     * @return true: existe combinacion de valores (por ende, sku); false: no existe.
     */
    private boolean existeSku(Sku sku, List<Sku> skusProducto) {

        boolean existe = false;
        for (Sku actualSku: skusProducto) {
            if (this.validadorCombinacionesSkus.esMismaCombinacion(sku.getValores(), actualSku.getValores())) {
                log.info("sku valores" + sku.getValores());
                log.info("actual sku valores: " + actualSku.getValores());
                existe = true;
                break;
            }
        }

        return existe;
    }

    /**
     * Valida que los valores pertenecientes al sku pasado por parametro, existan dentro de las propiedades
     * pasadas por parámetro.
     * @param sku {@link Sku} a validar sus valores.
     * @param propiedades {@link List<PropiedadProducto>} listado con propiedades a las cuales deben pertenecer los valores.
     * @return boolean.
     */
    private boolean validarValores(Sku sku, List<PropiedadProducto> propiedades) {
        List<ValorPropiedadProducto> valoresSku = sku.getValores();
        // Necesitamos TODOS los valores, de TODAS las propiedades, para recorrer y validar si contiene, de cada valor
        // del sku, el valor actual de cada iteración.
        List<ValorPropiedadProducto> valoresPropiedades = new ArrayList<>();

        for (PropiedadProducto propiedad: propiedades) {
            valoresPropiedades.addAll(propiedad.getValores());
        }

        for (ValorPropiedadProducto valorActual: valoresSku) {
            if (!valoresPropiedades.contains(valorActual)) {
                return false;
            }
        }
        return true;
    }

    private boolean existeValoresMismaPropiedad(Sku sku, List<PropiedadProducto> propiedades) {
        List<ValorPropiedadProducto> valoresSku = sku.getValores();
        // TODO: implementar validación para mismo valor en propiedad
        return false;
    }
}
