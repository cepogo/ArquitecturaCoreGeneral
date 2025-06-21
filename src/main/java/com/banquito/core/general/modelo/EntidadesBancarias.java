package com.banquito.core.general.modelo;

import jakarta.persistence.*;

import java.math.BigDecimal;


@Entity
@Table(name = "entidades_bancarias")
public class EntidadesBancarias {
    @Id
    @Column(name = "id_entidad_bancaria", nullable = false)
    private Integer id;

    @Column(name = "codigo_local", nullable = false, length = 6)
    private String codigoLocal;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "codigo_internacional", nullable = false, length = 20)
    private String codigoInternacional;

    @Column(name = "estado", nullable = false, length = 15)
    private String estado;

    @Column(name = "version", nullable = false, precision = 9)
    private BigDecimal version;
   
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoLocal() {
        return codigoLocal;
    }

    public void setCodigoLocal(String codigoLocal) {
        this.codigoLocal = codigoLocal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigoInternacional() {
        return codigoInternacional;
    }

    public void setCodigoInternacional(String codigoInternacional) {
        this.codigoInternacional = codigoInternacional;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigDecimal getVersion() {
        return version;
    }

    public void setVersion(BigDecimal version) {
        this.version = version;
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
        EntidadesBancarias other = (EntidadesBancarias) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "EntidadesBancarias [id=" + id + ", codigoLocal=" + codigoLocal + ", nombre=" + nombre
                + ", codigoInternacional=" + codigoInternacional + ", estado=" + estado + ", version=" + version + "]";
    }

}