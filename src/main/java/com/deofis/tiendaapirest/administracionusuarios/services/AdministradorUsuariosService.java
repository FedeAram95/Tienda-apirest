package com.deofis.tiendaapirest.administracionusuarios.services;

import com.deofis.tiendaapirest.administracionusuarios.entities.CambioHabilitacionUsuario;
import com.deofis.tiendaapirest.administracionusuarios.entities.CambioRol;
import com.deofis.tiendaapirest.autenticacion.dto.CambioRolRequest;
import com.deofis.tiendaapirest.autenticacion.dto.UsuarioDTO;
import com.deofis.tiendaapirest.autenticacion.entities.Rol;
import com.deofis.tiendaapirest.autenticacion.entities.Usuario;
import com.deofis.tiendaapirest.clientes.entities.Cliente;

import java.util.List;

/**
 * Servicio para administrar los usuarios registrados en el sistema. Esto incluye: listar usuarios,
 * listar roles, obtener un usuario en concreto, crear un usuario nuevo, cambiar rol, habilitar o
 * deshabilitar usuarios.
 */

public interface AdministradorUsuariosService {

    /**
     * Lista los roles.
     * @return List con los roles.
     */
    List<Rol> listarRoles();

    /**
     * Lista los usuarios ordenados por fecha de creacon.
     * @return List con los usuarios registrados.
     */
    List<UsuarioDTO> listarUsuarios();

    /**
     * Obtiene los usuarios que son administradores del sistema.
     * @return List con los usuarios administradores.
     */
    List<UsuarioDTO> obtenerAdministradores();

    /**
     * Obtiene un usuario en particular.
     * @param usuarioEmail String email del usuario.
     * @return Datos del usuario.
     */
    UsuarioDTO getUsuario(String usuarioEmail);

    /**
     * Como administrador, permite crear un nuevo usuario ya activado, con el rol especificado.
     * @param usuario Datos del usuario completos (email, pass y el rol).
     * @return UsuarioDTO --> Datos del usuario no sensibles (sin pass).
     */
    UsuarioDTO crear(Usuario usuario);

    /**
     * Como administrador, permite cambiar el rol de un usuario. Registra esta transacción
     * con los datos: usuario que hizo el cambio, usuario al que se le cambio el rol,
     * nuevo rol del usuario y fecha del cambio de rol.
     * @param cambioRolRequest CambioRolRequest con los datos: Long id del rol nuevo, String
     *                         email del usuario.
     * @return CambioRol registro del cambio de rol.
     */
    CambioRol cambiarRol(CambioRolRequest cambioRolRequest);

    /**
     * Como administrador, permite deshabilitar un usuario, registrando la transacción con los
     * datos: usuario admin que hizo el cambio, usuario al que se deshabilita, mensaje deshabilitacion
     * y fecha del cambio.
     * @param usuarioEmail String email del usuario que se deshabilitará.
     * @return CambioHabilitacionUsuarios registro de los cambios realizados.
     */
    CambioHabilitacionUsuario deshabilitar(String usuarioEmail);

    /**
     * Como administrador, permite habilitar un usuario, registrando la transacción con los
     * datos: usuario admin que hizo el cambio, usuario al que se habilita, mensaje habilitacion
     * y fecha del cambio.
     * @param usuarioEmail String email del usuario que se habilitará.
     * @return CambioHabilitacionUsuarios registro de los cambios realizados.
     */
    CambioHabilitacionUsuario habilitar(String usuarioEmail);

    /**
     * Como administrador, quiero listar todos los registros de los cambios de roles que hayan sucedido,
     * ordenados de menor a mayor por fecha.
     * @return List listado de registros de cambio de rol.
     */
    List<CambioRol> listarRegistrosCambioRol();

    /**
     * Como administrador, quiero listar todos los registros de cambios de habilitación de usuarios,
     * ordenados de menor a mayor por fecha.
     * @return List listado de registros de habilitación de usuarios.
     */
    List<CambioHabilitacionUsuario> listarRegistrosHabilitacion();

    /**
     * Obtiene el listado de todos los {@link Cliente}s registrados en el sistema.
     * @return {@link List<Cliente>} listado con clientes.
     */
    List<Cliente> listarClientes();
}
