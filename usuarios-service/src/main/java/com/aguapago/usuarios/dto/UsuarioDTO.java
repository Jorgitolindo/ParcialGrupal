package com.aguapago.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para enviar información de usuario
 * NO incluye información sensible como passwordHash
 * Se usa para queries de GraphQL
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String rol;
    private Boolean activo;
    private LocalDateTime createdAt;

    /**
     * Constructor desde entidad Usuario
     * Convierte Usuario → UsuarioDTO
     */
    public static UsuarioDTO fromEntity(com.aguapago.usuarios.entity.Usuario usuario) {
        return new UsuarioDTO(
            usuario.getId(),
            usuario.getNombre(),
            usuario.getApellido(),
            usuario.getEmail(),
            usuario.getRol(),
            usuario.getActivo(),
            usuario.getCreatedAt()
        );
    }
}