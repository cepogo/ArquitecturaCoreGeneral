package com.banquito.core.general.repositorio;

import com.banquito.core.general.enums.EstadoGeneralEnum;
import com.banquito.core.general.modelo.EntidadBancaria;
import com.banquito.core.general.modelo.EntidadBancariaMoneda;
import com.banquito.core.general.modelo.Moneda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntidadBancariaMonedaRepositorio extends JpaRepository<EntidadBancariaMoneda, Integer> {
    List<EntidadBancariaMoneda> findByIdEntidadBancaria(EntidadBancaria entidadBancaria);
    List<EntidadBancariaMoneda> findByIdEntidadBancariaAndEstado(EntidadBancaria idEntidadBancaria, EstadoGeneralEnum estado);
    List<EntidadBancariaMoneda> findByIdMonedaIn(List<String> idsMoneda);
    List<EntidadBancariaMoneda> findByIdMoneda_IdMonedaIn(List<String> idMonedas);

    Optional<EntidadBancariaMoneda> findByIdEntidadBancariaAndIdMoneda(EntidadBancaria entidad, Moneda moneda);
}
