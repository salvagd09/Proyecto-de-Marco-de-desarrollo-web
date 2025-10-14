/*package com.marcosdeDesarrollo.Ropa.Persistencia.Entity;

import jakarta.persistence.*;

@Entity
@Table(name="rol_permisos")
public class Rol_permisos {
    @EmbeddedId
    private Rol_permisosID idRolP;
    @MapsId("idRol")
    @ManyToOne
    @JoinColumn(name="id_rol",nullable=false)
    private Rol roles;
    @MapsId("idPermiso")
    @ManyToOne
    @JoinColumn(name="id_permiso",nullable=false)
    private Permiso permisos;

    public Rol_permisosID getIdRolP() {
        return idRolP;
    }

    public void setIdRolP(Rol_permisosID idRolP) {
        this.idRolP = idRolP;
    }

    public Rol getRoles() {
        return roles;
    }

    public void setRoles(Rol roles) {
        this.roles = roles;
    }

    public Permiso getPermisos() {
        return permisos;
    }

    public void setPermisos(Permiso permisos) {
        this.permisos = permisos;
    }
}*/
