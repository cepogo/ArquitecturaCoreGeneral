package com.banquito.core.general.repositorio;

import com.banquito.core.general.modelo.EntidadBancaria;
import com.banquito.core.general.modelo.EntidadBancariaMoneda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntidadBancariaMonedaRepositorio extends JpaRepository<EntidadBancariaMoneda, Integer> {
    List<EntidadBancariaMoneda> findByIdEntidadBancaria(EntidadBancaria entidadBancaria);
    List<EntidadBancariaMoneda> findByIdEntidadBancariaAndEstado(EntidadBancaria entidadBancaria, String estado);
}
