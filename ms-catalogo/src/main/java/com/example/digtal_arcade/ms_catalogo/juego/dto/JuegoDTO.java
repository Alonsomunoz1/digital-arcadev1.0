package com.example.digtal_arcade.ms_catalogo.juego.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JuegoDTO {

    private Integer id;

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    private String titulo;

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 1000, message = "El precio mínimo en nuestra comunidad es $1.000 CLP")
    private Integer precio;
}
