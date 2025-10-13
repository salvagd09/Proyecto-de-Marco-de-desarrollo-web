package com.marcosdeDesarrollo.Ropa.Web.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("/vendedor")
public class VendedorController {

    @GetMapping("/")
    public String vendedorDashboard() {
        return "Vendedor/Ventana_Vendedor"; 
    }

    @GetMapping("/productos")
    public String productos() {
        return "Vendedor/Ventas_Productos"; 
    }

    @GetMapping("/ventas")
    public String ventas() {
        return "Vendedor/Ventas_Vendedor"; 
    }

    @GetMapping("/insumos")
    public String insumos() {
        return "Vendedor/Ventas_Insumos"; 
    }
}
