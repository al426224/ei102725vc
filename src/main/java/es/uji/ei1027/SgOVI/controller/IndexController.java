package es.uji.ei1027.SgOVI.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String index(Model model, HttpSession session) {
        String tipo = (String) session.getAttribute("tipo");
        if (tipo != null) {
            switch (tipo) {
                case "usuarioOVI":
                    return "redirect:/usuarioOVI/homeUsuarioOVI";
                case "asistente":
                    return "redirect:/asistentePersonal/home";
                case "tecnicoovi":
                    return "redirect:/tecnico/home";
                case "formador":
                    return "redirect:/formador/homeFormador";
            }
        }
        model.addAttribute("usuario", session.getAttribute("usuario"));
        model.addAttribute("tipo", session.getAttribute("tipo"));
        return "index";
    }
}