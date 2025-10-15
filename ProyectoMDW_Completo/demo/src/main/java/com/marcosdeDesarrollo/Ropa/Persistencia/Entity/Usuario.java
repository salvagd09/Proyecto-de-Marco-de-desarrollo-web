package com.marcosdeDesarrollo.Ropa.Persistencia.Entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;
    @Column(name="id_rol")
    private Integer idRol;
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "contrasena")
    private String password;

    @Column(name = "nombre_usuario", nullable = false, unique = true)
    private String nombreUsuario;

    @Column(name = "estado")
    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_rol",insertable=false,updatable = false)
    private Rol rol;
    @OneToMany(mappedBy="usuarioGasto")
    private List<Gastos> gastos;
    @OneToMany(mappedBy="usuarios")
    private List<Venta> ventas;
    @OneToMany(mappedBy="usuariosOC")
    private List<Ordenes_Compra> ordenesCompras;
    @OneToMany(mappedBy="usuarioMI")
    private List<Movimientos_inventario> movimientosInventarios;
    public Integer getId() {
        return idUsuario;
    }

    public Integer getIdRol() {
        return idRol;
    }
    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public List<Gastos> getGastos() {
        return gastos;
    }

    public void setGastos(List<Gastos> gastos) {
        this.gastos = gastos;
    }

    public List<Venta> getVentas() {
        return ventas;
    }

    public void setVentas(List<Venta> ventas) {
        this.ventas = ventas;
    }

    public List<Ordenes_Compra> getOrdenesCompras() {
        return ordenesCompras;
    }

    public void setOrdenesCompras(List<Ordenes_Compra> ordenesCompras) {
        this.ordenesCompras = ordenesCompras;
    }

    public List<Movimientos_inventario> getMovimientosInventarios() {
        return movimientosInventarios;
    }

    public void setMovimientosInventarios(List<Movimientos_inventario> movimientosInventarios) {
        this.movimientosInventarios = movimientosInventarios;
    }

    public void setId(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
