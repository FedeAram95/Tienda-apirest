package com.deofis.tiendaapirest.catalogo.skus.services;

import com.deofis.tiendaapirest.catalogo.productos.entities.ValorPropiedadProducto;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class ValidadorCombinacionesSkusImpl implements ValidadorCombinacionesSkus {

    @Override
    public boolean esMismaCombinacion(List<ValorPropiedadProducto> comb1, List<ValorPropiedadProducto> comb2) {

        if (comb1.size() == comb2.size()) {
            Collection<Long> comb1Ids = CustomCollectionUtils.collect(comb1, input -> ((ValorPropiedadProducto) input).getId());

            Collection<Long> comb2Ids = CustomCollectionUtils.collect(comb2, input -> ((ValorPropiedadProducto) input).getId());

            return comb1Ids.containsAll(comb2Ids);
        }
        return false;
    }

    /**
     * Interface que sirve de ayudar al collection custom.
     * @param <K>
     */
    private interface TypedTransformer<K> extends Transformer<Object, K> {

        @Override
        K transform(Object var1);
    }

    /**
     * Clase estatica que sirve de apoyo a esMismaCombinacion().
     */
    private static class CustomCollectionUtils {
        public CustomCollectionUtils() {

        }

        public static <T> Collection<T> collect(Collection inputCollection, TypedTransformer<T> transformer) {
            return CollectionUtils.collect(inputCollection, transformer);
        }
    }
}
