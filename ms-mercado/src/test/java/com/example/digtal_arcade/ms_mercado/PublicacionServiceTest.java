package com.example.digtal_arcade.ms_mercado;

import com.example.digtal_arcade.ms_mercado.publicacion.dto.PublicacionDTO;
import com.example.digtal_arcade.ms_mercado.publicacion.model.EstadoPublicacion;
import com.example.digtal_arcade.ms_mercado.publicacion.model.Publicacion;
import com.example.digtal_arcade.ms_mercado.publicacion.repository.PublicacionRepository;
import com.example.digtal_arcade.ms_mercado.publicacion.service.PublicacionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublicacionServiceTest {

    @Mock
    private PublicacionRepository publicacionRepository;

    // El service tambien depende de RestTemplate para validar juego/item remotos.
    // Por eso lo mockeamos: no queremos llamadas HTTP reales en una prueba unitaria.
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PublicacionService publicacionService;

    private Publicacion publicacionDisponible(Integer id) {
        Publicacion p = new Publicacion();
        p.setId(id);
        p.setIdUsuario(1);
        p.setIdJuego(1);
        p.setIdItem(1);
        p.setPrecioVenta(5000);
        p.setEstado(EstadoPublicacion.DISPONIBLE);
        return p;
    }

    @Test
    @DisplayName("Debe listar solo las publicaciones DISPONIBLES del mercado")
    void listarMercado_devuelveDisponibles() {
        when(publicacionRepository.findByEstado(EstadoPublicacion.DISPONIBLE))
                .thenReturn(List.of(publicacionDisponible(1), publicacionDisponible(2)));

        List<PublicacionDTO> resultado = publicacionService.listarMercado();

        assertEquals(2, resultado.size());
        verify(publicacionRepository, times(1)).findByEstado(EstadoPublicacion.DISPONIBLE);
    }

    @Test
    @DisplayName("Debe listar las publicaciones disponibles de un juego")
    void listarMercadoPorJuego_devuelveLista() {
        when(publicacionRepository.findByIdJuegoAndEstado(1, EstadoPublicacion.DISPONIBLE))
                .thenReturn(List.of(publicacionDisponible(1)));

        List<PublicacionDTO> resultado = publicacionService.listarMercadoPorJuego(1);

        assertEquals(1, resultado.size());
        verify(publicacionRepository, times(1)).findByIdJuegoAndEstado(1, EstadoPublicacion.DISPONIBLE);
    }

    @Test
    @DisplayName("Debe buscar una publicacion por ID")
    void buscarPorId_exitoso() {
        when(publicacionRepository.findById(1)).thenReturn(Optional.of(publicacionDisponible(1)));

        Optional<PublicacionDTO> resultado = publicacionService.buscarPorId(1);

        assertTrue(resultado.isPresent());
        assertEquals(5000, resultado.get().getPrecioVenta());
    }

    @Test
    @DisplayName("Debe publicar una venta cuando el juego y el item existen")
    void publicarVenta_exitoso() {
        PublicacionDTO solicitud = new PublicacionDTO(null, 1, 1, 1, 5000, null);
        // El RestTemplate no lanza excepcion -> juego e item existen en ms-catalogo
        when(restTemplate.getForObject(anyString(), eq(Object.class), any(Object.class)))
                .thenReturn(new Object());
        when(publicacionRepository.save(any(Publicacion.class))).thenReturn(publicacionDisponible(9));

        PublicacionDTO resultado = publicacionService.publicarVenta(solicitud);

        assertNotNull(resultado.getId());
        assertEquals(EstadoPublicacion.DISPONIBLE, resultado.getEstado());
        verify(publicacionRepository, times(1)).save(any(Publicacion.class));
    }

    @Test
    @DisplayName("Debe rechazar la publicacion si el juego no existe en el catalogo")
    void publicarVenta_juegoNoExiste() {
        PublicacionDTO solicitud = new PublicacionDTO(null, 1, 99, 1, 5000, null);
        // Simulamos que el catalogo responde con error al pedir el juego
        when(restTemplate.getForObject(anyString(), eq(Object.class), any(Object.class)))
                .thenThrow(new RestClientException("404"));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> publicacionService.publicarVenta(solicitud));

        assertTrue(ex.getMessage().contains("juego"));
        verify(publicacionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe marcar como vendida una publicacion disponible")
    void marcarComoVendida_exitoso() {
        Publicacion disponible = publicacionDisponible(1);
        when(publicacionRepository.findByIdAndEstado(1, EstadoPublicacion.DISPONIBLE))
                .thenReturn(Optional.of(disponible));
        when(publicacionRepository.save(disponible)).thenReturn(disponible);

        Optional<PublicacionDTO> resultado = publicacionService.marcarComoVendida(1);

        assertTrue(resultado.isPresent());
        assertEquals(EstadoPublicacion.VENDIDO, resultado.get().getEstado());
    }

    @Test
    @DisplayName("No debe marcar como vendida una publicacion que no esta disponible")
    void marcarComoVendida_noDisponible() {
        when(publicacionRepository.findByIdAndEstado(1, EstadoPublicacion.DISPONIBLE))
                .thenReturn(Optional.empty());

        Optional<PublicacionDTO> resultado = publicacionService.marcarComoVendida(1);

        assertTrue(resultado.isEmpty());
        verify(publicacionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe cancelar una publicacion disponible")
    void cancelarPublicacion_exitoso() {
        Publicacion disponible = publicacionDisponible(1);
        when(publicacionRepository.findByIdAndEstado(1, EstadoPublicacion.DISPONIBLE))
                .thenReturn(Optional.of(disponible));
        when(publicacionRepository.save(disponible)).thenReturn(disponible);

        Optional<PublicacionDTO> resultado = publicacionService.cancelarPublicacion(1);

        assertTrue(resultado.isPresent());
        assertEquals(EstadoPublicacion.CANCELADO, resultado.get().getEstado());
    }
}
