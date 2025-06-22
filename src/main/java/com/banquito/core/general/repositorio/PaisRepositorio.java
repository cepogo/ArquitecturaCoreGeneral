package com.banquito.core.general.repositorio;

import com.banquito.core.general.enums.EstadoGeneralEnum;
import com.banquito.core.general.modelo.Pais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaisRepositorio extends JpaRepository<Pais, String> {
    Optional<Pais> findByNombre(String nombre);
    List<Pais> findByEstadoOrderByNombre(EstadoGeneralEnum estado);
}
