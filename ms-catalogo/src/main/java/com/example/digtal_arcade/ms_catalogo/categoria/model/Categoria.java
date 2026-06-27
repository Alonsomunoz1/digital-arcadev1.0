package com.example.digtal_arcade.ms_catalogo.categoria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer idCategoria;

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(min = 1, max = 30, message = "El nombre de la categoría debe tener entre 1 y 30 caracteres")
    @Column(nullable = false, unique = true)
    private String nombre;

    @NotBlank(message = "La descripción de la categoría es obligatoria")
    @Size(min = 1, max = 200, message = "La descripción de la categoría debe tener entre 1 y 200 caracteres")
    @Column(nullable = false)
    private String descripcion;
}
