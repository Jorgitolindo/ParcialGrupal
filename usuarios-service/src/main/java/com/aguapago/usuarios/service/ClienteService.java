package com.aguapago.usuarios.service;

import com.aguapago.usuarios.entity.Cliente;
import com.aguapago.usuarios.entity.Usuario;
import com.aguapago.usuarios.repository.ClienteRepository;
import com.aguapago.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de Cliente
 * 
 * Contiene la lógica de negocio relacionada con clientes:
 * - Creación de perfiles de cliente
 * - Consultas de información de cliente
 * - Actualización de datos de cliente
 */
@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // ========================================
    // MÉTODOS DE CONSULTA
    // ========================================

    /**
     * Obtiene todos los clientes
     * @return lista de todos los clientes
     */
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    /**
     * Busca un cliente por su ID
     * @param id el ID del cliente
     * @return Optional con el cliente si existe
     */
    public Optional<Cliente> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    /**
     * Busca un cliente por su código
     * @param codigoCliente el código del cliente (ej: "CLI-001")
     * @return Optional con el cliente si existe
     */
    public Optional<Cliente> obtenerClientePorCodigo(String codigoCliente) {
        return clienteRepository.findByCodigoCliente(codigoCliente);
    }

    /**
     * Busca un cliente por el ID de su usuario
     * @param usuarioId el ID del usuario
     * @return Optional con el cliente si existe
     */
    public Optional<Cliente> obtenerClientePorUsuarioId(Long usuarioId) {
        return clienteRepository.findByUsuarioId(usuarioId);
    }

    /**
     * Busca un cliente por el email de su usuario
     * @param email el email del usuario
     * @return Optional con el cliente si existe
     */
    public Optional<Cliente> obtenerClientePorEmail(String email) {
        return clienteRepository.findByUsuarioEmail(email);
    }

    // ========================================
    // MÉTODOS DE CREACIÓN
    // ========================================

    /**
     * Crea un nuevo perfil de cliente para un usuario
     * @param usuarioId ID del usuario (debe tener rol CLIENTE)
     * @param codigoCliente código único del cliente (ej: "CLI-001")
     * @param direccion dirección del cliente
     * @param telefono teléfono del cliente
     * @param documentoIdentidad documento de identidad (CI, NIT, etc.)
     * @return el cliente creado
     * @throws IllegalArgumentException si el usuario no existe, no es CLIENTE, o ya tiene perfil
     */
    public Cliente crearCliente(Long usuarioId, String codigoCliente, 
                                String direccion, String telefono, String documentoIdentidad) {
        // Verificar que el usuario existe
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        // Verificar que el usuario es CLIENTE
        if (!"CLIENTE".equals(usuario.getRol())) {
            throw new IllegalArgumentException("El usuario debe tener rol CLIENTE");
        }
        
        // Verificar que el usuario no tenga ya un perfil de cliente
        if (clienteRepository.existsByUsuarioId(usuarioId)) {
            throw new IllegalArgumentException("Este usuario ya tiene un perfil de cliente");
        }
        
        // Verificar que el código no exista
        if (clienteRepository.existsByCodigoCliente(codigoCliente)) {
            throw new IllegalArgumentException("El código de cliente ya existe");
        }
        
        // Validar código
        if (codigoCliente == null || codigoCliente.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de cliente es obligatorio");
        }
        
        // Crear el cliente
        Cliente cliente = new Cliente();
        cliente.setUsuario(usuario);
        cliente.setCodigoCliente(codigoCliente);
        cliente.setDireccion(direccion);
        cliente.setTelefono(telefono);
        cliente.setDocumentoIdentidad(documentoIdentidad);
        
        return clienteRepository.save(cliente);
    }

    // ========================================
    // MÉTODOS DE ACTUALIZACIÓN
    // ========================================

    /**
     * Actualiza la información de un cliente
     * @param id ID del cliente
     * @param direccion nueva dirección (opcional)
     * @param telefono nuevo teléfono (opcional)
     * @param documentoIdentidad nuevo documento (opcional)
     * @return el cliente actualizado
     * @throws IllegalArgumentException si el cliente no existe
     */
    public Cliente actualizarCliente(Long id, String direccion, 
                                     String telefono, String documentoIdentidad) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        
        // Actualizar solo los campos que no son null
        if (direccion != null) {
            cliente.setDireccion(direccion);
        }
        if (telefono != null) {
            cliente.setTelefono(telefono);
        }
        if (documentoIdentidad != null) {
            cliente.setDocumentoIdentidad(documentoIdentidad);
        }
        
        return clienteRepository.save(cliente);
    }

    /**
     * Actualiza el cliente de un usuario específico
     * @param usuarioId ID del usuario
     * @param direccion nueva dirección (opcional)
     * @param telefono nuevo teléfono (opcional)
     * @param documentoIdentidad nuevo documento (opcional)
     * @return el cliente actualizado
     */
    public Cliente actualizarClientePorUsuarioId(Long usuarioId, String direccion,
                                                  String telefono, String documentoIdentidad) {
        Cliente cliente = clienteRepository.findByUsuarioId(usuarioId)
            .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado para este usuario"));
        
        return actualizarCliente(cliente.getId(), direccion, telefono, documentoIdentidad);
    }

    // ========================================
    // MÉTODOS AUXILIARES
    // ========================================

    /**
     * Genera un código de cliente único automáticamente
     * @return código generado (ej: "CLI-001", "CLI-002", etc.)
     */
    public String generarCodigoCliente() {
        long totalClientes = clienteRepository.count();
        String codigo;
        int contador = (int) totalClientes + 1;
        
        // Generar código y verificar que no exista (por si acaso)
        do {
            codigo = String.format("CLI-%03d", contador);
            contador++;
        } while (clienteRepository.existsByCodigoCliente(codigo));
        
        return codigo;
    }
}