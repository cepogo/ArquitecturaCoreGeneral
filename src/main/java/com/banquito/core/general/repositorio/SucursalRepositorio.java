package com.banquito.core.general.repositorio;

import com.banquito.core.general.modelo.EntidadBancaria;
import com.banquito.core.general.modelo.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SucursalRepositorio extends JpaRepository<Sucursal, String> {
    List<Sucursal> findByIdLocacion_IdLocacionAndEstado(Integer idLocacion, String estado);
    List<Sucursal> findByIdLocacion_IdLocacionIn(List<Integer> idsLocacion);
    List<Sucursal> findByIdEntidadBancaria(EntidadBancaria entidadBancaria);



} 