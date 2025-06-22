package com.banquito.core.general.repositorio;

import com.banquito.core.general.modelo.LocacionGeografica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocacionGeograficaRepositorio extends JpaRepository<LocacionGeografica, Integer> {

} 