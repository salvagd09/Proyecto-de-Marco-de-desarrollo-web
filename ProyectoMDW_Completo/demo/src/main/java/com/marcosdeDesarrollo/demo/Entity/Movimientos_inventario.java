package com.marcosdeDesarrollo.demo.Entity;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

@Entity
@Table(name="movimientos_inventario")
public class Movimientos_inventario {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_movimiento")
    private Integer id_movimiento;
    @ManyToOne
    @JoinColumn(name="id_producto",nullable=false)
    private Integer id_producto;
    @ManyToOne
    @JoinColumn(name="id_usuario",nullable=false)
    private Integer id_usuario;
    @Temporal(TemporalType.DATE)
    @Value("{fecha:current_timestamp}")
    private Date fecha;
    @Enumerated(EnumType.STRING)
    private tipo tipo;
    private enum tipo{
        Entrada,Salida
    }
    private String referencia;
    @Column(name="cantidad",nullable=false)
    private int cantidad;
    private int stock_anterior;
    private int stock_nuevo;

}
