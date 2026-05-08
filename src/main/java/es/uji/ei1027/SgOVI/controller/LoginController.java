package es.uji.ei1027.SgOVI.controller;

import es.uji.ei1027.SgOVI.dao.AsistentePersonalDao;
import es.uji.ei1027.SgOVI.dao.FormadorDao;
import es.uji.ei1027.SgOVI.dao.UsuarioOVIDao;
import es.uji.ei1027.SgOVI.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;

class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> cls) {
        return Usuario.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        Usuario user = (Usuario) obj;
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            errors.rejectValue("username", "username.empty", "El email no puede estar vacío");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            errors.rejectValue("password", "password.empty", "La contraseña no puede estar vacía");
        }
    }
}

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
        model.addAttribute("userLogin", new Usuario());
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String checkLogin(@ModelAttribute("userLogin") Usuario usuario, BindingResult bindingResult, HttpSession session) {
        UserValidator userValidator = new UserValidator();
        userValidator.validate(usuario, bindingResult);

        if (bindingResult.hasErrors()) {
            return "login";
        }

        UsuarioOVI usuarioOVI = usuarioOVIDao.auth(usuario.getUsername(), usuario.getPassword());
        if (usuarioOVI != null) {
            session.setAttribute("usuario", usuarioOVI);
            session.setAttribute("tipo", "usuarioOVI");
            session.setAttribute("rol", Rol.USUARIOOVI);
            return "redirect:/usuarioOVI/homeUsuarioOVI";
        }

        AsistentePersonal asistente = asistentePersonalDao.auth(usuario.getUsername(), usuario.getPassword());
        if (asistente != null) {
            if ("pendiente".equals(asistente.getEstadoValidacion())) {
                bindingResult.rejectValue("password", "NotAcceptedYet", "Tu solicitud aún no ha sido aceptada");
                return "login";
            }
            session.setAttribute("usuario", asistente);
            session.setAttribute("tipo", "asistente");
            session.setAttribute("rol", Rol.ASISTENTE);
            return "redirect:/";
        }

        Formador formador = formadorDao.auth(usuario.getUsername(), usuario.getPassword());
        if (formador != null) {
            session.setAttribute("usuario", formador);
            session.setAttribute("tipo", "formador");
            session.setAttribute("rol", Rol.FORMADOR);
            return "redirect:/";
        }

        bindingResult.rejectValue("password", "IncorrectPassword", "Usuario o contraseña incorrectos");
        return "login";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}