package com.banquito.core.general.modelo;

import com.banquito.core.general.enums.EstadoGeneralEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "estructuras_geograficas")
public class EstructuraGeografica {

    @EmbeddedId
    private EstructuraGeograficaId id;

    @ManyToOne
    @MapsId("idPais")
    @JoinColumn(name = "id_pais", referencedColumnName = "id_pais", nullable = false)
    private Pais idPais;

    @Column(name = "nombre", length = 25, nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 15, nullable = false)
    private EstadoGeneralEnum estado = EstadoGeneralEnum.ACTIVO;

    @Column(name = "version", nullable = false)
    private Long version;

    public EstructuraGeografica() {
    }

    public EstructuraGeografica(EstructuraGeograficaId id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        EstructuraGeografica other = (EstructuraGeografica) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "EstructuraGeografica [id=" + id + ", idPais=" + idPais + ", nombre=" + nombre + ", estado=" + estado
                + ", version=" + version + "]";
    }

}
