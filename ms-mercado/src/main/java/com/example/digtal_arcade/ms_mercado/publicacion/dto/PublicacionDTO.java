package com.example.digtal_arcade.ms_mercado.publicacion.dto;

import com.example.digtal_arcade.ms_mercado.publicacion.model.EstadoPublicacion;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicacionDTO {

    private Integer id;

    @NotNull(message = "El ID del vendedor es obligatorio")
    private Integer idUsuario;

    @NotNull(message = "El ID del juego es obligatorio")
    private Integer idJuego;

    @NotNull(message = "El ID del item es obligatorio")
    private Integer idItem;

    @NotNull(message = "El precio de venta es obligatorio")
    @Min(value = 1, message = "El precio debe ser mayor que 0")
    private Integer precioVenta;

    private EstadoPublicacion estado;
}
