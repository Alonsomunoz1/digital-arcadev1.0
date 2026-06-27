package com.example.digtal_arcade.ms_mercado.publicacion.controller;

import com.example.digtal_arcade.ms_mercado.publicacion.dto.PublicacionDTO;
import com.example.digtal_arcade.ms_mercado.publicacion.model.EstadoPublicacion;
import com.example.digtal_arcade.ms_mercado.publicacion.service.PublicacionService;
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

@Tag(name = "Mercado", description = "Publicaciones de venta estilo warframe.market / Steam Market")
@RestController
@RequestMapping("/api/v1/mercado")
public class PublicacionController {

    private final PublicacionService service;

    public PublicacionController(PublicacionService service) {
        this.service = service;
    }

    @Operation(summary = "Ver publicaciones disponibles")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PublicacionDTO>>> verMercado() {
        List<EntityModel<PublicacionDTO>> publicaciones = service.listarMercado().stream()
                .map(this::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(publicaciones,
                linkTo(methodOn(PublicacionController.class).verMercado()).withSelfRel()));
    }

    @Operation(summary = "Buscar publicación por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PublicacionDTO>> buscarPublicacion(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(p -> ResponseEntity.ok(toModel(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Ver publicaciones de un juego")
    @GetMapping("/juego/{idJuego}")
    public ResponseEntity<CollectionModel<EntityModel<PublicacionDTO>>> verMercadoPorJuego(@PathVariable Integer idJuego) {
        List<EntityModel<PublicacionDTO>> publicaciones = service.listarMercadoPorJuego(idJuego).stream()
                .map(this::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(publicaciones,
                linkTo(methodOn(PublicacionController.class).verMercadoPorJuego(idJuego)).withSelfRel(),
                linkTo(methodOn(PublicacionController.class).verMercado()).withRel("mercado")));
    }

    @Operation(summary = "Publicar un item en venta")
    @PostMapping
    public ResponseEntity<?> publicar(@Valid @RequestBody PublicacionDTO dto) {
        try {
            PublicacionDTO publicado = service.publicarVenta(dto);
            URI location = linkTo(methodOn(PublicacionController.class).buscarPublicacion(publicado.getId())).toUri();
            return ResponseEntity.created(location).body(toModel(publicado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Marcar publicación como vendida")
    @PatchMapping("/{id}/vendida")
    public ResponseEntity<EntityModel<PublicacionDTO>> marcarComoVendida(@PathVariable Integer id) {
        return service.marcarComoVendida(id)
                .map(p -> ResponseEntity.ok(toModel(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cancelar publicación")
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<EntityModel<PublicacionDTO>> cancelarPublicacion(@PathVariable Integer id) {
        return service.cancelarPublicacion(id)
                .map(p -> ResponseEntity.ok(toModel(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    private EntityModel<PublicacionDTO> toModel(PublicacionDTO publicacion) {
        EntityModel<PublicacionDTO> model = EntityModel.of(publicacion,
                linkTo(methodOn(PublicacionController.class).buscarPublicacion(publicacion.getId())).withSelfRel(),
                linkTo(methodOn(PublicacionController.class).verMercado()).withRel("mercado"));

        if (EstadoPublicacion.DISPONIBLE.equals(publicacion.getEstado())) {
            model.add(linkTo(methodOn(PublicacionController.class).marcarComoVendida(publicacion.getId())).withRel("marcar-vendida"));
            model.add(linkTo(methodOn(PublicacionController.class).cancelarPublicacion(publicacion.getId())).withRel("cancelar"));
        }
        return model;
    }
}
