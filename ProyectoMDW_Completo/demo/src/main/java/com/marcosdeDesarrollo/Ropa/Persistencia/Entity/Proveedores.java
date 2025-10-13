/* package com.marcosdeDesarrollo.demo.Entity;

import jakarta.persistence.*;

@Entity
@Table(name="proveedores")
public class Proveedores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_proveedor")
    private Integer ID_Proveedor;
    private String nombre_Proveedor;
    private String contacto;
    private String telefono;
    private String email;

    public Integer getID_Proveedor() {
        return ID_Proveedor;
    }

    public void setID_Proveedor(Integer ID_Proveedor) {
        this.ID_Proveedor = ID_Proveedor;
    }

    public String getNombre_Proveedor() {
        return nombre_Proveedor;
    }

    public void setNombre_Proveedor(String nombre_Proveedor) {
        this.nombre_Proveedor = nombre_Proveedor;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
//     */