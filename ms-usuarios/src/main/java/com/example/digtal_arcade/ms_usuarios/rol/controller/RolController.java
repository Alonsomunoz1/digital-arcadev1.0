package com.example.digtal_arcade.ms_usuarios.rol.controller;

import com.example.digtal_arcade.ms_usuarios.rol.dto.RolDTO;
import com.example.digtal_arcade.ms_usuarios.rol.service.RolService;
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

@Tag(name = "Usuarios y roles", description = "Administra usuarios, su rol y estado de conexión en Digital Arcade")
@RestController
@RequestMapping("/api/v1/usuarios")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @Operation(summary = "Listar usuarios")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<RolDTO>>> listar() {
        List<EntityModel<RolDTO>> usuarios = rolService.obtenerTodos().stream()
                .map(this::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(usuarios,
                linkTo(methodOn(RolController.class).listar()).withSelfRel()));
    }

    @Operation(summary = "Buscar usuario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<RolDTO>> porId(@PathVariable Integer id) {
        return rolService.buscarPorId(id)
                .map(u -> ResponseEntity.ok(toModel(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Registrar usuario")
    @PostMapping
    public ResponseEntity<?> registrar(@Valid @RequestBody RolDTO dto) {
        try {
            RolDTO nuevo = rolService.guardar(dto);
            URI location = linkTo(methodOn(RolController.class).porId(nuevo.getId())).toUri();
            return ResponseEntity.created(location).body(toModel(nuevo));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Actualizar parcialmente un usuario")
    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarParcial(@PathVariable Integer id, @RequestBody RolDTO dto) {
        try {
            return rolService.actualizarParcial(id, dto)
                    .map(u -> ResponseEntity.ok(toModel(u)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Eliminar usuario")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return rolService.eliminarPorId(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    private EntityModel<RolDTO> toModel(RolDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(RolController.class).porId(dto.getId())).withSelfRel(),
                linkTo(methodOn(RolController.class).listar()).withRel("usuarios"));
    }
}
