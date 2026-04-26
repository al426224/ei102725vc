package es.uji.ei1027.SgOVI.controller;

import es.uji.ei1027.SgOVI.dao.AsistentePersonalDao;
import es.uji.ei1027.SgOVI.model.AsistentePersonal;
import es.uji.ei1027.SgOVI.validator.AsistentePersonalSignupValidator;
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
@RequestMapping("/asistentePersonal")
public class AsistentePersonalController {

    private final AsistentePersonalDao asistentePersonalDao;
    private final Logger logger = Logger.getLogger(AsistentePersonalController.class.getName());

    @Autowired
    public AsistentePersonalController(AsistentePersonalDao asistentePersonalDao) {
        this.asistentePersonalDao = asistentePersonalDao;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
    }

    @GetMapping("/list")
    public String listAsistentes(Model model) {
        List<AsistentePersonal> asistentes = asistentePersonalDao.getAsistentes();
        model.addAttribute("asistentes", asistentes);
        return "asistentePersonal/list";
    }

    @GetMapping("/search")
    public String searchForm(Model model) {
        return "asistentePersonal/search";
    }

    @PostMapping("/search")
    public String processSearch(@RequestParam String tipo, @RequestParam String valor, Model model, RedirectAttributes redirectAttributes) {
        AsistentePersonal asistente = null;
        
        if (tipo.equals("id")) {
            asistente = asistentePersonalDao.getAsistente(Integer.parseInt(valor));
        } else if (tipo.equals("email")) {
            asistente = asistentePersonalDao.getAsistenteByEmail(valor);
        }
        
        if (asistente == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Asistente no encontrado");
            return "redirect:/asistentePersonal/search";
        }
        
        model.addAttribute("asistente", asistente);
        return "asistentePersonal/search";
    }

    @GetMapping("/add")
    public String addAsistenteForm(Model model) {
        model.addAttribute("asistentePersonal", new AsistentePersonal());
        return "asistentePersonal/add";
    }

    @PostMapping("/add")
    public String addAsistente(@ModelAttribute("asistentePersonal") @Validated AsistentePersonal asistente,
                               BindingResult bindingResult, Model model,
                               RedirectAttributes redirectAttributes) {
        AsistentePersonalSignupValidator validator = new AsistentePersonalSignupValidator(asistentePersonalDao);
        validator.validate(asistente, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("asistentePersonal", asistente);
            return "asistentePersonal/add";
        }

        if (asistente.getEstadoValidacion() == null || asistente.getEstadoValidacion().trim().isEmpty()) {
            asistente.setEstadoValidacion("pendiente");
        }

        asistentePersonalDao.addAsistente(asistente);
        redirectAttributes.addFlashAttribute("successMessage", "Asistente registrado correctamente");
        return "redirect:/asistentePersonal/list";
    }

    @GetMapping("/update/{id}")
    public String updateAsistenteForm(Model model, @PathVariable int id) {
        model.addAttribute("asistentePersonal", asistentePersonalDao.getAsistente(id));
        return "asistentePersonal/update";
    }

    @PostMapping("/update")
    public String updateAsistente(@ModelAttribute("asistentePersonal") @Validated AsistentePersonal asistente,
                                  BindingResult bindingResult, Model model,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("asistentePersonal", asistente);
            return "asistentePersonal/update";
        }

        asistentePersonalDao.updateAsistente(asistente);
        redirectAttributes.addFlashAttribute("successMessage", "Asistente actualizado correctamente");
        return "redirect:/asistentePersonal/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteAsistente(@PathVariable int id, RedirectAttributes redirectAttributes) {
        asistentePersonalDao.deleteAsistente(id);
        redirectAttributes.addFlashAttribute("successMessage", "Asistente eliminado correctamente");
        return "redirect:/asistentePersonal/list";
    }

    @GetMapping("/perfil/{id}")
    public String perfilAsistente(Model model, @PathVariable int id) {
        AsistentePersonal asistente = asistentePersonalDao.getAsistente(id);

        if (asistente == null) {
            return "redirect:/asistentePersonal/list";
        }

        model.addAttribute("asistente", asistente);
        return "asistentePersonal/perfil";
    }

    @GetMapping("/validar/{id}")
    public String validarAsistenteForm(Model model, @PathVariable int id) {
        model.addAttribute("asistentePersonal", asistentePersonalDao.getAsistente(id));
        return "asistentePersonal/validar";
    }

    @PostMapping("/validar")
    public String validarAsistente(@ModelAttribute("asistentePersonal") @Validated AsistentePersonal asistente,
                                  BindingResult bindingResult, Model model,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("asistentePersonal", asistente);
            return "asistentePersonal/validar";
        }

        asistentePersonalDao.updateAsistente(asistente);
        redirectAttributes.addFlashAttribute("successMessage", "Asistente validado correctamente");
        return "redirect:/asistentePersonal/list";
    }

    @PostMapping("/approve/{id}")
    public String approveAsistente(@PathVariable int id, RedirectAttributes redirectAttributes) {
        AsistentePersonal asistente = asistentePersonalDao.getAsistente(id);
        if (asistente != null) {
            asistente.setEstadoValidacion("validado");
            asistentePersonalDao.updateAsistente(asistente);
            redirectAttributes.addFlashAttribute("successMessage", "Asistente aprobado correctamente");
        }
        return "redirect:/asistentePersonal/list";
    }

    @PostMapping("/reject/{id}")
    public String rejectAsistente(@PathVariable int id, RedirectAttributes redirectAttributes) {
        AsistentePersonal asistente = asistentePersonalDao.getAsistente(id);
        if (asistente != null) {
            asistente.setEstadoValidacion("rechazado");
            asistentePersonalDao.updateAsistente(asistente);
            redirectAttributes.addFlashAttribute("successMessage", "Asistente rechazado correctamente");
        }
        return "redirect:/asistentePersonal/list";
    }

    @GetMapping("/byTipo/{tipo}")
    public String getAsistentesByTipo(Model model, @PathVariable String tipo) {
        List<AsistentePersonal> asistentes = asistentePersonalDao.getAsistentesByTipo(tipo);
        model.addAttribute("asistentes", asistentes);
        return "asistentePersonal/list";
    }

    @GetMapping("/byEstado/{estado}")
    public String getAsistentesByEstado(Model model, @PathVariable String estado) {
        List<AsistentePersonal> asistentes = asistentePersonalDao.getAsistentesByEstado(estado);
        model.addAttribute("asistentes", asistentes);
        return "asistentePersonal/list";
    }
}