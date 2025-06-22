package com.banquito.core.general.repositorio;

import com.banquito.core.general.modelo.Feriado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface FeriadoRepositorio extends JpaRepository<Feriado, Integer> {
    List<Feriado> findByEstadoAndFechaBetweenAndIdLocacion_IdLocacion(
        com.banquito.core.general.enums.EstadoGeneralEnum estado, Date fechaInicio, Date fechaFin, Integer idLocacion);
} 