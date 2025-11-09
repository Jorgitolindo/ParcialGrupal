package com.aguapago.usuarios.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad Cliente - Representa la tabla 'cliente' en la base de datos
 * 
 * Esta clase almacena información adicional específica de los usuarios
 * que tienen rol CLIENTE (no aplica para ADMIN).
 * 
 * Relación: Un Usuario (CLIENTE) tiene un Cliente (1:1)
 */
@Entity
@Table(name = "cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "El código de cliente es obligatorio")
    @Size(max = 20, message = "El código no puede tener más de 20 caracteres")
    @Column(name = "codigo_cliente", nullable = false, unique = true, length = 20)
    private String codigoCliente; // Ejemplo: "CLI-001", "CLI-002"

    @Size(max = 200, message = "La dirección no puede tener más de 200 caracteres")
    @Column(name = "direccion", length = 200)
    private String direccion;

    @Size(max = 20, message = "El teléfono no puede tener más de 20 caracteres")
    @Column(name = "telefono", length = 20)
    private String telefono;

    @Size(max = 20, message = "El documento de identidad no puede tener más de 20 caracteres")
    @Column(name = "documento_identidad", length = 20)
    private String documentoIdentidad; // CI, NIT, Pasaporte, etc.

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Relación One-to-One con Usuario
     * 
     * Un Cliente pertenece a un Usuario
     * Un Usuario (con rol CLIENTE) tiene un Cliente
     */
    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    /**
     * Método que se ejecuta antes de persistir la entidad
     * Establece la fecha de creación automáticamente
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

