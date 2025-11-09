package com.aguapago.usuarios.controller;

import com.aguapago.usuarios.dto.ClienteDTO;
import com.aguapago.usuarios.dto.ClienteInput;
import com.aguapago.usuarios.entity.Cliente;
import com.aguapago.usuarios.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Resolver GraphQL para operaciones de Cliente
 * 
 * Maneja todas las queries y mutations relacionadas con clientes:
 * - Consultas (obtener clientes)
 * - Creación y actualización de perfiles
 * - Generación de códigos
 */
@Controller
public class ClienteResolver {

    @Autowired
    private ClienteService clienteService;

    // ========================================
    // QUERIES (Consultas)
    // ========================================

    /**
     * Obtiene todos los clientes
     * GraphQL: obtenerTodosLosClientes
     */
    @QueryMapping
    public List<ClienteDTO> obtenerTodosLosClientes() {
        return clienteService.obtenerTodosLosClientes()
            .stream()
            .map(ClienteDTO::fromEntity)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene un cliente por su ID
     * GraphQL: obtenerClientePorId(id: ID!)
     */
    @QueryMapping
    public ClienteDTO obtenerClientePorId(@Argument Long id) {
        return clienteService.obtenerClientePorId(id)
            .map(ClienteDTO::fromEntity)
            .orElse(null);
    }

    /**
     * Obtiene un cliente por su código
     * GraphQL: obtenerClientePorCodigo(codigo: String!)
     */
    @QueryMapping
    public ClienteDTO obtenerClientePorCodigo(@Argument String codigo) {
        return clienteService.obtenerClientePorCodigo(codigo)
            .map(ClienteDTO::fromEntity)
            .orElse(null);
    }

    /**
     * Obtiene un cliente por el ID de su usuario
     * GraphQL: obtenerClientePorUsuarioId(usuarioId: ID!)
     */
    @QueryMapping
    public ClienteDTO obtenerClientePorUsuarioId(@Argument Long usuarioId) {
        return clienteService.obtenerClientePorUsuarioId(usuarioId)
            .map(ClienteDTO::fromEntity)
            .orElse(null);
    }

    // ========================================
    // MUTATIONS (Modificaciones)
    // ========================================

    /**
     * Crea un nuevo perfil de cliente
     * GraphQL: crearCliente(input: ClienteInput!)
     */
    @MutationMapping
    public ClienteDTO crearCliente(@Argument ClienteInput input) {
        Cliente cliente = clienteService.crearCliente(
            input.getUsuarioId(),
            input.getCodigoCliente(),
            input.getDireccion(),
            input.getTelefono(),
            input.getDocumentoIdentidad()
        );
        return ClienteDTO.fromEntity(cliente);
    }

    /**
     * Actualiza un cliente
     * GraphQL: actualizarCliente(id: ID!, input: ActualizarClienteInput!)
     */
    @MutationMapping
    public ClienteDTO actualizarCliente(@Argument Long id,
                                        @Argument String direccion,
                                        @Argument String telefono,
                                        @Argument String documentoIdentidad) {
        Cliente cliente = clienteService.actualizarCliente(
            id, 
            direccion, 
            telefono, 
            documentoIdentidad
        );
        return ClienteDTO.fromEntity(cliente);
    }

    /**
     * Genera un código de cliente único automáticamente
     * GraphQL: generarCodigoCliente
     */
    @MutationMapping
    public String generarCodigoCliente() {
        return clienteService.generarCodigoCliente();
    }
}