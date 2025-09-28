package com.marcosdeDesarrollo.demo.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
@Controller
@RequestMapping("/administrador")
public class AdministradorController {


    @GetMapping("/")
    public String vendedorDashboard() {
        return "Administrador/Ventana_Admin"; 
    }

    @GetMapping("/compras")
    public String compras() {
        return "Administrador/compras"; 
    }

    @GetMapping("/ventas")
    public String ventas() {
        return "Administrador/ventas"; 
    }

    @GetMapping("/gastos")
    public String gastos() {
        return "Administrador/gastos"; 
    }

    @GetMapping("/insumos")
    public String insumos() {
        return "Administrador/insumos"; 
    }

    @GetMapping("/productos")
    public String productos() {
        return "Administrador/productos"; 
    }

    @GetMapping("/roles")
    public String roles() {
        return "Administrador/roles"; 
    }
}

