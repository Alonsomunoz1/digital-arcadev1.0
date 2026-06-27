package com.example.digtal_arcade.ms_mercado.publicacion.service;

import com.example.digtal_arcade.ms_mercado.publicacion.dto.PublicacionDTO;
import com.example.digtal_arcade.ms_mercado.publicacion.model.EstadoPublicacion;
import com.example.digtal_arcade.ms_mercado.publicacion.model.Publicacion;
import com.example.digtal_arcade.ms_mercado.publicacion.repository.PublicacionRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PublicacionService {

    private static final EstadoPublicacion ESTADO_DISPONIBLE = EstadoPublicacion.DISPONIBLE;

    private final PublicacionRepository publicacionRepository;
    private final RestTemplate restTemplate;

    public PublicacionService(PublicacionRepository publicacionRepository, RestTemplate restTemplate) {
        this.publicacionRepository = publicacionRepository;
        this.restTemplate = restTemplate;
    }

    public List<PublicacionDTO> listarMercado() {
        return publicacionRepository.findByEstado(ESTADO_DISPONIBLE).stream()
                .map(this::toDTO)
                .toList();
    }

    public List<PublicacionDTO> listarMercadoPorJuego(Integer idJuego) {
        return publicacionRepository.findByIdJuegoAndEstado(idJuego, ESTADO_DISPONIBLE).stream()
                .map(this::toDTO)
                .toList();
    }

    public Optional<PublicacionDTO> buscarPorId(Integer id) {
        return publicacionRepository.findById(id)
                .map(this::toDTO);
    }

    public PublicacionDTO publicarVenta(PublicacionDTO dto) {
        validarJuego(dto.getIdJuego());
        validarItem(dto.getIdItem());

        Publicacion nuevaPublicacion = toEntity(dto);
        nuevaPublicacion.setId(null);
        nuevaPublicacion.setEstado(ESTADO_DISPONIBLE);

        return toDTO(publicacionRepository.save(nuevaPublicacion));
    }

    public Optional<PublicacionDTO> marcarComoVendida(Integer id) {
        return cambiarEstadoSiDisponible(id, EstadoPublicacion.VENDIDO);
    }

    public Optional<PublicacionDTO> cancelarPublicacion(Integer id) {
        return cambiarEstadoSiDisponible(id, EstadoPublicacion.CANCELADO);
    }

    private Optional<PublicacionDTO> cambiarEstadoSiDisponible(Integer id, EstadoPublicacion nuevoEstado) {
        return publicacionRepository.findByIdAndEstado(id, ESTADO_DISPONIBLE)
                .map(publicacion -> {
                    publicacion.setEstado(nuevoEstado);
                    return toDTO(publicacionRepository.save(publicacion));
                });
    }

    private void validarJuego(Integer idJuego) {
        try {
            restTemplate.getForObject("http://ms-catalogo/api/v1/juegos/{idJuego}", Object.class, idJuego);
        } catch (RestClientException exception) {
            throw new IllegalArgumentException("El juego especificado no existe en el catalogo.");
        }
    }

    private void validarItem(Integer idItem) {
        try {
            restTemplate.getForObject("http://ms-catalogo/api/v1/items/{idItem}", Object.class, idItem);
        } catch (RestClientException exception) {
            throw new IllegalArgumentException("El item que intentas vender no existe.");
        }
    }

    private PublicacionDTO toDTO(Publicacion p) {
        return new PublicacionDTO(
                p.getId(), p.getIdUsuario(), p.getIdJuego(),
                p.getIdItem(), p.getPrecioVenta(), p.getEstado());
    }

    private Publicacion toEntity(PublicacionDTO dto) {
        Publicacion p = new Publicacion();
        p.setId(dto.getId());
        p.setIdUsuario(dto.getIdUsuario());
        p.setIdJuego(dto.getIdJuego());
        p.setIdItem(dto.getIdItem());
        p.setPrecioVenta(dto.getPrecioVenta());
        p.setEstado(dto.getEstado());
        return p;
    }
}
