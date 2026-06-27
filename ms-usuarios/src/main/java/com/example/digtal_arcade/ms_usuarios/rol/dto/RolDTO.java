package com.example.digtal_arcade.ms_usuarios.rol.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolDTO {

    private Integer id;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String nombreUsuario;

    @Email(message = "Debe ingresar un correo válido")
    @NotBlank(message = "El correo es obligatorio")
    private String correo;

    @NotBlank(message = "El tipo de rol es obligatorio")
    private String tipoRol;

    private String estadoConexion;
}
