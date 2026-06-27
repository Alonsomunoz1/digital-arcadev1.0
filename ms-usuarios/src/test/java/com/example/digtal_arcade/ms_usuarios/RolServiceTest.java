package com.example.digtal_arcade.ms_usuarios;

import com.example.digtal_arcade.ms_usuarios.rol.dto.RolDTO;
import com.example.digtal_arcade.ms_usuarios.rol.model.Rol;
import com.example.digtal_arcade.ms_usuarios.rol.repository.RolRepository;
import com.example.digtal_arcade.ms_usuarios.rol.service.RolService;
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
class RolServiceTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolService rolService;

    private Rol nuevoRol(Integer id) {
        return new Rol(id, "jugador_uno", "jugador1@arcade.cl", "JUGADOR", "DESCONECTADO");
    }

    @Test
    @DisplayName("Debe devolver todos los usuarios como DTOs")
    void obtenerTodos_devuelveLista() {
        when(rolRepository.findAll()).thenReturn(List.of(nuevoRol(1), nuevoRol(2)));

        List<RolDTO> resultado = rolService.obtenerTodos();

        assertEquals(2, resultado.size());
        verify(rolRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe buscar un usuario por ID")
    void buscarPorId_exitoso() {
        when(rolRepository.findById(1)).thenReturn(Optional.of(nuevoRol(1)));

        Optional<RolDTO> resultado = rolService.buscarPorId(1);

        assertTrue(resultado.isPresent());
        assertEquals("jugador_uno", resultado.get().getNombreUsuario());
    }

    @Test
    @DisplayName("Debe devolver vacio si el usuario no existe")
    void buscarPorId_noExiste() {
        when(rolRepository.findById(99)).thenReturn(Optional.empty());

        Optional<RolDTO> resultado = rolService.buscarPorId(99);

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Debe guardar un usuario nuevo cuando nombre y correo no existen")
    void guardar_nuevo() {
        RolDTO solicitud = new RolDTO(null, "nuevo_user", "nuevo@arcade.cl", "JUGADOR", null);
        when(rolRepository.existsByNombreUsuario("nuevo_user")).thenReturn(false);
        when(rolRepository.existsByCorreo("nuevo@arcade.cl")).thenReturn(false);
        when(rolRepository.save(any(Rol.class)))
                .thenReturn(new Rol(10, "nuevo_user", "nuevo@arcade.cl", "JUGADOR", "DESCONECTADO"));

        RolDTO resultado = rolService.guardar(solicitud);

        assertNotNull(resultado.getId());
        assertEquals("nuevo_user", resultado.getNombreUsuario());
        verify(rolRepository, times(1)).save(any(Rol.class));
    }

    @Test
    @DisplayName("Debe rechazar un usuario con nombre duplicado")
    void guardar_nombreDuplicado() {
        RolDTO solicitud = new RolDTO(null, "jugador_uno", "otro@arcade.cl", "JUGADOR", null);
        when(rolRepository.existsByNombreUsuario("jugador_uno")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> rolService.guardar(solicitud));

        assertTrue(ex.getMessage().contains("usuario"));
        verify(rolRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe rechazar un usuario con correo duplicado")
    void guardar_correoDuplicado() {
        RolDTO solicitud = new RolDTO(null, "otro_user", "jugador1@arcade.cl", "JUGADOR", null);
        when(rolRepository.existsByNombreUsuario("otro_user")).thenReturn(false);
        when(rolRepository.existsByCorreo("jugador1@arcade.cl")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> rolService.guardar(solicitud));

        assertTrue(ex.getMessage().contains("correo"));
        verify(rolRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe actualizar el estado de conexion de un usuario existente")
    void actualizar_exitoso() {
        Rol existente = nuevoRol(1);
        when(rolRepository.findById(1)).thenReturn(Optional.of(existente));
        when(rolRepository.save(existente)).thenReturn(existente);

        RolDTO cambios = new RolDTO(null, null, null, null, "EN_LINEA");
        Optional<RolDTO> resultado = rolService.actualizarParcial(1, cambios);

        assertTrue(resultado.isPresent());
        assertEquals("EN_LINEA", resultado.get().getEstadoConexion());
    }

    @Test
    @DisplayName("Debe devolver vacio al actualizar un usuario inexistente")
    void actualizar_noExiste() {
        when(rolRepository.findById(99)).thenReturn(Optional.empty());

        Optional<RolDTO> resultado = rolService.actualizarParcial(99, new RolDTO());

        assertTrue(resultado.isEmpty());
        verify(rolRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe eliminar cuando el usuario existe")
    void eliminar_existe() {
        when(rolRepository.existsById(1)).thenReturn(true);

        boolean resultado = rolService.eliminarPorId(1);

        assertTrue(resultado);
        verify(rolRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("No debe eliminar cuando el usuario no existe")
    void eliminar_noExiste() {
        when(rolRepository.existsById(99)).thenReturn(false);

        boolean resultado = rolService.eliminarPorId(99);

        assertFalse(resultado);
        verify(rolRepository, never()).deleteById(anyInt());
    }
}
