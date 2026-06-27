package com.example.digtal_arcade.ms_catalogo.item.repository;

import com.example.digtal_arcade.ms_catalogo.item.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findByIdJuego(Integer idJuego);

    Optional<Item> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    Integer countByIdJuego(Integer idJuego);
}
