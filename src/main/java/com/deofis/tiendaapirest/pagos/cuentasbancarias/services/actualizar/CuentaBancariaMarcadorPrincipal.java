package com.deofis.tiendaapirest.pagos.cuentasbancarias.services.actualizar;

import com.deofis.tiendaapirest.pagos.cuentasbancarias.entities.CuentaBancaria;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.exceptions.CuentaBancariaException;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.services.CuentaBancariaDSGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuentaBancariaMarcadorPrincipal implements CuentaBancariaMarcadorPrincipalService {

    private final CuentaBancariaDSGateway cuentaBancariaDSGateway;

    @Autowired
    public CuentaBancariaMarcadorPrincipal(CuentaBancariaDSGateway cuentaBancariaDSGateway) {
        this.cuentaBancariaDSGateway = cuentaBancariaDSGateway;
    }

    @Override
    public void marcarCuentaPrincipal(Long cuentaId) throws CuentaBancariaException {
        this.desmarcarOtraCuenta();
        CuentaBancaria cuentaBancaria = this.cuentaBancariaDSGateway.findById(cuentaId);
        cuentaBancaria.marcarPrincipal();
        // Podríamos hacer que desmarcarOtraCuenta() devuelva objeto desmarcado, juntarlo en un array list
        // con cuentaBancaria y hacer un update en batch. Pero no es necesario, ya que nos aseguramos que
        // el rendimiento no será un problema (solo una cuenta puede ser principal, el ciclo corta al encontrarlo).
        this.cuentaBancariaDSGateway.save(cuentaBancaria);
    }

    /**
     * Método que desmarca todas las cuentas como NO principales. Recorre todas las cuentas existentes
     * y las desmarca.
     */
    private void desmarcarOtraCuenta() {
        // Se toma como verdadero: solo hay una cuenta principal. La encuentra, desmarca, guarda y termina
        // el ciclo del for, y la invocación misma del método.
        List<CuentaBancaria> cuentas = this.cuentaBancariaDSGateway.findAll();
        for (CuentaBancaria currentCuenta: cuentas) {
            if (currentCuenta.isPrincipal()) {
                currentCuenta.desmarcarPrincipal();
                this.cuentaBancariaDSGateway.save(currentCuenta);
                break;
            }
        }
    }
}
