package com.example.digtal_arcade.ms_catalogo.itemcategoria.service;

import com.example.digtal_arcade.ms_catalogo.itemcategoria.dto.ItemCategoriaDTO;
import com.example.digtal_arcade.ms_catalogo.itemcategoria.model.ItemCategoria;
import com.example.digtal_arcade.ms_catalogo.itemcategoria.repository.ItemCategoriaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ItemCategoriaService {

    private final ItemCategoriaRepository repository;

    public ItemCategoriaService(ItemCategoriaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ItemCategoriaDTO> obtenerTodas() {
        return repository.findAll().stream().map(this::convertirADTO).toList();
    }

    @Transactional(readOnly = true)
    public Optional<ItemCategoriaDTO> buscarPorId(Integer id) {
        return repository.findById(id).map(this::convertirADTO);
    }

    @Transactional(readOnly = true)
    public List<ItemCategoriaDTO> buscarPorItem(Integer idItem) {
        return repository.findByIdItem(idItem).stream().map(this::convertirADTO).toList();
    }

    @Transactional(readOnly = true)
    public List<ItemCategoriaDTO> buscarPorCategoria(Integer idCategoria) {
        return repository.findByIdCategoria(idCategoria).stream().map(this::convertirADTO).toList();
    }

    @Transactional
    public ItemCategoriaDTO guardar(ItemCategoriaDTO dto) {
        if (repository.existsByIdItemAndIdCategoria(dto.getIdItem(), dto.getIdCategoria())) {
            throw new IllegalArgumentException("Esa asociación item-categoría ya existe.");
        }
        ItemCategoria entidad = convertirAEntidad(dto);
        entidad.setIdItemCategoria(null);
        try {
            return convertirADTO(repository.save(entidad));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Esa asociación item-categoría ya existe.");
        }
    }

    @Transactional
    public boolean eliminarPorId(Integer id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    private ItemCategoriaDTO convertirADTO(ItemCategoria e) {
        return new ItemCategoriaDTO(e.getIdItemCategoria(), e.getIdItem(), e.getIdCategoria());
    }

    private ItemCategoria convertirAEntidad(ItemCategoriaDTO dto) {
        ItemCategoria e = new ItemCategoria();
        e.setIdItemCategoria(dto.getIdItemCategoria());
        e.setIdItem(dto.getIdItem());
        e.setIdCategoria(dto.getIdCategoria());
        return e;
    }
}
