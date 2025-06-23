package com.banquito.core.general.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LocacionGeograficaUpdateDTO {

    private Integer idLocacionPadre;

    @Size(max = 2, message = "El ID del país debe tener máximo 2 caracteres")
    private String idPais;

    private Integer codigoNivel;

    @Size(max = 100, message = "El nombre debe tener máximo 100 caracteres")
    private String nombre;

    @Size(max = 3, message = "El código de teléfono de área debe tener máximo 3 caracteres")
    private String codigoTelefonoArea;

    @Size(max = 20, message = "El código geográfico debe tener máximo 20 caracteres")
    private String codigoGeografico;

    @Size(max = 6, message = "El código postal debe tener máximo 6 caracteres")
    private String codigoPostal;
} 