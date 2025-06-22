package com.banquito.core.general.repositorio;

import com.banquito.core.general.modelo.Moneda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MonedaRepositorio extends JpaRepository<Moneda, String> {
    Optional<Moneda> findByNombre(String nombre);
    List<Moneda> findByEstado(String estado);
    List<Moneda> findByIdPais(String idPais);
}
