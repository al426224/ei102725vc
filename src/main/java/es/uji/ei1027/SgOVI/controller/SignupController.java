package es.uji.ei1027.SgOVI.controller;

import es.uji.ei1027.SgOVI.dao.UsuarioOVIDao;
import es.uji.ei1027.SgOVI.model.UsuarioOVI;
import es.uji.ei1027.SgOVI.validator.UsuarioOVISignupValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequestMapping("/signup")
@Controller
public class SignupController {

    private final UsuarioOVIDao usuarioOVIDao;

    @Autowired
    public SignupController(UsuarioOVIDao usuarioOVIDao) {
        this.usuarioOVIDao = usuarioOVIDao;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping
    public String showTipoRegistro() {
        return "signup/signup";
    }

    @GetMapping("/registerUsuarioOVI")
    public String showRegisterUsuarioOVIForm(Model model) {
        model.addAttribute("usuarioOVI", new UsuarioOVI());
        return "signup/signupUsuarioOVI";
    }

    @PostMapping("/registerUsuarioOVI")
    public String registerUsuarioOVI(@ModelAttribute("usuarioOVI") @Validated UsuarioOVI usuarioOVI,
                                       BindingResult bindingResult, Model model) {

        UsuarioOVISignupValidator validator = new UsuarioOVISignupValidator(usuarioOVIDao);
        validator.validate(usuarioOVI, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("usuarioOVI", usuarioOVI);
            return "signup/signupUsuarioOVI";
        }

        usuarioOVI.setFechaRegistro(LocalDate.now());
        usuarioOVI.setEstado("pendiente");

        usuarioOVIDao.addUsuario(usuarioOVI);

        return "registro-exitoso";
    }
}