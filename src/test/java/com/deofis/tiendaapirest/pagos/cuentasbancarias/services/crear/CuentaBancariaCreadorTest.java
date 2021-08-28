package com.deofis.tiendaapirest.pagos.cuentasbancarias.services.crear;

import com.deofis.tiendaapirest.pagos.cuentasbancarias.entities.CajaAhorroEnum;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.entities.TipoCuentaBancaria;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.exceptions.CuentaBancariaException;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.facade.CuentaBancariaRequest;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.facade.CuentaBancariaResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"qa"})
@Slf4j
class CuentaBancariaCreadorTest {

    @Autowired
    CuentaBancariaCreadorService creatorService;

    CuentaBancariaRequest request;

    @BeforeEach
    void setUp() {
        request = CuentaBancariaRequest.builder()
                .nroCuenta("0000000154684652124")
                .tipo(TipoCuentaBancaria.CBU)
                .ca(CajaAhorroEnum.ARS)
                .alias("MICHAEL_PALO_ALTO")
                .titular("Juan Ramirez")
                .banco("Banco Genérico")
                .email("gavilan@example.com")
                .paisId(11L).build();
    }

    @Transactional
    @Test
    void crearNuevaCuentaBancariaExitosamente() {
        CuentaBancariaResponse response;

        try {
            response = creatorService.crearCuentaBancaria(request);
        } catch (CuentaBancariaException e) {
            e.printStackTrace();
            return;
        }

        log.info(String.valueOf(response));

        assertNotNull(response);
        assertEquals("0000000154684652124", response.getNroCuenta());
    }

    @Transactional
    @Test
    void crearCuentaExcepcionCuentaPaisExiste() {
        try {
            creatorService.crearCuentaBancaria(request);
        } catch (CuentaBancariaException e) {
            e.printStackTrace();
        }

        CuentaBancariaRequest newRequest = CuentaBancariaRequest.builder()
                .nroCuenta("00001298794564654")
                .alias("RICO_PATO_PLOMO")
                .email("placeholder@example.com")
                .paisId(11L).build();

        Exception exception = assertThrows(CuentaBancariaException.class,
                () -> creatorService.crearCuentaBancaria(newRequest));

        System.out.println("\n");
        log.info(exception.getMessage());
        System.out.println("\n");
    }

    @Transactional
    @Test
    void crearCuentaNuevaPrincipal() {
        CuentaBancariaResponse response;
        try {
            response = creatorService.crearCuentaBancaria(request);
        } catch (CuentaBancariaException e) {
            e.printStackTrace();
            return;
        }

        log.info(response.getAlias() + " : " + response.isPrincipal());
        assertTrue(response.isPrincipal());
    }

    @Transactional
    @Test
    void crearCuentaNoPrincipal() {
        try {
            creatorService.crearCuentaBancaria(request);
        } catch (CuentaBancariaException e) {
            e.printStackTrace();
            return;
        }

        CuentaBancariaRequest newRequest = CuentaBancariaRequest.builder()
                .nroCuenta("00001298794564654")
                .tipo(TipoCuentaBancaria.CBU)
                .ca(CajaAhorroEnum.ARS)
                .alias("RICO_PATO_PLOMO")
                .email("placeholder@example.com")
                .paisId(207L).build();

        CuentaBancariaResponse newResponse;
        try {
            newResponse = creatorService.crearCuentaBancaria(newRequest);
        } catch (CuentaBancariaException e) {
            e.printStackTrace();
            return;
        }

        log.info(newResponse.getAlias() + " : " + newResponse.isPrincipal());
        assertFalse(newResponse.isPrincipal());
    }
}