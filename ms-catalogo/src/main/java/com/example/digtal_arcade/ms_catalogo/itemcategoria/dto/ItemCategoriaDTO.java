package com.example.digtal_arcade.ms_catalogo.itemcategoria.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCategoriaDTO {

    private Integer idItemCategoria;

    @NotNull(message = "El id del item es obligatorio")
    private Integer idItem;

    @NotNull(message = "El id de la categoría es obligatorio")
    private Integer idCategoria;
}
