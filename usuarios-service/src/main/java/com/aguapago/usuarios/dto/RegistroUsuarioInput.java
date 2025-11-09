package com.aguapago.usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para recibir datos de registro de usuario
 * Se usa en la mutation de GraphQL para crear usuarios
 */
@Data
public class RegistroUsuarioInput {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    @Size(max = 150, message = "El email no puede tener más de 150 caracteres")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String password;

    @NotBlank(message = "El rol es obligatorio")
    private String rol; // "ADMIN" o "CLIENTE"


    // ===== CAMPOS OPCIONALES PARA PERFIL DE CLIENTE =====
@Size(max = 200, message = "La dirección no puede tener más de 200 caracteres")
private String direccion;

@Size(max = 20, message = "El teléfono no puede tener más de 20 caracteres")
private String telefono;

@Size(max = 20, message = "El documento no puede tener más de 20 caracteres")
private String documentoIdentidad;

    // Constructor vacío requerido por GraphQL
    public RegistroUsuarioInput() {
    }

   // Constructor con parámetros
public RegistroUsuarioInput(String nombre, String apellido, String email, 
String password, String rol, String direccion,
String telefono, String documentoIdentidad) {
this.nombre = nombre;
this.apellido = apellido;
this.email = email;
this.password = password;
this.rol = rol;
this.direccion = direccion;
this.telefono = telefono;
this.documentoIdentidad = documentoIdentidad;
}
}