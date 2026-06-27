package com.example.digtal_arcade.ms_usuarios.rol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.digtal_arcade.ms_usuarios.rol.model.Rol;

@Repository 
public interface RolRepository extends JpaRepository<Rol, Integer> {
    boolean existsByNombreUsuario(String nombreUsuario);
    boolean existsByCorreo(String correo);
}
