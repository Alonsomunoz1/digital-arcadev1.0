package com.example.digtal_arcade.ms_catalogo.juego.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "juegos")
public class Juego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 100)
    @Column(nullable = false, unique = true)
    private String titulo;

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 1000, message = "El precio debe ser al menos $1.000 CLP")
    @Column(nullable = false)
    private Integer precio;
}
