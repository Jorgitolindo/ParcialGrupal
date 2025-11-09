package com.aguapago.usuarios.repository;

import com.aguapago.usuarios.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad Cliente
 * 
 * Proporciona métodos para acceder y manipular datos de clientes
 * en la base de datos PostgreSQL.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Busca un cliente por su código único
     * @param codigoCliente el código del cliente (ej: "CLI-001")
     * @return Optional con el cliente si existe
     */
    Optional<Cliente> findByCodigoCliente(String codigoCliente);

    /**
     * Verifica si existe un cliente con el código dado
     * @param codigoCliente el código a verificar
     * @return true si existe, false si no
     */
    Boolean existsByCodigoCliente(String codigoCliente);

    /**
     * Busca un cliente por el ID de su usuario asociado
     * @param usuarioId el ID del usuario
     * @return Optional con el cliente si existe
     */
    Optional<Cliente> findByUsuarioId(Long usuarioId);

    /**
     * Busca un cliente por el email de su usuario asociado
     * Este método navega por la relación Usuario -> Cliente
     * @param email el email del usuario
     * @return Optional con el cliente si existe
     */
    Optional<Cliente> findByUsuarioEmail(String email);

    /**
     * Verifica si existe un cliente asociado a un usuario específico
     * @param usuarioId el ID del usuario
     * @return true si existe, false si no
     */
    Boolean existsByUsuarioId(Long usuarioId);
}