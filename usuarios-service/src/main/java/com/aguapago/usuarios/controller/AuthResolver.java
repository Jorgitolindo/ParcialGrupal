package com.aguapago.usuarios.controller;

import com.aguapago.usuarios.dto.AuthResponse;
import com.aguapago.usuarios.dto.LoginInput;
import com.aguapago.usuarios.entity.Usuario;
import com.aguapago.usuarios.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

/**
 * Resolver GraphQL para autenticación
 * Maneja el login simple sin JWT
 */
@Controller
public class AuthResolver {

    @Autowired
    private AuthService authService;

    /**
     * Login simple
     * Si el login falla, devuelve null en los campos y mensaje de error
     * Si el login es exitoso, devuelve los datos del usuario
     */
    @MutationMapping
    public AuthResponse login(@Argument LoginInput input) {
        try {
            Usuario usuario = authService.login(input.getEmail(), input.getPassword());
            return AuthResponse.fromUsuario(usuario);
        } catch (Exception e) {
            // Devolver respuesta con solo el mensaje de error
            return new AuthResponse(null, null, null, null, null, null, 
                                   "Error: " + e.getMessage());
        }
    }
}