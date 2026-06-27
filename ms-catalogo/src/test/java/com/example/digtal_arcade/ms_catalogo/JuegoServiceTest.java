package com.example.digtal_arcade.ms_catalogo;

import com.example.digtal_arcade.ms_catalogo.juego.dto.JuegoDTO;
import com.example.digtal_arcade.ms_catalogo.juego.model.Juego;
import com.example.digtal_arcade.ms_catalogo.juego.repository.JuegoRepository;
import com.example.digtal_arcade.ms_catalogo.juego.service.JuegoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JuegoServiceTest {

    @Mock
    private JuegoRepository juegoRepository;

    @InjectMocks
    private JuegoService juegoService;

    @Test
    @DisplayName("Debe devolver todos los juegos como DTOs")
    void obtenerTodos_devuelveLista() {
        when(juegoRepository.findAll())
                .thenReturn(List.of(new Juego(1, "Warframe", 1000), new Juego(2, "Dota 2", 1000)));

        List<JuegoDTO> resultado = juegoService.obtenerTodos();

        assertEquals(2, resultado.size());
        assertEquals("Warframe", resultado.get(0).getTitulo());
        verify(juegoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe buscar un juego por ID")
    void obtenerPorId_exitoso() {
        when(juegoRepository.findById(1)).thenReturn(Optional.of(new Juego(1, "Warframe", 1000)));

        Optional<JuegoDTO> resultado = juegoService.obtenerPorId(1);

        assertTrue(resultado.isPresent());
        assertEquals("Warframe", resultado.get().getTitulo());
    }

    @Test
    @DisplayName("Debe devolver vacio si el juego no existe")
    void obtenerPorId_noExiste() {
        when(juegoRepository.findById(99)).thenReturn(Optional.empty());

        Optional<JuegoDTO> resultado = juegoService.obtenerPorId(99);

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Debe guardar un juego nuevo cuando el titulo no existe")
    void guardar_juegoNuevo() {
        JuegoDTO solicitud = new JuegoDTO(null, "Counter-Strike 2", 1000);
        when(juegoRepository.existsByTitulo("Counter-Strike 2")).thenReturn(false);
        when(juegoRepository.save(any(Juego.class))).thenReturn(new Juego(5, "Counter-Strike 2", 1000));

        JuegoDTO resultado = juegoService.guardar(solicitud);

        assertNotNull(resultado.getId());
        assertEquals("Counter-Strike 2", resultado.getTitulo());
        verify(juegoRepository, times(1)).save(any(Juego.class));
    }

    @Test
    @DisplayName("Debe rechazar un juego con titulo duplicado")
    void guardar_juegoDuplicado() {
        JuegoDTO solicitud = new JuegoDTO(null, "Warframe", 1000);
        when(juegoRepository.existsByTitulo("Warframe")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> juegoService.guardar(solicitud));

        assertTrue(ex.getMessage().contains("ya existe"));
        verify(juegoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe actualizar el precio de un juego existente")
    void actualizar_exitoso() {
        Juego existente = new Juego(1, "Warframe", 1000);
        when(juegoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(juegoRepository.save(existente)).thenReturn(existente);

        JuegoDTO cambios = new JuegoDTO(null, null, 2500);
        Optional<JuegoDTO> resultado = juegoService.actualizarParcial(1, cambios);

        assertTrue(resultado.isPresent());
        assertEquals(2500, resultado.get().getPrecio());
    }

    @Test
    @DisplayName("Debe devolver vacio al actualizar un juego inexistente")
    void actualizar_noExiste() {
        when(juegoRepository.findById(99)).thenReturn(Optional.empty());

        Optional<JuegoDTO> resultado = juegoService.actualizarParcial(99, new JuegoDTO());

        assertTrue(resultado.isEmpty());
        verify(juegoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe eliminar cuando el juego existe")
    void eliminar_existe() {
        when(juegoRepository.existsById(1)).thenReturn(true);

        boolean resultado = juegoService.eliminarPorId(1);

        assertTrue(resultado);
        verify(juegoRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("No debe eliminar cuando el juego no existe")
    void eliminar_noExiste() {
        when(juegoRepository.existsById(99)).thenReturn(false);

        boolean resultado = juegoService.eliminarPorId(99);

        assertFalse(resultado);
        verify(juegoRepository, never()).deleteById(anyInt());
    }
}
