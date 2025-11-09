package com.aguapago.usuarios.service;

import com.aguapago.usuarios.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Servicio de Autenticación Simple
 * Valida credenciales sin usar JWT
 */
@Service
public class AuthService {

    @Autowired
    private UsuarioService usuarioService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Valida credenciales de login
     * @param email email del usuario
     * @param password contraseña en texto plano
     * @return el usuario si las credenciales son válidas
     * @throws RuntimeException si las credenciales son inválidas
     */
    public Usuario login(String email, String password) {
        // Buscar usuario por email
        Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorEmail(email);
        
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }
        
        Usuario usuario = usuarioOpt.get();
        
        // Verificar que esté activo
        if (!usuario.getActivo()) {
            throw new IllegalArgumentException("Usuario inactivo");
        }
        
        // Verificar contraseña
        if (!passwordEncoder.matches(password, usuario.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }
        
        return usuario;
    }

    /**
     * Verifica si un usuario tiene un rol específico
     */
    public boolean tieneRol(String email, String rol) {
        return usuarioService.obtenerUsuarioPorEmail(email)
            .map(u -> rol.equals(u.getRol()))
            .orElse(false);
    }

    /**
     * Verifica si un usuario es el propietario de un recurso
     */
    public boolean esPropietario(String email, Long usuarioId) {
        return usuarioService.obtenerUsuarioPorEmail(email)
            .map(u -> u.getId().equals(usuarioId))
            .orElse(false);
    }
}