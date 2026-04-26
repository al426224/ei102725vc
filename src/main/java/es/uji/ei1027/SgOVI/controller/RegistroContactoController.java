package es.uji.ei1027.SgOVI.controller;

import es.uji.ei1027.SgOVI.dao.RegistroContactoDao;
import es.uji.ei1027.SgOVI.model.RegistroContacto;
import es.uji.ei1027.SgOVI.validator.RegistroContactoValidator;
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

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/registroContacto")
public class RegistroContactoController {

    private final RegistroContactoDao registroContactoDao;
    private final Logger logger = Logger.getLogger(RegistroContactoController.class.getName());

    @Autowired
    public RegistroContactoController(RegistroContactoDao registroContactoDao) {
        this.registroContactoDao = registroContactoDao;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
    }

    @GetMapping("/list")
    public String listRegistros(Model model) {
        List<RegistroContacto> registros = registroContactoDao.getRegistros();
        model.addAttribute("registros", registros);
        return "registroContacto/list";
    }

    @GetMapping("/add")
    public String addRegistroForm(Model model) {
        model.addAttribute("registroContacto", new RegistroContacto());
        return "registroContacto/add";
    }

    @PostMapping("/add")
    public String addRegistro(@ModelAttribute("registroContacto") @Validated RegistroContacto registro,
                                BindingResult bindingResult, Model model,
                                RedirectAttributes redirectAttributes) {
        RegistroContactoValidator validator = new RegistroContactoValidator();
        validator.validate(registro, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("registroContacto", registro);
            return "registroContacto/add";
        }

        if (registro.getFechaInicio() == null) {
            registro.setFechaInicio(LocalDate.now());
        }
        if (registro.getResultado() == null || registro.getResultado().trim().isEmpty()) {
            registro.setResultado("pendiente");
        }

        registroContactoDao.addRegistro(registro);
        redirectAttributes.addFlashAttribute("successMessage", "Registro de contacto creado correctamente");
        return "redirect:/registroContacto/list";
    }

    @GetMapping("/update/{id}")
    public String updateRegistroForm(Model model, @PathVariable int id) {
        model.addAttribute("registroContacto", registroContactoDao.getRegistro(id));
        return "registroContacto/update";
    }

    @PostMapping("/update")
    public String updateRegistro(@ModelAttribute("registroContacto") @Validated RegistroContacto registro,
                                   BindingResult bindingResult, Model model,
                                   RedirectAttributes redirectAttributes) {
        RegistroContactoValidator validator = new RegistroContactoValidator();
        validator.validate(registro, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("registroContacto", registro);
            return "registroContacto/update";
        }

        registroContactoDao.updateRegistro(registro);
        redirectAttributes.addFlashAttribute("successMessage", "Registro actualizado correctamente");
        return "redirect:/registroContacto/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteRegistro(@PathVariable int id, RedirectAttributes redirectAttributes) {
        registroContactoDao.deleteRegistro(id);
        redirectAttributes.addFlashAttribute("successMessage", "Registro eliminado correctamente");
        return "redirect:/registroContacto/list";
    }

    @GetMapping("/bySeleccion/{idSeleccion}")
    public String getRegistrosBySeleccion(Model model, @PathVariable int idSeleccion) {
        List<RegistroContacto> registros = registroContactoDao.getRegistrosBySeleccion(idSeleccion);
        model.addAttribute("registros", registros);
        return "registroContacto/list";
    }

    @GetMapping("/byResultado/{resultado}")
    public String getRegistrosByResultado(Model model, @PathVariable String resultado) {
        List<RegistroContacto> registros = registroContactoDao.getRegistrosByResultado(resultado);
        model.addAttribute("registros", registros);
        return "registroContacto/list";
    }

    @PostMapping("/finalize/{id}")
    public String finalizeRegistro(@PathVariable int id, RedirectAttributes redirectAttributes) {
        RegistroContacto registro = registroContactoDao.getRegistro(id);
        if (registro != null) {
            registro.setResultado("finalizado");
            registro.setFechaFin(LocalDate.now());
            registroContactoDao.updateRegistro(registro);
            redirectAttributes.addFlashAttribute("successMessage", "Registro finalizado correctamente");
        }
        return "redirect:/registroContacto/list";
    }

    @PostMapping("/cancel/{id}")
    public String cancelRegistro(@PathVariable int id, RedirectAttributes redirectAttributes) {
        RegistroContacto registro = registroContactoDao.getRegistro(id);
        if (registro != null) {
            registro.setResultado("cancelado");
            registro.setFechaFin(LocalDate.now());
            registroContactoDao.updateRegistro(registro);
            redirectAttributes.addFlashAttribute("successMessage", "Registro cancelado correctamente");
        }
        return "redirect:/registroContacto/list";
    }
}