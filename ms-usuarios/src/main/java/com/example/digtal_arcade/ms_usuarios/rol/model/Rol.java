package com.example.digtal_arcade.ms_usuarios.rol.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Column(name = "nombre_usuario", nullable = false, unique = true)
    private String nombreUsuario;

    @Email
    @NotBlank(message = "Debe ingresar un correo válido")
    @Column(nullable = false, unique = true)
    private String correo;

    @NotBlank
    @Column(name = "tipo_rol", nullable = false)
    private String tipoRol; // Ej: JUGADOR, ADMIN

    @Column(name = "estado_conexion", nullable = false)
    private String estadoConexion = "DESCONECTADO"; // EN_LINEA, DESCONECTADO, EN_EL_JUEGO
}