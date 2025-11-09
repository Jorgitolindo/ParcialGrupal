package com.aguapago.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para enviar información de cliente
 * Incluye información del usuario asociado
 * Se usa para queries de GraphQL
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {

    private Long id;
    private String codigoCliente;
    private String direccion;
    private String telefono;
    private String documentoIdentidad;
    private LocalDateTime createdAt;
    
    // Información del usuario asociado
    private UsuarioDTO usuario;

    /**
     * Constructor desde entidad Cliente
     * Convierte Cliente → ClienteDTO (incluye Usuario → UsuarioDTO)
     */
    public static ClienteDTO fromEntity(com.aguapago.usuarios.entity.Cliente cliente) {
        return new ClienteDTO(
            cliente.getId(),
            cliente.getCodigoCliente(),
            cliente.getDireccion(),
            cliente.getTelefono(),
            cliente.getDocumentoIdentidad(),
            cliente.getCreatedAt(),
            UsuarioDTO.fromEntity(cliente.getUsuario()) // Convierte Usuario a DTO
        );
    }
}