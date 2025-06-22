package com.banquito.core.general.modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
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

    // Getters y Setters
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocacionGeografica getIdLocacion() {
        return idLocacion;
    }

    public void setIdLocacion(LocacionGeografica idLocacion) {
        this.idLocacion = idLocacion;
    }

    public EntidadBancaria getIdEntidadBancaria() {
        return idEntidadBancaria;
    }

    public void setIdEntidadBancaria(EntidadBancaria idEntidadBancaria) {
        this.idEntidadBancaria = idEntidadBancaria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccionLinea1() {
        return direccionLinea1;
    }

    public void setDireccionLinea1(String direccionLinea1) {
        this.direccionLinea1 = direccionLinea1;
    }

    public String getDireccionLinea2() {
        return direccionLinea2;
    }

    public void setDireccionLinea2(String direccionLinea2) {
        this.direccionLinea2 = direccionLinea2;
    }

    public BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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