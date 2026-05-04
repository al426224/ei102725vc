package es.uji.ei1027.SgOVI.controller;

import es.uji.ei1027.SgOVI.dao.AsistentePersonalDao;
import es.uji.ei1027.SgOVI.dao.UsuarioOVIDao;
import es.uji.ei1027.SgOVI.model.AsistentePersonal;
import es.uji.ei1027.SgOVI.model.UsuarioOVI;
import es.uji.ei1027.SgOVI.validator.UsuarioOVISignupValidator;
import org.jasypt.util.password.BasicPasswordEncryptor;
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
    private final AsistentePersonalDao asistentePersonalDao;

    @Autowired
    public SignupController(UsuarioOVIDao usuarioOVIDao) {
        this.usuarioOVIDao = usuarioOVIDao;
        this.asistentePersonalDao = new AsistentePersonalDao();
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


    @GetMapping("/registerAsistentePersonal")
    public String showRegisterAsistentePersonalForm(Model model) {
        model.addAttribute("asistente", new AsistentePersonal()); // Inyectamos objeto para Thymeleaf[cite: 1, 2]
        return "signup/signupAsistentePersonal";
    }

    @PostMapping("/registerAsistentePersonal")
    public String registerAsistentePersonal(@ModelAttribute("asistente") AsistentePersonal asistente) {
        // Encriptar contraseña antes de persistir[cite: 2]
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        asistente.setContrasena(passwordEncryptor.encryptPassword(asistente.getContrasena()));

        // Establecer estado por defecto[cite: 2]
        asistente.setEstadoValidacion("pendiente");

        // Guardar en la base de datos usando el nuevo DAO[cite: 2]
        asistentePersonalDao.addAsistente(asistente);

        return "registro-exitoso";
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

        // Encriptar contraseña antes de guardar
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        usuarioOVI.setContrasena(passwordEncryptor.encryptPassword(usuarioOVI.getContrasena()));

        usuarioOVIDao.addUsuario(usuarioOVI);

        return "registro-exitoso";
    }
}