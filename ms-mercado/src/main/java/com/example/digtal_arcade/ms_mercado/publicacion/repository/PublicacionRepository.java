package com.example.digtal_arcade.ms_mercado.publicacion.repository;

import com.example.digtal_arcade.ms_mercado.publicacion.model.EstadoPublicacion;
import com.example.digtal_arcade.ms_mercado.publicacion.model.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Integer> {

    List<Publicacion> findByEstado(EstadoPublicacion estado);

    List<Publicacion> findByIdJuegoAndEstado(Integer idJuego, EstadoPublicacion estado);

    Optional<Publicacion> findByIdAndEstado(Integer id, EstadoPublicacion estado);
}
