package com.marcosdeDesarrollo.Ropa.Domain.DTO;

import com.marcosdeDesarrollo.Ropa.Persistencia.Entity.Estado;
import java.time.LocalDateTime;

public class CategoriaDto {
    private Integer idCategoria;
    private String nombreCategoria;
    private String descripcion;
    private Estado estado;
    private LocalDateTime fechaCreacion;

    public Integer getIdCategoria() {
        return idCategoria;
    }
    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
