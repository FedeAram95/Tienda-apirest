package com.deofis.tiendaapirest.compras.services;

import com.deofis.tiendaapirest.clientes.entities.Cliente;
import com.deofis.tiendaapirest.compras.dto.CompraPayload;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import com.deofis.tiendaapirest.operaciones.repositories.OperacionRepository;
import com.deofis.tiendaapirest.perfiles.services.PerfilService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CompraServiceImpl implements CompraService {

    private final OperacionRepository operacionRepository;
    private final PerfilService perfilService;
    private final EncontradorCompraCliente encontradorCompraCliente;

    @Transactional(readOnly = true)
    @Override
    public List<CompraPayload> historialCompras() {
        Cliente cliente = this.getClienteDelPerfilActual();
        List<Operacion> historialOperaciones = this.operacionRepository
                .findAllByClienteOrderByFechaOperacionAsc(cliente);

        return historialOperaciones
                .stream()
                .map(this::mapToCompra)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<CompraPayload> comprasEstado(String estado) {
        List<CompraPayload> comprasCliente = this.historialCompras();

        List<CompraPayload> comprasEstado = new ArrayList<>();

        for (CompraPayload compra: comprasCliente) {
            if (estado.equalsIgnoreCase(String.valueOf(compra.getEstado())))
                comprasEstado.add(compra);
        }

        return comprasEstado;
    }

    @Transactional(readOnly = true)
    @Override
    public List<CompraPayload> comprasYear(Integer year) {
        Cliente cliente = this.getClienteDelPerfilActual();

        /*
        Implementación vieja.
        LocalDate localDate = LocalDate.of(year, 1, 1);
        Date dateOfYear = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
         */

        return this.operacionRepository.operacionesByYear(cliente.getId(), year)
                .stream()
                .map(this::mapToCompra)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<CompraPayload> comprasMonth(Integer month) {
        Cliente cliente = this.getClienteDelPerfilActual();

        int actualYear = Calendar.getInstance().get(Calendar.YEAR);
        return this.operacionRepository.operacionesByMonth(cliente.getId(), actualYear, month)
                .stream()
                .map(this::mapToCompra)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public CompraPayload verCompra(Long nroOperacion) {
        Cliente cliente = this.getClienteDelPerfilActual();
        return this.mapToCompra(this.obtenerCompraDelCliente(nroOperacion, cliente));
    }

    /**
     * Mapea una {@link Operacion} con todos los datos, a una {@link CompraPayload}, con los datos
     * suficientes para consultar la compra, desde la vista del usuario.
     * @param operacion {@link Operacion}.
     * @return {@link CompraPayload}
     */
    private CompraPayload mapToCompra(Operacion operacion) {
        return CompraPayload.builder()
                .nroOperacion(operacion.getNroOperacion())
                .fechaOperacion(operacion.getFechaOperacion())
                .fechaEnvio(operacion.getFechaEnvio())
                .fechaEntrega(operacion.getFechaEntrega())
                .estado(operacion.getEstado())
                .cliente(operacion.getCliente().getApellido() + ", " + operacion.getCliente().getNombre())
                .direccionEnvio(operacion.getDireccionEnvio())
                .medioPago(operacion.getMedioPago())
                .items(operacion.getItems())
                .total(operacion.getTotal()).build();
    }

    /**
     * Devuelve una {@link Operacion} de compra a través de su número de operación, y del cliente
     * requerido.
     * @param nroOperacion Long número de operación requerida.
     * @param cliente {@link Cliente} cliente al que pertenece la operación.
     * @return {@link Operacion} requerida.
     */
    private Operacion obtenerCompraDelCliente(Long nroOperacion, Cliente cliente) {
        return this.encontradorCompraCliente.encontrarCompraCliente(nroOperacion, cliente);
    }

    /**
     * Obtiene el {@link Cliente} del perfil logueado actualmente en el sistema.
     * @return Cliente actual.
     */
    private Cliente getClienteDelPerfilActual() {
        return this.perfilService.obtenerPerfil().getCliente();
    }
}
