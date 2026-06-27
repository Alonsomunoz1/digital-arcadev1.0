package com.example.digtal_arcade.ms_catalogo.itemcategoria.repository;

import com.example.digtal_arcade.ms_catalogo.itemcategoria.model.ItemCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemCategoriaRepository extends JpaRepository<ItemCategoria, Integer> {

    List<ItemCategoria> findByIdItem(Integer idItem);

    List<ItemCategoria> findByIdCategoria(Integer idCategoria);

    boolean existsByIdItemAndIdCategoria(Integer idItem, Integer idCategoria);
}
