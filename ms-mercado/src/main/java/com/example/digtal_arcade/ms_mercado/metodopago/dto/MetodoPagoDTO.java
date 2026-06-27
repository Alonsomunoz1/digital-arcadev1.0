package com.example.digtal_arcade.ms_mercado.metodopago.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetodoPagoDTO {

    private Integer idMetodoPago;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombreMetodoPago;

    private String descripcion;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}
