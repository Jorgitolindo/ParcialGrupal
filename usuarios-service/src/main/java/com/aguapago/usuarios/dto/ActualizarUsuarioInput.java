package com.aguapago.usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para actualizar información de usuario
 */
@Data
public class ActualizarUsuarioInput {

    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;

    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String apellido;

    @Email(message = "El email debe ser válido")
    @Size(max = 150, message = "El email no puede tener más de 150 caracteres")
    private String email;

    public ActualizarUsuarioInput() {
    }

    public ActualizarUsuarioInput(String nombre, String apellido, String email) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
    }
}