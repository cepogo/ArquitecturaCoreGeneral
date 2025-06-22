package com.banquito.core.general.repositorio;

import com.banquito.core.general.modelo.EstructuraGeografica;
import com.banquito.core.general.modelo.EstructuraGeograficaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstructuraGeograficaRepositorio extends JpaRepository<EstructuraGeografica, EstructuraGeograficaId> {

} 