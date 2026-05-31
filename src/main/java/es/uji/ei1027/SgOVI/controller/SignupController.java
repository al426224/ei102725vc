package es.uji.ei1027.SgOVI.controller;

import es.uji.ei1027.SgOVI.dao.AsistentePersonalDao;
import es.uji.ei1027.SgOVI.dao.UsuarioOVIDao;
import es.uji.ei1027.SgOVI.model.AsistentePersonal;
import es.uji.ei1027.SgOVI.model.UsuarioOVI;
import es.uji.ei1027.SgOVI.services.ProyectoVidaService;
import es.uji.ei1027.SgOVI.validator.AsistentePersonalSignupValidator;
import es.uji.ei1027.SgOVI.validator.PdfFileValidator;
import es.uji.ei1027.SgOVI.validator.UsuarioOVISignupValidator;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RequestMapping("/signup")
@Controller
public class SignupController {

    private final UsuarioOVIDao usuarioOVIDao;
    private final AsistentePersonalDao asistentePersonalDao;
    private final ProyectoVidaService proyectoVidaService;

    @Autowired
    public SignupController(UsuarioOVIDao usuarioOVIDao, AsistentePersonalDao asistentePersonalDao,
                            ProyectoVidaService proyectoVidaService) {
        this.usuarioOVIDao = usuarioOVIDao;
        this.asistentePersonalDao = asistentePersonalDao;
        this.proyectoVidaService = proyectoVidaService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping
    public String showTipoRegistro() {
        return "signup/signup";
    }

    @RequestMapping(value = "/registerUsuarioOVI")
    public String showRegisterUsuarioOVIForm(Model model) {
        model.addAttribute("usuarioOVI", new UsuarioOVI());
        model.addAttribute("rawPassword", "");
        return "signup/signupUsuarioOVI";
    }


    @RequestMapping(value = "/registerAsistentePersonal")
    public String showRegisterAsistentePersonalForm(Model model) {
        model.addAttribute("asistente", new AsistentePersonal());
        return "signup/signupAsistentePersonal";
    }

    @RequestMapping(value = "/registerAsistentePersonal", method = RequestMethod.POST)
    public String registerAsistentePersonal(@ModelAttribute("asistente") AsistentePersonal asistente,
                                             BindingResult bindingResult, Model model) {
        AsistentePersonalSignupValidator validator = new AsistentePersonalSignupValidator(asistentePersonalDao);
        validator.validate(asistente, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("asistente", asistente);
            return "signup/signupAsistentePersonal";
        }

        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        asistente.setContrasena(passwordEncryptor.encryptPassword(asistente.getContrasena()));
        asistente.setEstadoValidacion("pendiente");
        asistentePersonalDao.addAsistente(asistente);
        return "redirect:/login?registered";
    }

    @RequestMapping(value = "/registerUsuarioOVI", method = RequestMethod.POST)
    public String registerUsuarioOVI(@ModelAttribute("usuarioOVI") @Validated UsuarioOVI usuarioOVI,
                                       BindingResult bindingResult,
                                       @RequestParam("archivo") MultipartFile archivo,
                                       Model model) {

        UsuarioOVISignupValidator validator = new UsuarioOVISignupValidator(usuarioOVIDao);
        validator.validate(usuarioOVI, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("usuarioOVI", usuarioOVI);
            model.addAttribute("rawPassword", usuarioOVI.getContrasena());
            return "signup/signupUsuarioOVI";
        }

        if (archivo != null && !archivo.isEmpty()) {
            PdfFileValidator pdfValidator = new PdfFileValidator();
            BeanPropertyBindingResult fileErrors = new BeanPropertyBindingResult(archivo, "archivo");
            pdfValidator.validate(archivo, fileErrors);
            if (fileErrors.hasErrors()) {
                String errorMsg = fileErrors.getGlobalError() != null ?
                                  fileErrors.getGlobalError().getDefaultMessage() :
                                  "El archivo no es valido";
                model.addAttribute("usuarioOVI", usuarioOVI);
                model.addAttribute("rawPassword", usuarioOVI.getContrasena());
                model.addAttribute("archivoError", errorMsg);
                return "signup/signupUsuarioOVI";
            }
        }

        usuarioOVI.setFechaRegistro(LocalDate.now());
        usuarioOVI.setEstado("pendiente");

        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        usuarioOVI.setContrasena(passwordEncryptor.encryptPassword(usuarioOVI.getContrasena()));

        usuarioOVIDao.addUsuario(usuarioOVI);

        if (archivo != null && !archivo.isEmpty()) {
            UsuarioOVI creado = usuarioOVIDao.getUsuarioByEmail(usuarioOVI.getEmail());
            if (creado != null) {
                try {
                    proyectoVidaService.guardarProyectoVida(archivo, creado.getIdUsuario());
                } catch (Exception e) {
                    // Si falla la subida del PDF, el usuario ya está creado
                }
            }
        }

        return "redirect:/login?registered";
    }
}
