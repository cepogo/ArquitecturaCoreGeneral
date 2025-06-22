package com.banquito.core.general.repositorio;

import com.banquito.core.general.enums.EstadoGeneralEnum;
import com.banquito.core.general.modelo.EntidadBancaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntidadBancariaRepositorio extends JpaRepository<EntidadBancaria, Integer> {
    Optional<EntidadBancaria> findFirstByEstado(EstadoGeneralEnum estado);
}
