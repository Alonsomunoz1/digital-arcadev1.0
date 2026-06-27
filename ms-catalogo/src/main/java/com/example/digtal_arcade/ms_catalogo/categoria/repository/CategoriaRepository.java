package com.example.digtal_arcade.ms_catalogo.categoria.repository;

import com.example.digtal_arcade.ms_catalogo.categoria.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    boolean existsByNombre(String nombre);

    Optional<Categoria> findByNombre(String nombre);
}
