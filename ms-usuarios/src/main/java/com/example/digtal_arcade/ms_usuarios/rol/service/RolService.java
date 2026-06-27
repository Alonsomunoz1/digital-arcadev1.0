package com.example.digtal_arcade.ms_usuarios.rol.service;

import com.example.digtal_arcade.ms_usuarios.rol.dto.RolDTO;
import com.example.digtal_arcade.ms_usuarios.rol.model.Rol;
import com.example.digtal_arcade.ms_usuarios.rol.repository.RolRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Transactional(readOnly = true)
    public List<RolDTO> obtenerTodos() {
        return rolRepository.findAll().stream().map(this::convertirADTO).toList();
    }

    @Transactional(readOnly = true)
    public Optional<RolDTO> buscarPorId(Integer id) {
        return rolRepository.findById(id).map(this::convertirADTO);
    }

    @Transactional
    public RolDTO guardar(RolDTO dto) {
        if (rolRepository.existsByNombreUsuario(dto.getNombreUsuario())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
        }
        if (rolRepository.existsByCorreo(dto.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado.");
        }
        Rol rol = convertirAEntidad(dto);
        rol.setId(null);
        if (rol.getEstadoConexion() == null) {
            rol.setEstadoConexion("DESCONECTADO");
        }
        try {
            return convertirADTO(rolRepository.save(rol));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("El usuario o correo ya están registrados.");
        }
    }

    @Transactional
    public Optional<RolDTO> actualizarParcial(Integer id, RolDTO dto) {
        return rolRepository.findById(id).map(rol -> {
            if (dto.getNombreUsuario() != null) {
                boolean cambia = !dto.getNombreUsuario().equals(rol.getNombreUsuario());
                if (cambia && rolRepository.existsByNombreUsuario(dto.getNombreUsuario())) {
                    throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
                }
                rol.setNombreUsuario(dto.getNombreUsuario());
            }
            if (dto.getCorreo() != null) {
                boolean cambia = !dto.getCorreo().equals(rol.getCorreo());
                if (cambia && rolRepository.existsByCorreo(dto.getCorreo())) {
                    throw new IllegalArgumentException("El correo ya está registrado.");
                }
                rol.setCorreo(dto.getCorreo());
            }
            if (dto.getTipoRol() != null) {
                rol.setTipoRol(dto.getTipoRol());
            }
            if (dto.getEstadoConexion() != null) {
                rol.setEstadoConexion(dto.getEstadoConexion());
            }
            return convertirADTO(rolRepository.save(rol));
        });
    }

    @Transactional
    public boolean eliminarPorId(Integer id) {
        if (!rolRepository.existsById(id)) {
            return false;
        }
        rolRepository.deleteById(id);
        return true;
    }

    private RolDTO convertirADTO(Rol rol) {
        return new RolDTO(rol.getId(), rol.getNombreUsuario(), rol.getCorreo(),
                rol.getTipoRol(), rol.getEstadoConexion());
    }

    private Rol convertirAEntidad(RolDTO dto) {
        Rol rol = new Rol();
        rol.setId(dto.getId());
        rol.setNombreUsuario(dto.getNombreUsuario());
        rol.setCorreo(dto.getCorreo());
        rol.setTipoRol(dto.getTipoRol());
        rol.setEstadoConexion(dto.getEstadoConexion());
        return rol;
    }
}
