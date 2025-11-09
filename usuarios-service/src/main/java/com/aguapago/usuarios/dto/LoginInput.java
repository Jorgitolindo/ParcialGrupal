package com.aguapago.usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO para recibir credenciales de login
 */
@Data
public class LoginInput {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    public LoginInput() {
    }

    public LoginInput(String email, String password) {
        this.email = email;
        this.password = password;
    }
}