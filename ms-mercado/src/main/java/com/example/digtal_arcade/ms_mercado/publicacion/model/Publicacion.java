package com.example.digtal_arcade.ms_mercado.publicacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ordenes_mercado")
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "id_usuario")
    private Integer idUsuario;

    @Column(nullable = false, name = "id_juego")
    private Integer idJuego;

    @Column(nullable = false, name = "id_item")
    private Integer idItem;

    @Column(nullable = false, name = "precio_venta")
    private Integer precioVenta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPublicacion estado = EstadoPublicacion.DISPONIBLE;
}
