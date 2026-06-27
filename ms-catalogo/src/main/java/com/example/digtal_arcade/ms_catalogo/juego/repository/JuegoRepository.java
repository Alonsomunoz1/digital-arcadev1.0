package com.example.digtal_arcade.ms_catalogo.juego.repository;

import com.example.digtal_arcade.ms_catalogo.juego.model.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JuegoRepository extends JpaRepository<Juego, Integer> {

    boolean existsByTitulo(String titulo);
}
