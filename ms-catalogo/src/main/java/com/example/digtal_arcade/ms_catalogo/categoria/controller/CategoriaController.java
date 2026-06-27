package com.example.digtal_arcade.ms_catalogo.categoria.controller;

import com.example.digtal_arcade.ms_catalogo.categoria.dto.CategoriaDTO;
import com.example.digtal_arcade.ms_catalogo.categoria.service.CategoriaService;
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

@Tag(name = "Categorias", description = "Clasificación de los items del catálogo (Skin, Mod, Moneda, etc.)")
@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @Operation(summary = "Listar categorías")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<CategoriaDTO>>> todas() {
        List<EntityModel<CategoriaDTO>> categorias = service.obtenerTodas().stream()
                .map(this::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(categorias,
                linkTo(methodOn(CategoriaController.class).todas()).withSelfRel()));
    }

    @Operation(summary = "Buscar categoría por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CategoriaDTO>> porId(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(c -> ResponseEntity.ok(toModel(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Registrar categoría")
    @PostMapping
    public ResponseEntity<?> registrar(@Valid @RequestBody CategoriaDTO dto) {
        try {
            CategoriaDTO guardada = service.guardar(dto);
            URI location = linkTo(methodOn(CategoriaController.class).porId(guardada.getIdCategoria())).toUri();
            return ResponseEntity.created(location).body(toModel(guardada));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Actualizar parcialmente una categoría")
    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarParcial(@PathVariable Integer id, @RequestBody CategoriaDTO dto) {
        try {
            return service.actualizarParcial(id, dto)
                    .map(c -> ResponseEntity.ok(toModel(c)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Eliminar categoría")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return service.eliminarPorId(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    private EntityModel<CategoriaDTO> toModel(CategoriaDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(CategoriaController.class).porId(dto.getIdCategoria())).withSelfRel(),
                linkTo(methodOn(CategoriaController.class).todas()).withRel("categorias"));
    }
}
