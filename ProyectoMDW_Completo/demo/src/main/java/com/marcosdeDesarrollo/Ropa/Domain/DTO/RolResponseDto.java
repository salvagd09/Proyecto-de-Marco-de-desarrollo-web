package com.marcosdeDesarrollo.Ropa.Domain.DTO;

import java.time.LocalDateTime;
import java.util.List;

public class RolResponseDto {
    private Integer id;
    private String nombre;
    private String descripcion;
    private long usuariosAsignados;
    private LocalDateTime fechaCreacion;
    private List<PermisoResponseDto> permisos;
    private LocalDateTime ultimaActualizacion;
    private String actualizadoPor;
    private List<String> usuarios;
    private long usuariosActivos;
    private long usuariosInactivos;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public long getUsuariosAsignados() {
        return usuariosAsignados;
    }

    public void setUsuariosAsignados(long usuariosAsignados) {
        this.usuariosAsignados = usuariosAsignados;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public List<PermisoResponseDto> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<PermisoResponseDto> permisos) {
        this.permisos = permisos;
    }

    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public String getActualizadoPor() {
        return actualizadoPor;
    }

    public void setActualizadoPor(String actualizadoPor) {
        this.actualizadoPor = actualizadoPor;
    }

    public List<String> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<String> usuarios) {
        this.usuarios = usuarios;
    }

    public long getUsuariosActivos() {
        return usuariosActivos;
    }

    public void setUsuariosActivos(long usuariosActivos) {
        this.usuariosActivos = usuariosActivos;
    }

    public long getUsuariosInactivos() {
        return usuariosInactivos;
    }

    public void setUsuariosInactivos(long usuariosInactivos) {
        this.usuariosInactivos = usuariosInactivos;
    }
}
