package com.deofis.tiendaapirest.perfiles.services;

import com.deofis.tiendaapirest.autenticacion.entities.Usuario;
import com.deofis.tiendaapirest.autenticacion.exceptions.AutenticacionException;
import com.deofis.tiendaapirest.autenticacion.repositories.UsuarioRepository;
import com.deofis.tiendaapirest.autenticacion.security.UserPrincipal;
import com.deofis.tiendaapirest.carrito.entities.Carrito;
import com.deofis.tiendaapirest.carrito.entities.DetalleCarrito;
import com.deofis.tiendaapirest.carrito.exceptions.CarritoException;
import com.deofis.tiendaapirest.carrito.repositories.CarritoRepository;
import com.deofis.tiendaapirest.carrito.repositories.DetalleCarritoRepository;
import com.deofis.tiendaapirest.clientes.entities.Cliente;
import com.deofis.tiendaapirest.clientes.entities.Direccion;
import com.deofis.tiendaapirest.clientes.services.ClienteService;
import com.deofis.tiendaapirest.favoritos.entities.Favorito;
import com.deofis.tiendaapirest.favoritos.repositories.FavoritosRepository;
import com.deofis.tiendaapirest.perfiles.entities.Perfil;
import com.deofis.tiendaapirest.perfiles.exceptions.PerfilesException;
import com.deofis.tiendaapirest.perfiles.repositories.PerfilRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PerfilServiceImpl implements PerfilService {

    private final PerfilRepository perfilRepository;
    private final CarritoRepository carritoRepository;
    private final DetalleCarritoRepository detalleCarritoRepository;
    private final FavoritosRepository favoritosRepository;
    private final ClienteService clienteService;
    private final UsuarioRepository usuarioRepository;

    @Override
    public void cargarPerfil(Cliente cliente, String usuarioEmail) {
        Usuario usuario = this.usuarioRepository.findByEmail(usuarioEmail)
                .orElseThrow(() -> new PerfilesException("Error al crear el perfil del usuario"));
        cliente.setEmail(usuario.getEmail());
        Cliente clienteCargado = this.clienteService.crear(cliente);
        Carrito carrito = Carrito.builder()
                .fechaCreacion(new Date())
                .useremail(usuarioEmail)
                .items(new ArrayList<>())
                .build();
        // Cascade: All : no hace falta --> this.carritoRepository.save(carrito);

        Favorito favorito = Favorito.builder()
                .fechaCreacion(new Date())
                .useremail(usuarioEmail)
                .items(new ArrayList<>()).build();

        Perfil perfil = Perfil.builder()
                .usuario(usuario)
                .cliente(clienteCargado)
                .carrito(carrito)
                .favorito(favorito)
                .build();

        try {
            this.save(perfil);
        } catch (DataIntegrityViolationException e) {
            throw new PerfilesException("El usuario ya tiene asignado datos de cliente a su perfil");
        }
    }

    @Override
    public void cargarPerfil(Cliente cliente, Usuario usuario) {
        Carrito carrito = Carrito.builder()
                .fechaCreacion(new Date())
                .useremail(usuario.getEmail())
                .items(new ArrayList<>())
                .build();

        Favorito favorito = Favorito.builder()
                .fechaCreacion(new Date())
                .useremail(usuario.getEmail())
                .items(new ArrayList<>()).build();

        Perfil perfil = Perfil.builder()
                .usuario(usuario)
                .cliente(cliente)
                .carrito(carrito)
                .favorito(favorito)
                .build();

        try {
            this.save(perfil);
        } catch (DataIntegrityViolationException e) {
            throw new PerfilesException("El usuario ya tiene asignado datos de cliente a su perfil");
        }
    }

    @Override
    public Cliente actualizarDatosCliente(Cliente cliente) {
        Perfil perfil =this.obtenerPerfilConUsuario(this.getUsuarioActual());
        Cliente clienteActualizado = this.clienteService.actualizar(cliente, perfil.getCliente().getId());
        perfil.setCliente(clienteActualizado);
        this.save(perfil);

        return clienteActualizado;
    }

    @Override
    public Perfil obtenerPerfil() {
        return this.obtenerPerfilConUsuario(this.getUsuarioActual());
    }

    @Override
    public Cliente obtenerDatosCliente() {
        Perfil perfil = this.obtenerPerfilConUsuario(this.getUsuarioActual());
        return this.clienteService.obtenerCliente(perfil.getCliente().getId());
    }

    @Override
    public Cliente actualizarDireccionCliente(Direccion direccion) {
        Cliente clienteActual = this.obtenerDatosCliente();
        return this.clienteService.actualizarDireccion(clienteActual, direccion);
    }

    @Transactional(readOnly = true)
    @Override
    public Carrito obtenerCarrito() {
        if (this.obtenerPerfil().getCarrito() == null) {
            throw new PerfilesException("Error al cargar el carrito: no se guardo correctamente al crear " +
                    "el perfil");
        }

        return this.carritoRepository.findById(this.obtenerPerfil().getCarrito().getId())
                .orElseThrow(() -> new CarritoException("No existe el carrito para el perfil."));
    }

    /*
    MAL IMPLEMENTADO: La compra y el carrito se combinan en el FRONT, y se registra una sola vez
    la compra, que es al finalizarla y haber cargado todos sus datos necesarios.
    @Transactional
    @Override
    public Operacion registrarCompra() throws CarritoException, PerfilesException {
        Carrito carrito = this.obtenerCarrito();
        Operacion compra = Operacion.builder()
                .cliente(this.obtenerPerfil().getCliente())
                .items(new ArrayList<>())
                .build();

        for (DetalleCarrito detalleCarrito: carrito.getItems()) {
            DetalleOperacion item = new DetalleOperacion();
            Producto producto = this.productoRepository.findById(detalleCarrito.getProducto().getId())
                    .orElseThrow(() -> new ProductoException("Error al obtener producto"));
            item.setProducto(producto);
            item.setCantidad(detalleCarrito.getCantidad());
            compra.getItems().add(item);
        }

        Operacion compraGuardada = this.operacionService.registrar(compra);
        Perfil perfil = this.perfilRepository.findByUsuario(this.autenticacionService.getUsuarioActual())
                .orElseThrow(() -> new PerfilesException("Error al obtener el perfil del usuario."));
        perfil.getCompras().add(compraGuardada);
        this.perfilRepository.save(perfil);

        return compraGuardada;
    }

     */

    @Override
    public void vaciarCarrito() {
        Carrito carrito = this.obtenerCarrito();

        for (DetalleCarrito item: carrito.getItems()) {
            this.detalleCarritoRepository.delete(item);
        }

        carrito.getItems().clear();
        this.carritoRepository.save(carrito);
    }

    @Transactional(readOnly = true)
    @Override
    public Favorito obtenerFavoritos() {
        Perfil perfil = this.obtenerPerfil();
        if (perfil.getFavorito() == null) {
            throw new PerfilesException("Error al obtener los favoritos: Favoritos no se cargo" +
                    "correctamente al crear el perfil");
        }

        return this.favoritosRepository.findById(perfil.getFavorito().getId())
                .orElseThrow(() -> new PerfilesException("No existe el objeto favoritos asociado" +
                        "al perfil"));
    }

    private Usuario getUsuarioActual() {
        if (!estaLogueado())
            throw new AutenticacionException("Usuario no logueado en el sistema");

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return this.usuarioRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new AutenticacionException("Usuario no encontrado: " +
                        principal.getUsername()));
    }

    private boolean estaLogueado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Perfil> findAll() {
        System.out.println("No corresponde...");
        return null;
    }

    @Override
    public Perfil findById(Long aLong) {
        System.out.println("No corresponde...");
        return null;
    }

    @Transactional
    @Override
    public Perfil save(Perfil object) {
        return this.perfilRepository.save(object);
    }

    @Override
    public void delete(Perfil object) {
        System.out.println("No corresponde...");
    }

    @Override
    public void deleteById(Long aLong) {
        System.out.println("No corresponde...");
    }

    @Transactional(readOnly = true)
    @Override
    public Perfil obtenerPerfilConUsuario(Usuario usuario) {
        return this.perfilRepository.findByUsuario(usuario)
                .orElseThrow(() -> new AutenticacionException("No existe un perfil para el usuario: " +
                usuario.getEmail()));
    }
}
