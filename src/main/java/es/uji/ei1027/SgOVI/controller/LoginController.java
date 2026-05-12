package es.uji.ei1027.SgOVI.controller;

import es.uji.ei1027.SgOVI.dao.AsistentePersonalDao;
import es.uji.ei1027.SgOVI.dao.FormadorDao;
import es.uji.ei1027.SgOVI.dao.TecnicoOVIDao;
import es.uji.ei1027.SgOVI.dao.UserDao;
import es.uji.ei1027.SgOVI.dao.UsuarioOVIDao;
import es.uji.ei1027.SgOVI.model.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UsuarioOVIDao usuarioOVIDao;

    @Autowired
    private AsistentePersonalDao asistentePersonalDao;

    @Autowired
    private FormadorDao formadorDao;

    @Autowired
    private TecnicoOVIDao tecnicoOVIDao;

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new UserDetails());
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String checkLogin(@ModelAttribute("user") UserDetails user,
                             BindingResult bindingResult, HttpSession session) {
        UserValidator userValidator = new UserValidator();
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "login";
        }

        UserDetails authenticatedUser = userDao.loadUserByUsername(
                user.getUsername(), user.getPassword());

        if (authenticatedUser == null) {
            bindingResult.rejectValue("password", "badpw", "Contrasenya incorrecta");
            return "login";
        }

        session.setAttribute("user", authenticatedUser);

        UsuarioOVI uovi = usuarioOVIDao.getUsuarioByEmail(authenticatedUser.getUsername());
        if (uovi != null) {
            if ("aceptado".equals(uovi.getEstado())) {
                session.setAttribute("usuario", uovi);
                session.setAttribute("tipo", "usuarioOVI");
                session.setAttribute("rol", Rol.USUARIOOVI);
                return "redirect:/usuarioOVI/homeUsuarioOVI";
            }
            if ("rechazado".equals(uovi.getEstado())) {
                session.invalidate();
                bindingResult.rejectValue("password", "CuentaRechazada",
                        "Tu cuenta ha sido rechazada. No cumples con los requerimientos del sistema.");
            } else {
                session.invalidate();
                bindingResult.rejectValue("password", "CuentaPendiente",
                        "Tu cuenta aún está pendiente de revisión. Espera a que un técnico la valide.");
            }
            return "login";
        }

        AsistentePersonal asistente = asistentePersonalDao.getAsistenteByEmail(authenticatedUser.getUsername());
        if (asistente != null) {
            if ("aceptado".equals(asistente.getEstadoValidacion())) {
                session.setAttribute("usuario", asistente);
                session.setAttribute("userId", asistente.getIdAsistente());
                session.setAttribute("tipo", "asistente");
                session.setAttribute("rol", Rol.ASISTENTE);
                return "redirect:/asistentePersonal/home";
            }
            if ("rechazado".equals(asistente.getEstadoValidacion())) {
                session.invalidate();
                bindingResult.rejectValue("password", "CuentaRechazada",
                        "Tu cuenta ha sido rechazada. No cumples con los requerimientos del sistema.");
            } else {
                session.invalidate();
                bindingResult.rejectValue("password", "CuentaPendiente",
                        "Tu cuenta aún está pendiente de revisión. Espera a que un técnico la valide.");
            }
            return "login";
        }

        Formador formador = formadorDao.getFormadorByEmail(authenticatedUser.getUsername());
        if (formador != null) {
            session.setAttribute("usuario", formador);
            session.setAttribute("tipo", "formador");
            session.setAttribute("rol", Rol.FORMADOR);
            return "redirect:/";
        }

        TecnicoOVI tecnico = tecnicoOVIDao.getTecnicoByEmail(authenticatedUser.getUsername());
        if (tecnico != null) {
            session.setAttribute("usuario", tecnico);
            session.setAttribute("tipo", "tecnicoovi");
            session.setAttribute("rol", Rol.TECNICOOVI);
            return "redirect:/tecnico/home";
        }

        return "redirect:/";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
