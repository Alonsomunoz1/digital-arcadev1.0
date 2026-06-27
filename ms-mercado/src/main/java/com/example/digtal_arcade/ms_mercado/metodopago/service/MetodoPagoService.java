package com.example.digtal_arcade.ms_mercado.metodopago.service;

import com.example.digtal_arcade.ms_mercado.metodopago.dto.MetodoPagoDTO;
import com.example.digtal_arcade.ms_mercado.metodopago.model.MetodoPago;
import com.example.digtal_arcade.ms_mercado.metodopago.repository.MetodoPagoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MetodoPagoService {

    private final MetodoPagoRepository repository;

    public MetodoPagoService(MetodoPagoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<MetodoPagoDTO> obtenerTodos() {
        return repository.findAll().stream().map(this::convertirADTO).toList();
    }

    @Transactional(readOnly = true)
    public Optional<MetodoPagoDTO> buscarPorId(Integer id) {
        return repository.findById(id).map(this::convertirADTO);
    }

    @Transactional
    public MetodoPagoDTO guardar(MetodoPagoDTO dto) {
        if (repository.existsByNombreMetodoPago(dto.getNombreMetodoPago())) {
            throw new IllegalArgumentException("El método de pago '" + dto.getNombreMetodoPago() + "' ya existe.");
        }
        MetodoPago entidad = convertirAEntidad(dto);
        entidad.setIdMetodoPago(null);
        try {
            return convertirADTO(repository.save(entidad));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("El método de pago '" + dto.getNombreMetodoPago() + "' ya existe.");
        }
    }

    @Transactional
    public Optional<MetodoPagoDTO> actualizarParcial(Integer id, MetodoPagoDTO dto) {
        return repository.findById(id).map(entidad -> {
            if (dto.getNombreMetodoPago() != null) {
                boolean cambia = !dto.getNombreMetodoPago().equals(entidad.getNombreMetodoPago());
                if (cambia && repository.existsByNombreMetodoPago(dto.getNombreMetodoPago())) {
                    throw new IllegalArgumentException("El método de pago '" + dto.getNombreMetodoPago() + "' ya existe.");
                }
                entidad.setNombreMetodoPago(dto.getNombreMetodoPago());
            }
            if (dto.getDescripcion() != null) {
                entidad.setDescripcion(dto.getDescripcion());
            }
            if (dto.getEstado() != null) {
                entidad.setEstado(dto.getEstado());
            }
            return convertirADTO(repository.save(entidad));
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

    private MetodoPagoDTO convertirADTO(MetodoPago m) {
        return new MetodoPagoDTO(m.getIdMetodoPago(), m.getNombreMetodoPago(), m.getDescripcion(), m.getEstado());
    }

    private MetodoPago convertirAEntidad(MetodoPagoDTO dto) {
        MetodoPago m = new MetodoPago();
        m.setIdMetodoPago(dto.getIdMetodoPago());
        m.setNombreMetodoPago(dto.getNombreMetodoPago());
        m.setDescripcion(dto.getDescripcion());
        m.setEstado(dto.getEstado());
        return m;
    }
}
