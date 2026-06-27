package com.example.digtal_arcade.ms_catalogo.juego.service;

import com.example.digtal_arcade.ms_catalogo.juego.dto.JuegoDTO;
import com.example.digtal_arcade.ms_catalogo.juego.model.Juego;
import com.example.digtal_arcade.ms_catalogo.juego.repository.JuegoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class JuegoService {

    private final JuegoRepository repository;

    public JuegoService(JuegoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<JuegoDTO> obtenerTodos() {
        return repository.findAll().stream().map(this::convertirADTO).toList();
    }

    @Transactional(readOnly = true)
    public Optional<JuegoDTO> obtenerPorId(Integer id) {
        return repository.findById(id).map(this::convertirADTO);
    }

    @Transactional
    public JuegoDTO guardar(JuegoDTO dto) {
        if (repository.existsByTitulo(dto.getTitulo())) {
            throw new IllegalArgumentException("El juego '" + dto.getTitulo() + "' ya existe en el Arcade.");
        }
        Juego juego = new Juego(null, dto.getTitulo(), dto.getPrecio());
        return convertirADTO(repository.save(juego));
    }

    @Transactional
    public Optional<JuegoDTO> actualizarParcial(Integer id, JuegoDTO dto) {
        return repository.findById(id).map(juego -> {
            if (dto.getTitulo() != null) {
                boolean cambia = !dto.getTitulo().equals(juego.getTitulo());
                if (cambia && repository.existsByTitulo(dto.getTitulo())) {
                    throw new IllegalArgumentException("El juego '" + dto.getTitulo() + "' ya existe en el Arcade.");
                }
                juego.setTitulo(dto.getTitulo());
            }
            if (dto.getPrecio() != null) {
                juego.setPrecio(dto.getPrecio());
            }
            return convertirADTO(repository.save(juego));
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

    private JuegoDTO convertirADTO(Juego j) {
        return new JuegoDTO(j.getId(), j.getTitulo(), j.getPrecio());
    }
}
