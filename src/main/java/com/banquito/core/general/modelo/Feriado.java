package com.banquito.core.general.modelo;

import com.banquito.core.general.enums.EstadoGeneralEnum;
import com.banquito.core.general.enums.TipoFeriadosEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "feriados")
@Getter
@Setter
public class Feriado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_feriado", nullable = false)
    private Integer idFeriado;

    @Column(name = "fecha", nullable = false)
    private Date fecha;

    @ManyToOne
    @JoinColumn(name = "id_pais", referencedColumnName = "id_pais")
    private Pais idPais;

    @ManyToOne
    @JoinColumn(name = "id_locacion", referencedColumnName = "id_locacion")
    private LocacionGeografica idLocacion;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 15)
    private TipoFeriadosEnum tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 15)
    private EstadoGeneralEnum estado = EstadoGeneralEnum.ACTIVO;

    @Column(name = "version", nullable = false)
    private Long version;

    public Feriado() {
    }

    public Feriado(Integer idFeriado) {
        this.idFeriado = idFeriado;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idFeriado == null) ? 0 : idFeriado.hashCode());
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
        Feriado other = (Feriado) obj;
        if (idFeriado == null) {
            if (other.idFeriado != null)
                return false;
        } else if (!idFeriado.equals(other.idFeriado))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Feriado [idFeriado=" + idFeriado + ", fecha=" + fecha + ", idPais=" + idPais + ", idLocacion="
                + idLocacion + ", nombre=" + nombre + ", tipo=" + tipo + ", estado=" + estado + ", version=" + version
                + "]";
    }

}
