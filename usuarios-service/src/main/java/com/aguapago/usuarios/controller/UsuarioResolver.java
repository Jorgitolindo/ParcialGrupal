package com.aguapago.usuarios.controller;

import com.aguapago.usuarios.dto.RegistroUsuarioInput;
import com.aguapago.usuarios.dto.ActualizarUsuarioInput;
import com.aguapago.usuarios.dto.UsuarioDTO;
import com.aguapago.usuarios.entity.Usuario;
import com.aguapago.usuarios.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import com.aguapago.usuarios.service.AuthService;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Resolver GraphQL para operaciones de Usuario
 * 
 * Maneja todas las queries y mutations relacionadas con usuarios:
 * - Consultas (obtener usuarios)
 * - Registro y actualización
 * - Cambio de contraseña
 * - Activar/desactivar usuarios
 */
@Controller
public class UsuarioResolver {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
private AuthService authService; 

    // ========================================
    // QUERIES (Consultas)
    // ========================================

    /**
 /**
 * Obtiene todos los usuarios del sistema
 * Solo ADMIN puede ejecutar esta operación
 * GraphQL: obtenerTodosLosUsuarios(usuarioEmail: String!)
 */
@QueryMapping
public List<UsuarioDTO> obtenerTodosLosUsuarios(@Argument String usuarioEmail) {
    // Validar que sea ADMIN
    if (!authService.tieneRol(usuarioEmail, "ADMIN")) {
        // Devolver lista vacía en lugar de error
        return List.of(); // Lista vacía
    }
    
    return usuarioService.obtenerTodosLosUsuarios()
        .stream()
        .map(UsuarioDTO::fromEntity)
        .collect(Collectors.toList());
}

    /**
 * Obtiene un usuario por su ID
 * ADMIN puede ver cualquier usuario
 * CLIENTE solo puede ver su propia información
 * GraphQL: obtenerUsuarioPorId(id: ID!, usuarioEmail: String!)
 */
@QueryMapping
public UsuarioDTO obtenerUsuarioPorId(@Argument Long id, @Argument String usuarioEmail) {
    // Verificar si es ADMIN
    boolean esAdmin = authService.tieneRol(usuarioEmail, "ADMIN");
    
    // Verificar si es el propietario
    boolean esPropietario = authService.esPropietario(usuarioEmail, id);
    
    // Validar permisos
    if (!esAdmin && !esPropietario) {
        return null;
    }
    
    return usuarioService.obtenerUsuarioPorId(id)
        .map(UsuarioDTO::fromEntity)
        .orElse(null);
}

    /**
     * Obtiene un usuario por su email
     * GraphQL: obtenerUsuarioPorEmail(email: String!)
     */
    @QueryMapping
    public UsuarioDTO obtenerUsuarioPorEmail(@Argument String email) {
        return usuarioService.obtenerUsuarioPorEmail(email)
            .map(UsuarioDTO::fromEntity)
            .orElse(null);
    }

    /**
     * Obtiene usuarios por rol
     * GraphQL: obtenerUsuariosPorRol(rol: String!)
     */
    @QueryMapping
    public List<UsuarioDTO> obtenerUsuariosPorRol(@Argument String rol) {
        return usuarioService.obtenerUsuariosPorRol(rol)
            .stream()
            .map(UsuarioDTO::fromEntity)
            .collect(Collectors.toList());
    }

    // ========================================
    // MUTATIONS (Modificaciones)
    // ========================================

    /**
 * Registra un nuevo usuario
 * Si el rol es CLIENTE, crea automáticamente su perfil
 * GraphQL: registrarUsuario(input: RegistroUsuarioInput!)
 */
@MutationMapping
public UsuarioDTO registrarUsuario(@Argument RegistroUsuarioInput input) {
    Usuario usuario = usuarioService.registrarUsuario(
        input.getNombre(),
        input.getApellido(),
        input.getEmail(),
        input.getPassword(),
        input.getRol(),
        input.getDireccion(),           // Nuevo
        input.getTelefono(),            // Nuevo
        input.getDocumentoIdentidad()   // Nuevo
    );
    return UsuarioDTO.fromEntity(usuario);
}

   /**
 * Actualiza un usuario
 * GraphQL: actualizarUsuario(id: ID!, input: ActualizarUsuarioInput!)
 */
@MutationMapping
public UsuarioDTO actualizarUsuario(@Argument Long id, 
                                    @Argument ActualizarUsuarioInput input) {
    Usuario usuario = usuarioService.actualizarUsuario(
        id, 
        input.getNombre(), 
        input.getApellido(), 
        input.getEmail()
    );
    return UsuarioDTO.fromEntity(usuario);
}

    /**
     * Cambia la contraseña de un usuario
     * GraphQL: cambiarPassword(id: ID!, nuevaPassword: String!)
     */
    @MutationMapping
    public UsuarioDTO cambiarPassword(@Argument Long id, @Argument String nuevaPassword) {
        Usuario usuario = usuarioService.cambiarPassword(id, nuevaPassword);
        return UsuarioDTO.fromEntity(usuario);
    }

    /**
     * Activa o desactiva un usuario
     * GraphQL: cambiarEstadoUsuario(id: ID!, activo: Boolean!)
     */
    @MutationMapping
    public UsuarioDTO cambiarEstadoUsuario(@Argument Long id, @Argument Boolean activo) {
        Usuario usuario = usuarioService.cambiarEstadoUsuario(id, activo);
        return UsuarioDTO.fromEntity(usuario);
    }
}