package es.uji.ei1027.SgOVI.controller;

import es.uji.ei1027.SgOVI.dao.FormadorDao;
import es.uji.ei1027.SgOVI.model.Formador;
import es.uji.ei1027.SgOVI.validator.FormadorEditarValidator;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/formador")
public class FormadorController {

    private final FormadorDao formadorDao;
    private final Logger logger = Logger.getLogger(FormadorController.class.getName());

    @Autowired
    public FormadorController(FormadorDao formadorDao) {
        this.formadorDao = formadorDao;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
    }

    private String getRedirectUrl(String redirectUrl, String defaultUrl) {
        return (redirectUrl != null) ? "redirect:" + redirectUrl : "redirect:" + defaultUrl;
    }

    @GetMapping("/list")
    public String listFormadores(Model model) {
        List<Formador> formadores = formadorDao.getFormadores();
        model.addAttribute("formadores", formadores);
        return "formador/list";
    }

    @GetMapping("/search")
    public String searchForm(Model model) {
        return "formador/search";
    }

    @PostMapping("/search")
    public String processSearch(@RequestParam String tipo, @RequestParam String valor, Model model, RedirectAttributes redirectAttributes) {
        Formador formador = null;
        
        if (tipo.equals("id")) {
            formador = formadorDao.getFormador(Integer.parseInt(valor));
        } else if (tipo.equals("email")) {
            formador = formadorDao.getFormadorByEmail(valor);
        }
        
        if (formador == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Formador no encontrado");
            return "redirect:/formador/search";
        }
        
        model.addAttribute("formador", formador);
        return "formador/search";
    }

    @GetMapping("/update/{id}")
    public String editFormador(Model model, @PathVariable int id) {
        model.addAttribute("formador", formadorDao.getFormador(id));
        return "formador/update";
    }

    @PostMapping("/update")
    public String processUpdateSubmit(@ModelAttribute("formador") @Validated Formador formador, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        FormadorEditarValidator validator = new FormadorEditarValidator(formadorDao, String.valueOf(formador.getIdFormador()));
        validator.validate(formador, bindingResult);

        if (bindingResult.hasErrors()) {
            return "formador/update";
        }
        
        formadorDao.updateFormador(formador);
        redirectAttributes.addFlashAttribute("successMessage", "Formador actualizado correctamente");
        return "redirect:/formador/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteFormador(@PathVariable int id, RedirectAttributes redirectAttributes) {
        formadorDao.deleteFormador(id);
        redirectAttributes.addFlashAttribute("successMessage", "Formador eliminado correctamente");
        return "redirect:/formador/list";
    }

    @GetMapping("/perfil/{id}")
    public String perfilFormador(Model model, @PathVariable int id) {
        Formador formador = formadorDao.getFormador(id);

        if (formador == null) {
            return "redirect:/formador/list";
        }

        model.addAttribute("formador", formador);
        return "formador/perfil";
    }

    @GetMapping("/homeFormador")
    public String homeFormador(HttpSession session, Model model) {
        Formador formador = (Formador) session.getAttribute("usuario");

        if (formador == null) {
            return "redirect:/login";
        }

        model.addAttribute("formador", formador);
        return "formador/home";
    }
}