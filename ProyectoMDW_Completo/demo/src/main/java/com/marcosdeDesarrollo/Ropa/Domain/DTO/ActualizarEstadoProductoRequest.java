package com.marcosdeDesarrollo.Ropa.Domain.DTO;

import com.marcosdeDesarrollo.Ropa.Persistencia.Entity.Estado;

public class ActualizarEstadoProductoRequest {

    private Estado estado;

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
