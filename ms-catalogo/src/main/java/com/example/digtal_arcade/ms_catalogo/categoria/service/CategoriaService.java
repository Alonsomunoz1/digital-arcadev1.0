package com.example.digtal_arcade.ms_catalogo.categoria.service;

import com.example.digtal_arcade.ms_catalogo.categoria.dto.CategoriaDTO;
import com.example.digtal_arcade.ms_catalogo.categoria.model.Categoria;
import com.example.digtal_arcade.ms_catalogo.categoria.repository.CategoriaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CategoriaDTO> obtenerTodas() {
        return repository.findAll().stream().map(this::convertirADTO).toList();
    }

    @Transactional(readOnly = true)
    public Optional<CategoriaDTO> buscarPorId(Integer id) {
        return repository.findById(id).map(this::convertirADTO);
    }

    @Transactional
    public CategoriaDTO guardar(CategoriaDTO dto) {
        if (repository.existsByNombre(dto.getNombre())) {
            throw new IllegalArgumentException("La categoría '" + dto.getNombre() + "' ya existe.");
        }
        Categoria categoria = convertirAEntidad(dto);
        categoria.setIdCategoria(null);
        try {
            return convertirADTO(repository.save(categoria));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("La categoría '" + dto.getNombre() + "' ya existe.");
        }
    }

    @Transactional
    public Optional<CategoriaDTO> actualizarParcial(Integer id, CategoriaDTO dto) {
        return repository.findById(id).map(categoria -> {
            if (dto.getNombre() != null) {
                boolean cambia = !dto.getNombre().equals(categoria.getNombre());
                if (cambia && repository.existsByNombre(dto.getNombre())) {
                    throw new IllegalArgumentException("La categoría '" + dto.getNombre() + "' ya existe.");
                }
                categoria.setNombre(dto.getNombre());
            }
            if (dto.getDescripcion() != null) {
                categoria.setDescripcion(dto.getDescripcion());
            }
            return convertirADTO(repository.save(categoria));
        });
    }

    @Transactional
    public boolean eliminarPorId(Integer id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    private CategoriaDTO convertirADTO(Categoria c) {
        return new CategoriaDTO(c.getIdCategoria(), c.getNombre(), c.getDescripcion());
    }

    private Categoria convertirAEntidad(CategoriaDTO dto) {
        Categoria c = new Categoria();
        c.setIdCategoria(dto.getIdCategoria());
        c.setNombre(dto.getNombre());
        c.setDescripcion(dto.getDescripcion());
        return c;
    }
}
