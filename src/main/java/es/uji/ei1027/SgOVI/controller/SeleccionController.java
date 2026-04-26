package es.uji.ei1027.SgOVI.controller;

import es.uji.ei1027.SgOVI.dao.SeleccionDao;
import es.uji.ei1027.SgOVI.model.Seleccion;
import es.uji.ei1027.SgOVI.validator.SeleccionValidator;
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
@RequestMapping("/seleccion")
public class SeleccionController {

    private final SeleccionDao seleccionDao;
    private final Logger logger = Logger.getLogger(SeleccionController.class.getName());

    @Autowired
    public SeleccionController(SeleccionDao seleccionDao) {
        this.seleccionDao = seleccionDao;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
    }

    @GetMapping("/list")
    public String listSelecciones(Model model) {
        List<Seleccion> selecciones = seleccionDao.getSelecciones();
        model.addAttribute("selecciones", selecciones);
        return "seleccion/list";
    }

    @GetMapping("/add")
    public String addSeleccionForm(Model model) {
        model.addAttribute("seleccion", new Seleccion());
        return "seleccion/add";
    }

    @PostMapping("/add")
    public String addSeleccion(@ModelAttribute("seleccion") @Validated Seleccion seleccion,
                              BindingResult bindingResult, Model model,
                              RedirectAttributes redirectAttributes) {
        SeleccionValidator validator = new SeleccionValidator();
        validator.validate(seleccion, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("seleccion", seleccion);
            return "seleccion/add";
        }

        if (seleccion.getEstadoSeleccion() == null || seleccion.getEstadoSeleccion().trim().isEmpty()) {
            seleccion.setEstadoSeleccion("pendiente");
        }
        if (seleccion.getPuntuacionMatch() == null) {
            seleccion.setPuntuacionMatch(0);
        }

        seleccionDao.addSeleccion(seleccion);
        redirectAttributes.addFlashAttribute("successMessage", "Selección creada correctamente");
        return "redirect:/seleccion/list";
    }

    @GetMapping("/update/{id}")
    public String updateSeleccionForm(Model model, @PathVariable int id) {
        model.addAttribute("seleccion", seleccionDao.getSeleccion(id));
        return "seleccion/update";
    }

    @PostMapping("/update")
    public String updateSeleccion(@ModelAttribute("seleccion") @Validated Seleccion seleccion,
                                BindingResult bindingResult, Model model,
                                RedirectAttributes redirectAttributes) {
        SeleccionValidator validator = new SeleccionValidator();
        validator.validate(seleccion, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("seleccion", seleccion);
            return "seleccion/update";
        }

        seleccionDao.updateSeleccion(seleccion);
        redirectAttributes.addFlashAttribute("successMessage", "Selección actualizada correctamente");
        return "redirect:/seleccion/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteSeleccion(@PathVariable int id, RedirectAttributes redirectAttributes) {
        seleccionDao.deleteSeleccion(id);
        redirectAttributes.addFlashAttribute("successMessage", "Selección eliminada correctamente");
        return "redirect:/seleccion/list";
    }

    @PostMapping("/accept/{id}")
    public String acceptSeleccion(@PathVariable int id, RedirectAttributes redirectAttributes) {
        Seleccion seleccion = seleccionDao.getSeleccion(id);
        if (seleccion != null) {
            seleccion.setEstadoSeleccion("aceptada");
            seleccionDao.updateSeleccion(seleccion);
            redirectAttributes.addFlashAttribute("successMessage", "Selección aceptada correctamente");
        }
        return "redirect:/seleccion/list";
    }

    @PostMapping("/reject/{id}")
    public String rejectSeleccion(@PathVariable int id, RedirectAttributes redirectAttributes) {
        Seleccion seleccion = seleccionDao.getSeleccion(id);
        if (seleccion != null) {
            seleccion.setEstadoSeleccion("rechazada");
            seleccionDao.updateSeleccion(seleccion);
            redirectAttributes.addFlashAttribute("successMessage", "Selección rechazada correctamente");
        }
        return "redirect:/seleccion/list";
    }

    @GetMapping("/bySolicitud/{idSolicitud}")
    public String getSeleccionesBySolicitud(Model model, @PathVariable int idSolicitud) {
        List<Seleccion> selecciones = seleccionDao.getSeleccionesBySolicitud(idSolicitud);
        model.addAttribute("selecciones", selecciones);
        return "seleccion/list";
    }

    @GetMapping("/byAsistente/{idAsistente}")
    public String getSeleccionesByAsistente(Model model, @PathVariable int idAsistente) {
        List<Seleccion> selecciones = seleccionDao.getSeleccionesByAsistente(idAsistente);
        model.addAttribute("selecciones", selecciones);
        return "seleccion/list";
    }
}