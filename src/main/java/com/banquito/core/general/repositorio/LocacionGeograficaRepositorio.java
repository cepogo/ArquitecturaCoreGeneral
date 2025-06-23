package com.banquito.core.general.repositorio;

import com.banquito.core.general.modelo.LocacionGeografica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LocacionGeograficaRepositorio extends JpaRepository<LocacionGeografica, Integer> {
    List<LocacionGeografica> findByIdLocacionPadre_IdLocacionAndEstado(Integer idLocacion, com.banquito.core.general.enums.EstadoLocacionesGeograficasEnum estado);
    
    Optional<LocacionGeografica> findFirstByEstructuraGeografica_Id_IdPaisAndEstructuraGeografica_Id_CodigoNivelAndEstado(
        String idPais, java.math.BigDecimal codigoNivel, com.banquito.core.general.enums.EstadoLocacionesGeograficasEnum estado);
    
    // Buscar provincia por nombre
    Optional<LocacionGeografica> findFirstByNombreAndEstructuraGeografica_Id_IdPaisAndEstructuraGeografica_Id_CodigoNivelAndEstado(
        String nombre, String idPais, java.math.BigDecimal codigoNivel, com.banquito.core.general.enums.EstadoLocacionesGeograficasEnum estado);
    
    // Listar cantones de una provincia específica
    List<LocacionGeografica> findByIdLocacionPadre_NombreAndEstructuraGeografica_Id_IdPaisAndEstructuraGeografica_Id_CodigoNivelAndEstado(
        String nombreProvincia, String idPais, java.math.BigDecimal codigoNivel, com.banquito.core.general.enums.EstadoLocacionesGeograficasEnum estado);

    List<LocacionGeografica> findByEstructuraGeografica_Id_IdPais(String idPais);

} 