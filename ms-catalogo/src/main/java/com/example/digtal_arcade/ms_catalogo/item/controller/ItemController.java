package com.example.digtal_arcade.ms_catalogo.item.controller;

import com.example.digtal_arcade.ms_catalogo.item.dto.ItemDTO;
import com.example.digtal_arcade.ms_catalogo.item.service.ItemService;
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

@Tag(name = "Catalogo de items", description = "Administra los items base que luego se publican en el mercado comunitario")
@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Operation(summary = "Listar items")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ItemDTO>>> todas() {
        List<EntityModel<ItemDTO>> items = itemService.obtenerTodos().stream()
                .map(this::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(ItemController.class).todas()).withSelfRel()));
    }

    @Operation(summary = "Buscar item por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ItemDTO>> porId(@PathVariable Integer id) {
        return itemService.buscarPorId(id)
                .map(item -> ResponseEntity.ok(toModel(item)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Registrar item")
    @PostMapping
    public ResponseEntity<?> registrar(@Valid @RequestBody ItemDTO itemDTO) {
        try {
            ItemDTO guardado = itemService.guardar(itemDTO);
            URI location = linkTo(methodOn(ItemController.class).porId(guardado.getId())).toUri();
            return ResponseEntity.created(location).body(toModel(guardado));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @Operation(summary = "Actualizar parcialmente un item")
    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarParcial(@PathVariable Integer id, @RequestBody ItemDTO itemDTO) {
        try {
            return itemService.actualizarParcial(id, itemDTO)
                    .map(item -> ResponseEntity.ok(toModel(item)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @Operation(summary = "Eliminar item")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return itemService.eliminarPorId(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Buscar items por juego")
    @GetMapping("/juego/{idJuego}")
    public ResponseEntity<CollectionModel<EntityModel<ItemDTO>>> buscarPorJuego(@PathVariable Integer idJuego) {
        List<EntityModel<ItemDTO>> items = itemService.buscarPorJuego(idJuego).stream()
                .map(this::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(ItemController.class).buscarPorJuego(idJuego)).withSelfRel(),
                linkTo(methodOn(ItemController.class).todas()).withRel("items")));
    }

    private EntityModel<ItemDTO> toModel(ItemDTO item) {
        return EntityModel.of(item,
                linkTo(methodOn(ItemController.class).porId(item.getId())).withSelfRel(),
                linkTo(methodOn(ItemController.class).todas()).withRel("items"),
                linkTo(methodOn(ItemController.class).buscarPorJuego(item.getIdJuego())).withRel("items-del-juego"));
    }
}
