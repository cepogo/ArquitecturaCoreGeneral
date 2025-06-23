package com.banquito.core.general.modelo;

import com.banquito.core.general.enums.EstadoGeneralEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "entidades_bancarias")
public class EntidadBancaria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entidad_bancaria", nullable = false)
    private Integer idEntidadBancaria;

    @Column(name = "codigo_local", nullable = false, length = 6)
    private String codigoLocal;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "codigo_internacional", nullable = false, length = 20)
    private String codigoInternacional;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 15)
    private EstadoGeneralEnum estado;

    @Column(name = "version", nullable = false, precision = 9)
    private Long version;

    public EntidadBancaria() {
    }

    public EntidadBancaria(Integer idEntidadBancaria) {
        this.idEntidadBancaria = idEntidadBancaria;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idEntidadBancaria == null) ? 0 : idEntidadBancaria.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EntidadBancaria other = (EntidadBancaria) obj;
        if (idEntidadBancaria == null) {
            if (other.idEntidadBancaria != null)
                return false;
        } else if (!idEntidadBancaria.equals(other.idEntidadBancaria))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "EntidadBancaria [idEntidadBancaria=" + idEntidadBancaria + ", codigoLocal=" + codigoLocal + ", nombre="
                + nombre + ", codigoInternacional=" + codigoInternacional + ", estado=" + estado + ", version="
                + version + "]";
    }

}