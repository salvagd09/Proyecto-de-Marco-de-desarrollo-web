package com.marcosdeDesarrollo.Ropa.Domain.DTO;

import java.util.List;

public class RolRequestDto {
    private String nombre;
    private String descripcion;
    private List<Integer> permisosIds;

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

    public List<Integer> getPermisosIds() {
        return permisosIds;
    }

    public void setPermisosIds(List<Integer> permisosIds) {
        this.permisosIds = permisosIds;
    }
}
