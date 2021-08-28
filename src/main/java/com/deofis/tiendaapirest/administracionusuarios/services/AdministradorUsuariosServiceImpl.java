package com.deofis.tiendaapirest.administracionusuarios.services;

import com.deofis.tiendaapirest.administracionusuarios.entities.CambioHabilitacionUsuario;
import com.deofis.tiendaapirest.administracionusuarios.entities.CambioRol;
import com.deofis.tiendaapirest.administracionusuarios.repositories.CambioHabilitacionRepository;
import com.deofis.tiendaapirest.administracionusuarios.repositories.CambioRolRepository;
import com.deofis.tiendaapirest.autenticacion.dto.CambioRolRequest;
import com.deofis.tiendaapirest.autenticacion.dto.UsuarioDTO;
import com.deofis.tiendaapirest.autenticacion.entities.Rol;
import com.deofis.tiendaapirest.autenticacion.entities.Usuario;
import com.deofis.tiendaapirest.autenticacion.exceptions.AutenticacionException;
import com.deofis.tiendaapirest.autenticacion.exceptions.PasswordException;
import com.deofis.tiendaapirest.autenticacion.exceptions.RegistrosException;
import com.deofis.tiendaapirest.autenticacion.repositories.RolRepository;
import com.deofis.tiendaapirest.autenticacion.repositories.UsuarioRepository;
import com.deofis.tiendaapirest.autenticacion.services.AutenticacionService;
import com.deofis.tiendaapirest.clientes.entities.Cliente;
import com.deofis.tiendaapirest.clientes.services.ClienteService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdministradorUsuariosServiceImpl implements AdministradorUsuariosService {

    private final AutenticacionService autenticacionService;
    private final ClienteService clienteService;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final CambioRolRepository cambioRolRepository;
    private final CambioHabilitacionRepository cambioHabilitacionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Rol> listarRoles() {
        return this.rolRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<UsuarioDTO> listarUsuarios() {
        return this.usuarioRepository.findAllByOrderByFechaCreacionDesc()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<UsuarioDTO> obtenerAdministradores() {
        Rol admin = this.rolRepository.findByNombre("ROLE_ADMIN").orElse(null);
        return this.usuarioRepository.findAllByRol(admin)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public UsuarioDTO getUsuario(String usuarioEmail) {
        Usuario usuario = this.usuarioRepository.findByEmail(usuarioEmail)
                .orElseThrow(() -> new AutenticacionException("No existe usuario con email: " +
                        usuarioEmail));

        return this.mapToDTO(usuario);
    }


    @Transactional
    @Override
    public UsuarioDTO crear(Usuario usuario) {

        if (this.usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new AutenticacionException("Ya existe un usuario registrado con el email: " +
                    usuario.getEmail());
        }

        if (usuario.getPassword().length() <= 7) {
            throw new PasswordException("La contraseña debe tener al menos 8 caracteres.");
        }

        Usuario nuevoUsuario = Usuario.builder()
                .email(usuario.getEmail())
                .password(passwordEncoder.encode(usuario.getPassword()))
                .enabled(true)
                .fechaCreacion(new Date())
                .rol(usuario.getRol())
                .build();

        try {
            this.usuarioRepository.save(nuevoUsuario);
        } catch (DataIntegrityViolationException e) {
            throw new AutenticacionException("Ya existe usuario");
        }

        return this.mapToDTO(nuevoUsuario);
    }

    @Transactional
    @Override
    public CambioRol cambiarRol(CambioRolRequest cambioRolRequest) {
        if (!this.autenticacionService.estaLogueado()) {
            throw new AutenticacionException("Error de seguridad. Debes estar logueado como admin para " +
                    "realizar esta acción");
        }

        Rol nuevoRol = this.rolRepository.findById(cambioRolRequest.getRolId())
                .orElseThrow(() -> new AutenticacionException("No existe el rol con id: " +
                        cambioRolRequest.getRolId()));

        Usuario admin = this.autenticacionService.getUsuarioActual();
        Usuario usuarioCambioRol = this.usuarioRepository.findByEmail(cambioRolRequest.getUsuarioEmail())
                .orElseThrow(() -> new AutenticacionException("No existe el usuario con email: " +
                        cambioRolRequest.getUsuarioEmail()));

        usuarioCambioRol.setRol(nuevoRol);

        CambioRol registro = this.registrarCambioRol(admin.getEmail(),
                usuarioCambioRol.getEmail(),
                nuevoRol.getNombre());
        this.usuarioRepository.save(usuarioCambioRol);

        return registro;
    }

    @Transactional
    @Override
    public CambioHabilitacionUsuario deshabilitar(String usuarioEmail) {
        Usuario admin = this.autenticacionService.getUsuarioActual();
        Usuario usuario = this.getUsuarioByEmail(usuarioEmail);
        String accion = "Deshabilitacion";

        if (admin.getEmail().equals(usuario.getEmail())) {
            throw new RegistrosException("No podes deshabilitarte a vos mismo.");
        }

        usuario.setEnabled(false);

        CambioHabilitacionUsuario registro = this.registrarCambioHabilitacion(admin.getEmail(),
                usuario.getEmail(), accion);

        this.usuarioRepository.save(usuario);
        return registro;
    }

    @Transactional
    @Override
    public CambioHabilitacionUsuario habilitar(String usuarioEmail) {
        Usuario admin = this.autenticacionService.getUsuarioActual();
        Usuario usuario = this.getUsuarioByEmail(usuarioEmail);
        String accion = "Habilitacion";

        usuario.setEnabled(true);

        CambioHabilitacionUsuario registro = this.registrarCambioHabilitacion(admin.getEmail(),
                usuario.getEmail(), accion);

        this.usuarioRepository.save(usuario);
        return registro;
    }

    @Override
    public List<CambioRol> listarRegistrosCambioRol() {
        return this.cambioRolRepository.findAllByOrderByFechaCambioAsc();
    }

    @Override
    public List<CambioHabilitacionUsuario> listarRegistrosHabilitacion() {
        return this.cambioHabilitacionRepository.findAllByOrderByFechaCambioAsc();
    }

    @Override
    public List<Cliente> listarClientes() {
        return this.clienteService.findAll();
    }

    private CambioHabilitacionUsuario registrarCambioHabilitacion(String admin, String usuario, String accion) {
        return this.cambioHabilitacionRepository.save(CambioHabilitacionUsuario.builder()
                .usuarioAdmin(admin)
                .usuarioCambioHabilitacion(usuario)
                .accion(accion)
                .fechaCambio(new Date())
                .build());
    }

    private Usuario getUsuarioByEmail(String usuarioEmail) {
        return this.usuarioRepository.findByEmail(usuarioEmail)
                .orElseThrow(() -> new AutenticacionException("No existe el usuario con email: "
                        + usuarioEmail));
    }

    private UsuarioDTO mapToDTO(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .enabled(usuario.isEnabled())
                .fechaCreacion(usuario.getFechaCreacion())
                .authProvider(usuario.getAuthProvider())
                .providerId(usuario.getProviderId())
                .rol(usuario.getRol().getNombre())
                .build();
    }

    private CambioRol registrarCambioRol(String admin, String usuario, String nuevoRol) {
        return this.cambioRolRepository.save(CambioRol.builder()
                .usuarioAdmin(admin)
                .usuarioCambioRol(usuario)
                .nuevoRol(nuevoRol)
                .fechaCambio(new Date())
                .build());
    }
}
