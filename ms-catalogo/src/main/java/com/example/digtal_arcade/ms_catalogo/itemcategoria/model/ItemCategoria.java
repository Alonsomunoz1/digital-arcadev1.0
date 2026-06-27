package com.example.digtal_arcade.ms_catalogo.itemcategoria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "item_categoria")
public class ItemCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item_categoria")
    private Integer idItemCategoria;

    @NotNull(message = "El id del item es obligatorio")
    @Column(name = "id_item", nullable = false)
    private Integer idItem;

    @NotNull(message = "El id de la categoría es obligatorio")
    @Column(name = "id_categoria", nullable = false)
    private Integer idCategoria;
}
