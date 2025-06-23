package com.banquito.core.general.repositorio;

import com.banquito.core.general.enums.EstadoGeneralEnum;
import com.banquito.core.general.enums.TipoFeriadosEnum;
import com.banquito.core.general.modelo.Feriado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface FeriadoRepositorio extends JpaRepository<Feriado, Integer> {
    List<Feriado> findByEstadoAndFechaBetweenAndIdLocacion_IdLocacion(
        EstadoGeneralEnum estado, Date fechaInicio, Date fechaFin, Integer idLocacion);

    List<Feriado> findByEstadoAndFechaBetween(
        EstadoGeneralEnum estado, Date fechaInicio, Date fechaFin);

    List<Feriado> findByEstadoAndFechaBetweenAndTipo(
        EstadoGeneralEnum estado, Date fechaInicio, Date fechaFin, TipoFeriadosEnum tipo);

    List<Feriado> findByEstadoAndFechaBetweenAndTipoAndIdLocacion_IdLocacion(
        EstadoGeneralEnum estado, Date fechaInicio, Date fechaFin, TipoFeriadosEnum tipo, Integer idLocacion);

    List<Feriado> findByIdPais_IdPais(String idPais);
} 