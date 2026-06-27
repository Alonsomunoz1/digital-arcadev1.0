package com.example.digtal_arcade.ms_mercado.metodopago.controller;

import com.example.digtal_arcade.ms_mercado.metodopago.dto.MetodoPagoDTO;
import com.example.digtal_arcade.ms_mercado.metodopago.service.MetodoPagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name = "Metodos de pago", description = "Administra los métodos de pago disponibles en la tienda")
@RestController
@RequestMapping("/api/v1/metodospago")
public class MetodopagoController {

    private final MetodoPagoService service;

    public MetodopagoController(MetodoPagoService service) {
        this.service = service;
    }

    @Operation(summary = "Listar métodos de pago")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<MetodoPagoDTO>>> listar() {
        List<EntityModel<MetodoPagoDTO>> metodos = service.obtenerTodos().stream()
                .map(this::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(metodos,
                linkTo(methodOn(MetodopagoController.class).listar()).withSelfRel()));
    }

    @Operation(summary = "Buscar método de pago por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<MetodoPagoDTO>> buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(m -> ResponseEntity.ok(toModel(m)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Registrar método de pago")
    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody MetodoPagoDTO dto) {
        try {
            MetodoPagoDTO guardado = service.guardar(dto);
            URI location = linkTo(methodOn(MetodopagoController.class).buscarPorId(guardado.getIdMetodoPago())).toUri();
            return ResponseEntity.created(location).body(toModel(guardado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Actualizar parcialmente un método de pago")
    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody MetodoPagoDTO dto) {
        try {
            return service.actualizarParcial(id, dto)
                    .map(m -> ResponseEntity.ok(toModel(m)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Eliminar método de pago")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return service.eliminarPorId(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    private EntityModel<MetodoPagoDTO> toModel(MetodoPagoDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(MetodopagoController.class).buscarPorId(dto.getIdMetodoPago())).withSelfRel(),
                linkTo(methodOn(MetodopagoController.class).listar()).withRel("metodosPago"));
    }
}
