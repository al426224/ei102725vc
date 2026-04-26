package es.uji.ei1027.SgOVI.controller;

import es.uji.ei1027.SgOVI.dao.AsistentePersonalDao;
import es.uji.ei1027.SgOVI.dao.FormadorDao;
import es.uji.ei1027.SgOVI.dao.UsuarioOVIDao;
import es.uji.ei1027.SgOVI.model.AsistentePersonal;
import es.uji.ei1027.SgOVI.model.Formador;
import es.uji.ei1027.SgOVI.model.UsuarioOVI;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UsuarioOVIDao usuarioOVIDao;
    
    @Autowired
    private AsistentePersonalDao asistentePersonalDao;
    
    @Autowired
    private FormadorDao formadorDao;

    @RequestMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String checkLogin(@RequestParam String email, 
                          @RequestParam String password,
                          HttpSession session) {
        
        UsuarioOVI usuarioOVI = usuarioOVIDao.auth(email, password);
        if (usuarioOVI != null) {
            session.setAttribute("usuario", usuarioOVI);
            session.setAttribute("tipo", "usuarioOVI");
            return "redirect:/";
        }
        
        AsistentePersonal asistente = asistentePersonalDao.auth(email, password);
        if (asistente != null) {
            session.setAttribute("usuario", asistente);
            session.setAttribute("tipo", "asistente");
            return "redirect:/";
        }
        
        Formador formador = formadorDao.auth(email, password);
        if (formador != null) {
            session.setAttribute("usuario", formador);
            session.setAttribute("tipo", "formador");
            return "redirect:/";
        }
        
        return "redirect:/login?error";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}