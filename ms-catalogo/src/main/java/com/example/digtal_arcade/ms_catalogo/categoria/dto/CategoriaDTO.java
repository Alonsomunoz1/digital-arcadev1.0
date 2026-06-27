package com.example.digtal_arcade.ms_catalogo.categoria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaDTO {

    private Integer idCategoria;

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(min = 1, max = 30, message = "El nombre de la categoría debe tener entre 1 y 30 caracteres")
    private String nombre;

    @NotBlank(message = "La descripción de la categoría es obligatoria")
    @Size(min = 1, max = 200, message = "La descripción de la categoría debe tener entre 1 y 200 caracteres")
    private String descripcion;
}
