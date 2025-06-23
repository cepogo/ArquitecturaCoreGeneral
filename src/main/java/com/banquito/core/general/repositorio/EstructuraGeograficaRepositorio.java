package com.banquito.core.general.repositorio;

import com.banquito.core.general.modelo.EstructuraGeografica;
import com.banquito.core.general.modelo.EstructuraGeograficaId;
import com.banquito.core.general.modelo.Pais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstructuraGeograficaRepositorio extends JpaRepository<EstructuraGeografica, EstructuraGeograficaId> {
    List<EstructuraGeografica> findById_IdPais(String idPais);
}