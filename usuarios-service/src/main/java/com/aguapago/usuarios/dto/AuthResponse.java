package com.aguapago.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respuesta de login
 * Devuelve la información del usuario autenticado
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String rol;
    private Boolean activo;
    private String mensaje;

    /**
     * Crea respuesta desde usuario
     */
    public static AuthResponse fromUsuario(com.aguapago.usuarios.entity.Usuario usuario) {
        return new AuthResponse(
            usuario.getId(),
            usuario.getNombre(),
            usuario.getApellido(),
            usuario.getEmail(),
            usuario.getRol(),
            usuario.getActivo(),
            "Login exitoso"
        );
    }
}