package com.banquito.core.general.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "sucursales")
public class Sucursal {
    @Id
    @Column(name = "codigo", nullable = false, length = 10)
    private String codigo;

    @ManyToOne
    @JoinColumn(name = "id_entidad_bancaria", referencedColumnName = "id_entidad_bancaria")
    private EntidadBancaria idEntidadBancaria;

    @ManyToOne
    @JoinColumn(name = "id_locacion", referencedColumnName = "id_locacion")
    private LocacionGeografica idLocacion;

    @Column(name = "nombre", nullable = false, length = 30)
    private String nombre;

    @Column(name = "fecha_creacion", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;

    @Column(name = "correo_electronico", nullable = false, length = 40)
    private String correoElectronico;

    @Column(name = "telefono", nullable = false, length = 10)
    private String telefono;

    @Column(name = "direccion_linea1", nullable = false, length = 150)
    private String direccionLinea1;

    @Column(name = "direccion_linea2", length = 150)
    private String direccionLinea2;

    @Column(name = "latitud", nullable = false, precision = 10, scale = 8)
    private BigDecimal latitud;

    @Column(name = "longitud", nullable = false, precision = 11, scale = 8)
    private BigDecimal longitud;

    @Column(name = "estado", nullable = false, length = 15)
    private String estado;

    @Column(name = "version", nullable = false)
    private Long version;

    public Sucursal() {
    }

    public Sucursal(String codigo) {
        this.codigo = codigo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
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
        Sucursal other = (Sucursal) obj;
        if (codigo == null) {
            if (other.codigo != null)
                return false;
        } else if (!codigo.equals(other.codigo))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Sucursal [codigo=" + codigo + ", idEntidadBancaria=" + idEntidadBancaria + ", idLocacion=" + idLocacion
                + ", nombre=" + nombre + ", fechaCreacion=" + fechaCreacion + ", correoElectronico=" + correoElectronico
                + ", telefono=" + telefono + ", direccionLinea1=" + direccionLinea1 + ", direccionLinea2="
                + direccionLinea2 + ", latitud=" + latitud + ", longitud=" + longitud + ", estado=" + estado
                + ", version=" + version + "]";
    }

}