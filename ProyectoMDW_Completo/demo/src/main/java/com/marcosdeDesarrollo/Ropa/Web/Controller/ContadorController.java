package com.marcosdeDesarrollo.Ropa.Web.Controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contador")
public class ContadorController {

    @GetMapping("/")
    public String vendedorDashboard() {
        return "Contador/PanelPrincipalContador"; 
    }

    @GetMapping("/ventas")
    public String productos() {
        return "Contador/ventasContador";
    }

    @GetMapping("/gastos")
    public String gastos() {
        return "Contador/GastosContador"; 
    }
}
