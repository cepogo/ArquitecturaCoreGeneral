package com.banquito.core.general.modelo;

import com.banquito.core.general.enums.EstadoGeneralEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Entity
@Table(name = "monedas")
public class Moneda {

    @Id
    @Column(name = "id_moneda", nullable = false, length = 3)
    private String idMoneda;

    @ManyToOne
    @JoinColumn(name = "id_pais", referencedColumnName = "id_pais", nullable = false)
    private Pais idPais;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "simbolo", nullable = false, length = 5)
    private String simbolo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 15)
    private EstadoGeneralEnum estado;

    @Column(name = "version", nullable = false, precision = 9)
    private Long version;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idMoneda == null) ? 0 : idMoneda.hashCode());
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
        Moneda other = (Moneda) obj;
        if (idMoneda == null) {
            if (other.idMoneda != null)
                return false;
        } else if (!idMoneda.equals(other.idMoneda))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Monedas [idMoneda=" + idMoneda + ", idPais=" + idPais + ", nombre=" + nombre + ", simbolo=" + simbolo
                + ", estado=" + estado + ", version=" + version + "]";
    }
} 