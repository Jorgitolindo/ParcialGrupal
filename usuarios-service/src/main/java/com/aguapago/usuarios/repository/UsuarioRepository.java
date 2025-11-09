package com.aguapago.usuarios.repository;

import com.aguapago.usuarios.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Usuario
 * 
 * Proporciona métodos para acceder y manipular datos de usuarios
 * en la base de datos PostgreSQL.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su email
     * @param email el email del usuario
     * @return Optional con el usuario si existe
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Verifica si existe un usuario con el email dado
     * @param email el email a verificar
     * @return true si existe, false si no
     */
    Boolean existsByEmail(String email);

    /**
     * Busca todos los usuarios con un rol específico
     * @param rol el rol a buscar ("ADMIN" o "CLIENTE")
     * @return lista de usuarios con ese rol
     */
    List<Usuario> findByRol(String rol);

    /**
     * Busca usuarios por estado (activo/inactivo)
     * @param activo true para activos, false para inactivos
     * @return lista de usuarios con ese estado
     */
    List<Usuario> findByActivo(Boolean activo);

    /**
     * Busca usuarios por rol y estado
     * @param rol el rol a buscar
     * @param activo el estado a buscar
     * @return lista de usuarios que cumplen ambas condiciones
     */
    List<Usuario> findByRolAndActivo(String rol, Boolean activo);
}