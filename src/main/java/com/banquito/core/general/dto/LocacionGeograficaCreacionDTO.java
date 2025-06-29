package com.banquito.core.general.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LocacionGeograficaCreacionDTO {

    @NotNull(message = "El ID del país no puede ser nulo")
    @Size(max = 2, message = "El ID del país debe tener máximo 2 caracteres")
    private String idPais;

    @NotNull(message = "El código de nivel no puede ser nulo")
    private Integer codigoNivel;

    @NotNull(message = "El nombre no puede ser nulo")
    @Size(max = 100, message = "El nombre debe tener máximo 100 caracteres")
    private String nombre;

    @NotNull(message = "El código de teléfono de área no puede ser nulo")
    @Size(max = 3, message = "El código de teléfono de área debe tener máximo 3 caracteres")
    private String codigoTelefonoArea;

    @NotNull(message = "El código geográfico no puede ser nulo")
    @Size(max = 20, message = "El código geográfico debe tener máximo 20 caracteres")
    private String codigoGeografico;

    @NotNull(message = "El código postal no puede ser nulo")
    @Size(max = 6, message = "El código postal debe tener máximo 6 caracteres")
    private String codigoPostal;

    // Campo opcional para especificar la provincia padre cuando codigoNivel = 2
    private Integer idProvinciaPadre;
} 