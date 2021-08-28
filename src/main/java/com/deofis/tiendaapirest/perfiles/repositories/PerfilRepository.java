package com.deofis.tiendaapirest.perfiles.repositories;

import com.deofis.tiendaapirest.autenticacion.entities.Usuario;
import com.deofis.tiendaapirest.perfiles.entities.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    Optional<Perfil> findByUsuario(Usuario usuario);
}
