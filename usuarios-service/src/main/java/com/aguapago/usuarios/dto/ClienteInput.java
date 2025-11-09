package com.aguapago.usuarios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para recibir datos al crear un perfil de cliente
 * Se usa en la mutation de GraphQL para crear clientes
 */
@Data
public class ClienteInput {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    @NotBlank(message = "El código de cliente es obligatorio")
    @Size(max = 20, message = "El código no puede tener más de 20 caracteres")
    private String codigoCliente;

    @Size(max = 200, message = "La dirección no puede tener más de 200 caracteres")
    private String direccion;

    @Size(max = 20, message = "El teléfono no puede tener más de 20 caracteres")
    private String telefono;

    @Size(max = 20, message = "El documento no puede tener más de 20 caracteres")
    private String documentoIdentidad;

    // Constructor vacío requerido por GraphQL
    public ClienteInput() {
    }

    // Constructor con parámetros
    public ClienteInput(Long usuarioId, String codigoCliente, String direccion,
                        String telefono, String documentoIdentidad) {
        this.usuarioId = usuarioId;
        this.codigoCliente = codigoCliente;
        this.direccion = direccion;
        this.telefono = telefono;
        this.documentoIdentidad = documentoIdentidad;
    }
}