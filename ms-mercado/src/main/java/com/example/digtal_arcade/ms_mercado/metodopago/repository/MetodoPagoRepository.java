package com.example.digtal_arcade.ms_mercado.metodopago.repository;

import com.example.digtal_arcade.ms_mercado.metodopago.model.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Integer> {

    boolean existsByNombreMetodoPago(String nombreMetodoPago);

    Optional<MetodoPago> findByNombreMetodoPago(String nombreMetodoPago);

    List<MetodoPago> findByEstado(String estado);
}
