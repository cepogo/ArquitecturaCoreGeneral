package com.banquito.core.general.modelo;

import com.banquito.core.general.enums.EstadoGeneralEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;


@Setter
@Getter
@Entity
@Table(name = "paises")
public class Pais {

    @Id
    @Column(name = "id_pais", length = 2, nullable = false)
    private String idPais;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "codigo_telefono", length = 4, nullable = false)
    private String codigoTelefono;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 15, nullable = false)
    private EstadoGeneralEnum estado;

    @Column(name = "version", precision = 9, nullable = false)
    private Long version;

    public Pais() {
    }

    public Pais(String idPais) {
        this.idPais = idPais;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pais pais = (Pais) o;
        return Objects.equals(idPais, pais.idPais);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idPais);
    }

    @Override
    public String toString() {
        return "Pais{" +
                "idPais='" + idPais + '\'' +
                ", nombre='" + nombre + '\'' +
                ", codigoTelefono='" + codigoTelefono + '\'' +
                ", estado='" + estado + '\'' +
                ", version=" + version +
                '}';
    }

}
