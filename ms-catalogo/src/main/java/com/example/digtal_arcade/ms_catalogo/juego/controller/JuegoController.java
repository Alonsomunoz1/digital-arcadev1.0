package com.example.digtal_arcade.ms_catalogo.juego.controller;

import com.example.digtal_arcade.ms_catalogo.juego.dto.JuegoDTO;
import com.example.digtal_arcade.ms_catalogo.juego.service.JuegoService;
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

@Tag(name = "Juegos", description = "Catálogo de juegos soportados por la tienda comunitaria")
@RestController
@RequestMapping("/api/v1/juegos")
public class JuegoController {

    private final JuegoService service;

    public JuegoController(JuegoService service) {
        this.service = service;
    }

    @Operation(summary = "Listar juegos")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<JuegoDTO>>> todos() {
        List<EntityModel<JuegoDTO>> juegos = service.obtenerTodos().stream()
                .map(this::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(juegos,
                linkTo(methodOn(JuegoController.class).todos()).withSelfRel()));
    }

    @Operation(summary = "Buscar juego por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<JuegoDTO>> porId(@PathVariable Integer id) {
        return service.obtenerPorId(id)
                .map(j -> ResponseEntity.ok(toModel(j)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Registrar juego")
    @PostMapping
    public ResponseEntity<?> registrar(@Valid @RequestBody JuegoDTO dto) {
        try {
            JuegoDTO guardado = service.guardar(dto);
            URI location = linkTo(methodOn(JuegoController.class).porId(guardado.getId())).toUri();
            return ResponseEntity.created(location).body(toModel(guardado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Actualizar parcialmente un juego")
    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarParcial(@PathVariable Integer id, @RequestBody JuegoDTO dto) {
        try {
            return service.actualizarParcial(id, dto)
                    .map(j -> ResponseEntity.ok(toModel(j)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Eliminar juego")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return service.eliminarPorId(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    private EntityModel<JuegoDTO> toModel(JuegoDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(JuegoController.class).porId(dto.getId())).withSelfRel(),
                linkTo(methodOn(JuegoController.class).todos()).withRel("juegos"));
    }
}
