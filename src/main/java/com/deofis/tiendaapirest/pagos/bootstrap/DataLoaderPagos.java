package com.deofis.tiendaapirest.pagos.bootstrap;

import com.deofis.tiendaapirest.pagos.entities.MedioPago;
import com.deofis.tiendaapirest.pagos.entities.MedioPagoEnum;
import com.deofis.tiendaapirest.pagos.repositories.MedioPagoRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class DataLoaderPagos implements CommandLineRunner {

    private final MedioPagoRepository medioPagoRepository;

    @Transactional
    @Override
    public void run(String... args) {

        List<MedioPago> mediosPago = new ArrayList<>();
        if (this.medioPagoRepository.findByNombre(MedioPagoEnum.EFECTIVO).isEmpty()) {
            MedioPago efectivo = new MedioPago();
            efectivo.setNombre(MedioPagoEnum.EFECTIVO);
            mediosPago.add(efectivo);
        }

        if (this.medioPagoRepository.findByNombre(MedioPagoEnum.MERCADO_PAGO).isEmpty()) {
            MedioPago mercadoPago = new MedioPago();
            mercadoPago.setNombre(MedioPagoEnum.MERCADO_PAGO);
            mediosPago.add(mercadoPago);
        }

        if (this.medioPagoRepository.findByNombre(MedioPagoEnum.PAYPAL).isEmpty()) {
            MedioPago paypal = new MedioPago();
            paypal.setNombre(MedioPagoEnum.PAYPAL);
            mediosPago.add(paypal);
        }

        if (this.medioPagoRepository.findByNombre(MedioPagoEnum.TRANSFERENCIA_BANCARIA).isEmpty()) {
            MedioPago transferenciaBancaria = new MedioPago();
            transferenciaBancaria.setNombre(MedioPagoEnum.TRANSFERENCIA_BANCARIA);
            mediosPago.add(transferenciaBancaria);
        }

        this.medioPagoRepository.saveAll(mediosPago);
    }
}
