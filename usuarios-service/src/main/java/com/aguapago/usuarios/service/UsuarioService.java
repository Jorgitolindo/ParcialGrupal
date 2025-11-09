package com.aguapago.usuarios.service;

import com.aguapago.usuarios.entity.Usuario;
import com.aguapago.usuarios.repository.UsuarioRepository;
//import com.aguapago.usuarios.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de Usuario
 * 
 * Contiene toda la lógica de negocio relacionada con usuarios:
 * - Registro y autenticación
 * - CRUD de usuarios
 * - Validaciones
 * - Gestión de roles
 */
@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteService clienteService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ========================================
    // MÉTODOS DE CONSULTA
    // ========================================

    /**
     * Obtiene todos los usuarios del sistema
     * 
     * @return lista de todos los usuarios
     */
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * Busca un usuario por su ID
     * 
     * @param id el ID del usuario
     * @return Optional con el usuario si existe
     */
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Busca un usuario por su email
     * 
     * @param email el email del usuario
     * @return Optional con el usuario si existe
     */
    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Obtiene todos los usuarios con un rol específico
     * 
     * @param rol el rol a buscar ("ADMIN" o "CLIENTE")
     * @return lista de usuarios con ese rol
     */
    public List<Usuario> obtenerUsuariosPorRol(String rol) {
        return usuarioRepository.findByRol(rol);
    }

    /**
     * Obtiene usuarios activos o inactivos
     * 
     * @param activo true para activos, false para inactivos
     * @return lista de usuarios
     */
    public List<Usuario> obtenerUsuariosPorEstado(Boolean activo) {
        return usuarioRepository.findByActivo(activo);
    }

    // ========================================
    // MÉTODOS DE CREACIÓN
    // ========================================

    /**
     * Registra un nuevo usuario en el sistema
     * /**
     * Registra un nuevo usuario en el sistema
     * Si el rol es CLIENTE, crea automáticamente su perfil de cliente
     * 
     * @param nombre             nombre del usuario
     * @param apellido           apellido del usuario
     * @param email              email del usuario (debe ser único)
     * @param password           contraseña en texto plano (se encriptará)
     * @param rol                rol del usuario ("ADMIN" o "CLIENTE")
     * @param direccion          dirección del cliente (opcional)
     * @param telefono           teléfono del cliente (opcional)
     * @param documentoIdentidad documento del cliente (opcional)
     * @return el usuario creado
     * @throws IllegalArgumentException si los datos son inválidos o el email ya
     *                                  existe
     */
    public Usuario registrarUsuario(String nombre, String apellido, String email,
            String password, String rol, String direccion,
            String telefono, String documentoIdentidad) {
        // Validaciones
        validarDatosUsuario(nombre, apellido, email, password, rol);

        // Verificar que el email no exista
        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Crear el usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setPasswordHash(passwordEncoder.encode(password)); // Encriptar contraseña
        usuario.setRol(rol);
        usuario.setActivo(true);

        // Guardar usuario
        usuario = usuarioRepository.save(usuario);

        // Si es CLIENTE, crear perfil automáticamente
        if ("CLIENTE".equals(rol)) {
            try {
                String codigoCliente = clienteService.generarCodigoCliente();
                clienteService.crearCliente(
                        usuario.getId(),
                        codigoCliente,
                        direccion,
                        telefono,
                        documentoIdentidad);
            } catch (Exception e) {
                // Log del error pero no falla el registro del usuario
                System.err.println("Error al crear perfil de cliente: " + e.getMessage());
            }
        }

        return usuario;
    }

    // ========================================
    // MÉTODOS DE ACTUALIZACIÓN
    // ========================================

    /**
     * Actualiza la información de un usuario
     * 
     * @param id       ID del usuario a actualizar
     * @param nombre   nuevo nombre (opcional)
     * @param apellido nuevo apellido (opcional)
     * @param email    nuevo email (opcional, debe ser único)
     * @return el usuario actualizado
     * @throws IllegalArgumentException si el usuario no existe
     */
    public Usuario actualizarUsuario(Long id, String nombre, String apellido, String email) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Actualizar solo los campos que no son null
        if (nombre != null && !nombre.isEmpty()) {
            usuario.setNombre(nombre);
        }
        if (apellido != null && !apellido.isEmpty()) {
            usuario.setApellido(apellido);
        }
        if (email != null && !email.isEmpty()) {
            // Verificar que el nuevo email no esté en uso por otro usuario
            if (!email.equals(usuario.getEmail()) && usuarioRepository.existsByEmail(email)) {
                throw new IllegalArgumentException("El email ya está en uso");
            }
            usuario.setEmail(email);
        }

        return usuarioRepository.save(usuario);
    }

    /**
     * Cambia la contraseña de un usuario
     * 
     * @param id            ID del usuario
     * @param nuevaPassword nueva contraseña en texto plano
     * @return el usuario actualizado
     */
    public Usuario cambiarPassword(Long id, String nuevaPassword) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (nuevaPassword == null || nuevaPassword.length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }

        usuario.setPasswordHash(passwordEncoder.encode(nuevaPassword));
        return usuarioRepository.save(usuario);
    }

    /**
     * Activa o desactiva un usuario
     * 
     * @param id     ID del usuario
     * @param activo true para activar, false para desactivar
     * @return el usuario actualizado
     */
    public Usuario cambiarEstadoUsuario(Long id, Boolean activo) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        usuario.setActivo(activo);
        return usuarioRepository.save(usuario);
    }

    // ========================================
    // MÉTODOS DE AUTENTICACIÓN
    // ========================================

    /**
     * Valida las credenciales de un usuario (para login)
     * 
     * @param email    email del usuario
     * @param password contraseña en texto plano
     * @return true si las credenciales son válidas
     */
    public boolean validarCredenciales(String email, String password) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        if (usuario.isEmpty()) {
            return false;
        }

        // Verificar que el usuario esté activo
        if (!usuario.get().getActivo()) {
            throw new IllegalArgumentException("Usuario inactivo");
        }

        // Comparar password con el hash
        return passwordEncoder.matches(password, usuario.get().getPasswordHash());
    }

    // ========================================
    // MÉTODOS DE VALIDACIÓN PRIVADOS
    // ========================================

    /**
     * Valida los datos básicos de un usuario
     */
    private void validarDatosUsuario(String nombre, String apellido, String email,
            String password, String rol) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido es obligatorio");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("El email no es válido");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }
        if (rol == null || (!rol.equals("ADMIN") && !rol.equals("CLIENTE"))) {
            throw new IllegalArgumentException("El rol debe ser 'ADMIN' o 'CLIENTE'");
        }
    }
}