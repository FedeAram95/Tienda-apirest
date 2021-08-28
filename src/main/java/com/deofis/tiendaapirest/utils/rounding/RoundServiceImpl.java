package com.deofis.tiendaapirest.utils.rounding;

import org.apache.commons.math3.util.Precision;
import org.springframework.stereotype.Service;

@Service
public class RoundServiceImpl implements RoundService {

    @Override
    public Double round(Double number) {
        return Precision.round(number, 2);
    }

    @Override
    public Double truncate(Double number) {
        return Precision.round(number, 2, 1);
    }
}
