package com.example.digtal_arcade.ms_catalogo.itemcategoria.controller;

import com.example.digtal_arcade.ms_catalogo.itemcategoria.dto.ItemCategoriaDTO;
import com.example.digtal_arcade.ms_catalogo.itemcategoria.service.ItemCategoriaService;
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

@Tag(name = "Item-Categoria", description = "Relación N:N que asigna categorías a los items del catálogo")
@RestController
@RequestMapping("/api/v1/item-categorias")
public class ItemCategoriaController {

    private final ItemCategoriaService service;

    public ItemCategoriaController(ItemCategoriaService service) {
        this.service = service;
    }

    @Operation(summary = "Listar asociaciones item-categoría")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ItemCategoriaDTO>>> todas() {
        List<EntityModel<ItemCategoriaDTO>> data = service.obtenerTodas().stream()
                .map(this::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(data,
                linkTo(methodOn(ItemCategoriaController.class).todas()).withSelfRel()));
    }

    @Operation(summary = "Buscar asociación por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ItemCategoriaDTO>> porId(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(ic -> ResponseEntity.ok(toModel(ic)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar categorías de un item")
    @GetMapping("/item/{idItem}")
    public ResponseEntity<CollectionModel<EntityModel<ItemCategoriaDTO>>> porItem(@PathVariable Integer idItem) {
        List<EntityModel<ItemCategoriaDTO>> data = service.buscarPorItem(idItem).stream()
                .map(this::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(data,
                linkTo(methodOn(ItemCategoriaController.class).porItem(idItem)).withSelfRel(),
                linkTo(methodOn(ItemCategoriaController.class).todas()).withRel("item-categorias")));
    }

    @Operation(summary = "Crear asociación item-categoría")
    @PostMapping
    public ResponseEntity<?> registrar(@Valid @RequestBody ItemCategoriaDTO dto) {
        try {
            ItemCategoriaDTO guardada = service.guardar(dto);
            URI location = linkTo(methodOn(ItemCategoriaController.class).porId(guardada.getIdItemCategoria())).toUri();
            return ResponseEntity.created(location).body(toModel(guardada));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Eliminar asociación")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return service.eliminarPorId(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    private EntityModel<ItemCategoriaDTO> toModel(ItemCategoriaDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(ItemCategoriaController.class).porId(dto.getIdItemCategoria())).withSelfRel(),
                linkTo(methodOn(ItemCategoriaController.class).todas()).withRel("item-categorias"));
    }
}
