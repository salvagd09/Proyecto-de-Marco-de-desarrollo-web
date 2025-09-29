package com.marcosdeDesarrollo.demo.DTO;
import com.marcosdeDesarrollo.demo.Entity.Estado;
public class ActualizarEstadoProductoRequest {
        private Estado estado;

        public Estado getEstado() {
            return estado;
        }

        public void setEstado(Estado estado) {
            this.estado = estado;
        }
}
