package com.banquito.core.general.modelo;

import com.banquito.core.general.enums.EstadoLocacionesGeograficasEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "locaciones_geograficas")
public class LocacionGeografica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_locacion", nullable = false)
    private Integer idLocacion;

    @ManyToOne
    @JoinColumn(name = "id_locacion_padre", referencedColumnName = "id_locacion")
    private LocacionGeografica idLocacionPadre;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "id_pais", referencedColumnName = "id_pais", nullable = false),
            @JoinColumn(name = "codigo_nivel", referencedColumnName = "codigo_nivel", nullable = false)
    })
    private EstructuraGeografica estructuraGeografica;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "codigo_telefono_area", length = 3, nullable = false)
    private String codigoTelefonoArea;

    @Column(name = "codigo_geografico", length = 20, nullable = false)
    private String codigoGeografico;

    @Column(name = "codigo_postal", length = 6, nullable = false)
    private String codigoPostal;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 15, nullable = false)
    private EstadoLocacionesGeograficasEnum estado;

    @Column(name = "version", nullable = false)
    private Long version;

    public LocacionGeografica() {
    }

    public LocacionGeografica(Integer idLocacion) {
        this.idLocacion = idLocacion;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idLocacion == null) ? 0 : idLocacion.hashCode());
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
        LocacionGeografica other = (LocacionGeografica) obj;
        if (idLocacion == null) {
            if (other.idLocacion != null)
                return false;
        } else if (!idLocacion.equals(other.idLocacion))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "LocacionGeografica [idLocacion=" + idLocacion + ", idLocacionPadre=" + idLocacionPadre
                + ", estructuraGeografica=" + estructuraGeografica + ", nombre=" + nombre + ", codigoTelefonoArea="
                + codigoTelefonoArea + ", codigoGeografico=" + codigoGeografico + ", codigoPostal=" + codigoPostal
                + ", estado=" + estado + ", version=" + version + "]";
    }

}
