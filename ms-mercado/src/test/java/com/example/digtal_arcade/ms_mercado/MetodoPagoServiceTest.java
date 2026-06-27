package com.example.digtal_arcade.ms_mercado;

import com.example.digtal_arcade.ms_mercado.metodopago.dto.MetodoPagoDTO;
import com.example.digtal_arcade.ms_mercado.metodopago.model.MetodoPago;
import com.example.digtal_arcade.ms_mercado.metodopago.repository.MetodoPagoRepository;
import com.example.digtal_arcade.ms_mercado.metodopago.service.MetodoPagoService;
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
class MetodoPagoServiceTest {

    @Mock
    private MetodoPagoRepository metodoPagoRepository;

    @InjectMocks
    private MetodoPagoService metodoPagoService;

    @Test
    @DisplayName("Debe devolver todos los metodos de pago como DTOs")
    void obtenerTodos_devuelveLista() {
        when(metodoPagoRepository.findAll()).thenReturn(List.of(
                new MetodoPago(1, "Webpay", "Pago Transbank", "ACTIVO"),
                new MetodoPago(2, "Tarjeta", "Visa/Mastercard", "ACTIVO")));

        List<MetodoPagoDTO> resultado = metodoPagoService.obtenerTodos();

        assertEquals(2, resultado.size());
        assertEquals("Webpay", resultado.get(0).getNombreMetodoPago());
        verify(metodoPagoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe buscar un metodo de pago por ID")
    void buscarPorId_exitoso() {
        when(metodoPagoRepository.findById(1))
                .thenReturn(Optional.of(new MetodoPago(1, "Webpay", "Pago Transbank", "ACTIVO")));

        Optional<MetodoPagoDTO> resultado = metodoPagoService.buscarPorId(1);

        assertTrue(resultado.isPresent());
        assertEquals("Webpay", resultado.get().getNombreMetodoPago());
    }

    @Test
    @DisplayName("Debe devolver vacio si el metodo de pago no existe")
    void buscarPorId_noExiste() {
        when(metodoPagoRepository.findById(99)).thenReturn(Optional.empty());

        Optional<MetodoPagoDTO> resultado = metodoPagoService.buscarPorId(99);

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Debe guardar un metodo de pago nuevo")
    void guardar_nuevo() {
        MetodoPagoDTO solicitud = new MetodoPagoDTO(null, "PayPal", "Pago internacional", "ACTIVO");
        when(metodoPagoRepository.existsByNombreMetodoPago("PayPal")).thenReturn(false);
        when(metodoPagoRepository.save(any(MetodoPago.class)))
                .thenReturn(new MetodoPago(8, "PayPal", "Pago internacional", "ACTIVO"));

        MetodoPagoDTO resultado = metodoPagoService.guardar(solicitud);

        assertNotNull(resultado.getIdMetodoPago());
        assertEquals("PayPal", resultado.getNombreMetodoPago());
        verify(metodoPagoRepository, times(1)).save(any(MetodoPago.class));
    }

    @Test
    @DisplayName("Debe rechazar un metodo de pago duplicado")
    void guardar_duplicado() {
        MetodoPagoDTO solicitud = new MetodoPagoDTO(null, "Webpay", "x", "ACTIVO");
        when(metodoPagoRepository.existsByNombreMetodoPago("Webpay")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> metodoPagoService.guardar(solicitud));

        assertTrue(ex.getMessage().contains("ya existe"));
        verify(metodoPagoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe actualizar el estado de un metodo de pago existente")
    void actualizar_exitoso() {
        MetodoPago existente = new MetodoPago(1, "Webpay", "Pago Transbank", "ACTIVO");
        when(metodoPagoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(metodoPagoRepository.save(existente)).thenReturn(existente);

        MetodoPagoDTO cambios = new MetodoPagoDTO(null, null, null, "INACTIVO");
        Optional<MetodoPagoDTO> resultado = metodoPagoService.actualizarParcial(1, cambios);

        assertTrue(resultado.isPresent());
        assertEquals("INACTIVO", resultado.get().getEstado());
    }

    @Test
    @DisplayName("Debe devolver vacio al actualizar un metodo inexistente")
    void actualizar_noExiste() {
        when(metodoPagoRepository.findById(99)).thenReturn(Optional.empty());

        Optional<MetodoPagoDTO> resultado = metodoPagoService.actualizarParcial(99, new MetodoPagoDTO());

        assertTrue(resultado.isEmpty());
        verify(metodoPagoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe eliminar cuando el metodo de pago existe")
    void eliminar_existe() {
        when(metodoPagoRepository.existsById(1)).thenReturn(true);

        boolean resultado = metodoPagoService.eliminarPorId(1);

        assertTrue(resultado);
        verify(metodoPagoRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("No debe eliminar cuando el metodo de pago no existe")
    void eliminar_noExiste() {
        when(metodoPagoRepository.existsById(99)).thenReturn(false);

        boolean resultado = metodoPagoService.eliminarPorId(99);

        assertFalse(resultado);
        verify(metodoPagoRepository, never()).deleteById(anyInt());
    }
}
