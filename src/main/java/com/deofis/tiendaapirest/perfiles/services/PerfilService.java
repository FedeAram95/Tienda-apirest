package com.deofis.tiendaapirest.perfiles.services;

import com.deofis.tiendaapirest.autenticacion.entities.Usuario;
import com.deofis.tiendaapirest.carrito.entities.Carrito;
import com.deofis.tiendaapirest.clientes.entities.Cliente;
import com.deofis.tiendaapirest.clientes.entities.Direccion;
import com.deofis.tiendaapirest.favoritos.entities.Favorito;
import com.deofis.tiendaapirest.perfiles.entities.Perfil;
import com.deofis.tiendaapirest.utils.crud.CrudService;

public interface PerfilService extends CrudService<Perfil, Long> {

    /**
     * Se encarga de tomar los datos del cliente y asignarlos al usuario registrado, creando el nuevo perfil.
     * NO actualiza datos de un usuario ya registrado.
     * NOTA: Crear el perfil implica: Asignar cliente, usuario, carrito, compras y favoritos.
     * @param cliente Cliente del nuevo usuario con sus datos.
     * @param usuarioEmail String del email del nuevo usuario.
     */
    void cargarPerfil(Cliente cliente, String usuarioEmail);

    /**
     * Se encarga de tomar los datos  del cliente y usuario, asociarlos y crear el nuevo perfil.
     * NOTA: Crear el perfil implica: Asignar cliente, usuario, carrito, compras y favoritos.
     * @param cliente Cliente del nuevo usuario con sus datos.
     * @param usuario Usuario usuario nuevo.
     */
    void cargarPerfil(Cliente cliente, Usuario usuario);

    /**
     * Actualiza los datos de cliente asociados al perfil del usuario logueado.
     * @param clienteActualizado Cliente datos del cliente actualizado.
     * @return {@link Cliente} datos del perfil luego del cambio.
     */
    Cliente actualizarDatosCliente(Cliente clienteActualizado);

    /**
     * Método que se encarga de obtener y devolver el Perfil actual del usuario logueado
     * en el sistema.
     * @return {@link Perfil}.
     */
    Perfil obtenerPerfil();

    /**
     * Método que obtiene y devuelve el perfil del usuario pasado por parámetro
     * @param usuario {@link Usuario} usuario a obtener su perfil.
     * @return {@link Perfil} del usuario.
     */
    Perfil obtenerPerfilConUsuario(Usuario usuario);

    /**
     * Obtener datos del cliente del usuario logueado.
     * @return Cliente datos del cliente del usuario logueado.
     */
    Cliente obtenerDatosCliente();

    /**
     * Servicio que actualiza la dirección del cliente, para el usuario logueado
     * en el sistema actualmente.
     * @param direccion {@link Direccion} nueva dirección.
     * @return {@link Cliente} cliente con la dirección actualizada.
     */
    Cliente actualizarDireccionCliente(Direccion direccion);

    /**
     * Obtiene el carrito del perfil actual.
     * @return Carrito del perfil.
     */
    Carrito obtenerCarrito();

    /**
     * Llama al carrito service para vaciarlo.
     */
    void vaciarCarrito();

    /**
     * Obtiene el objeto Favoritos que pertenece al usuario logueado.
     * @return Favoritos del perfil.
     */
    Favorito obtenerFavoritos();
}
