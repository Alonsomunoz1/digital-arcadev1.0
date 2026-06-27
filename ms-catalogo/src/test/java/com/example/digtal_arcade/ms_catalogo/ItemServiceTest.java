package com.example.digtal_arcade.ms_catalogo;

import com.example.digtal_arcade.ms_catalogo.item.dto.ItemDTO;
import com.example.digtal_arcade.ms_catalogo.item.model.Item;
import com.example.digtal_arcade.ms_catalogo.item.repository.ItemRepository;
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
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de ItemService.
 * Cada metodo publico del service tiene al menos un test.
 * Los metodos con if (eliminar, actualizar, guardar) tienen test para CADA rama.
 * Asi se alcanza alta cobertura (objetivo >= 80%).
 */
@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    // ---------- obtenerTodos ----------
    @Test
    @DisplayName("Debe devolver todos los items como DTOs")
    void obtenerTodos_devuelveLista() {
        Item item1 = new Item(1, "Skin Arcana", 1);
        Item item2 = new Item(2, "Mod Serration", 1);
        when(itemRepository.findAll()).thenReturn(List.of(item1, item2));

        List<ItemDTO> resultado = itemService.obtenerTodos();

        assertEquals(2, resultado.size());
        assertEquals("Skin Arcana", resultado.get(0).getNombre());
        verify(itemRepository, times(1)).findAll();
    }

    // ---------- buscarPorId ----------
    @Test
    @DisplayName("Debe buscar un item por ID y transformarlo a DTO")
    void buscarPorId_exitoso() {
        Item item = new Item(1, "Skin Arcana", 10);
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));

        Optional<ItemDTO> resultado = itemService.buscarPorId(1);

        assertTrue(resultado.isPresent());
        assertEquals(item.getNombre(), resultado.get().getNombre());
        verify(itemRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Debe devolver vacio si el ID no existe")
    void buscarPorId_noExiste() {
        when(itemRepository.findById(99)).thenReturn(Optional.empty());

        Optional<ItemDTO> resultado = itemService.buscarPorId(99);

        assertTrue(resultado.isEmpty());
    }

    // ---------- guardar ----------
    @Test
    @DisplayName("Debe guardar un item nuevo cuando el nombre no existe")
    void guardar_itemNuevo() {
        ItemDTO solicitud = new ItemDTO(null, "Skin Arcana", 3);
        Item guardado = new Item(7, "Skin Arcana", 3);
        when(itemRepository.existsByNombre("Skin Arcana")).thenReturn(false);
        when(itemRepository.save(any(Item.class))).thenReturn(guardado);

        ItemDTO resultado = itemService.guardar(solicitud);

        assertNotNull(resultado.getId());
        assertEquals("Skin Arcana", resultado.getNombre());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    @DisplayName("Debe rechazar un item duplicado")
    void guardar_itemDuplicado() {
        ItemDTO solicitud = new ItemDTO(null, "Item repetido", 5);
        when(itemRepository.existsByNombre("Item repetido")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> itemService.guardar(solicitud));

        assertEquals("El item ya existe en el catalogo.", ex.getMessage());
        verify(itemRepository, never()).save(any());
    }

    // ---------- actualizarParcial ----------
    @Test
    @DisplayName("Debe actualizar el nombre de un item existente")
    void actualizar_exitoso() {
        Item existente = new Item(1, "Nombre Viejo", 1);
        when(itemRepository.findById(1)).thenReturn(Optional.of(existente));
        when(itemRepository.existsByNombre("Nombre Nuevo")).thenReturn(false);
        when(itemRepository.save(existente)).thenReturn(existente);

        ItemDTO cambios = new ItemDTO(null, "Nombre Nuevo", null);
        Optional<ItemDTO> resultado = itemService.actualizarParcial(1, cambios);

        assertTrue(resultado.isPresent());
        assertEquals("Nombre Nuevo", resultado.get().getNombre());
    }

    @Test
    @DisplayName("Debe devolver vacio al actualizar un item inexistente")
    void actualizar_noExiste() {
        when(itemRepository.findById(99)).thenReturn(Optional.empty());

        Optional<ItemDTO> resultado = itemService.actualizarParcial(99, new ItemDTO());

        assertTrue(resultado.isEmpty());
        verify(itemRepository, never()).save(any());
    }

    // ---------- eliminarPorId ----------
    @Test
    @DisplayName("Debe eliminar cuando el item existe")
    void eliminar_existe() {
        when(itemRepository.existsById(1)).thenReturn(true);

        boolean resultado = itemService.eliminarPorId(1);

        assertTrue(resultado);
        verify(itemRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("No debe eliminar cuando el item no existe")
    void eliminar_noExiste() {
        when(itemRepository.existsById(99)).thenReturn(false);

        boolean resultado = itemService.eliminarPorId(99);

        assertFalse(resultado);
        verify(itemRepository, never()).deleteById(anyInt());
    }

    // ---------- buscarPorJuego ----------
    @Test
    @DisplayName("Debe devolver los items de un juego")
    void buscarPorJuego_devuelveLista() {
        when(itemRepository.findByIdJuego(1))
                .thenReturn(List.of(new Item(1, "Skin", 1), new Item(2, "Mod", 1)));

        List<ItemDTO> resultado = itemService.buscarPorJuego(1);

        assertEquals(2, resultado.size());
        verify(itemRepository, times(1)).findByIdJuego(1);
    }

    // ---------- buscarPorNombre ----------
    @Test
    @DisplayName("Debe encontrar un item por su nombre")
    void buscarPorNombre_exitoso() {
        when(itemRepository.findByNombre("Skin Arcana"))
                .thenReturn(Optional.of(new Item(1, "Skin Arcana", 1)));

        Optional<ItemDTO> resultado = itemService.buscarPorNombre("Skin Arcana");

        assertTrue(resultado.isPresent());
        assertEquals(1, resultado.get().getIdJuego());
    }

    // ---------- contarPorJuego ----------
    @Test
    @DisplayName("Debe contar cuantos items tiene un juego")
    void contarPorJuego_devuelveTotal() {
        when(itemRepository.countByIdJuego(1)).thenReturn(5);

        Integer total = itemService.contarPorJuego(1);

        assertEquals(5, total);
        verify(itemRepository, times(1)).countByIdJuego(1);
    }
}
