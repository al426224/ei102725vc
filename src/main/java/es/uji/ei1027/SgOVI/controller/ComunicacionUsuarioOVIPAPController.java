package es.uji.ei1027.SgOVI.controller;

import es.uji.ei1027.SgOVI.dao.ComunicacionUsuarioOVIPAPDao;
import es.uji.ei1027.SgOVI.model.ComunicacionUsuarioOVIPAP;
import es.uji.ei1027.SgOVI.validator.ComunicacionUsuarioOVIPAPValidator;
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
@RequestMapping("/comunicacionUsuarioOVIPAP")
public class ComunicacionUsuarioOVIPAPController {

    private final ComunicacionUsuarioOVIPAPDao comunicacionDao;
    private final Logger logger = Logger.getLogger(ComunicacionUsuarioOVIPAPController.class.getName());

    @Autowired
    public ComunicacionUsuarioOVIPAPController(ComunicacionUsuarioOVIPAPDao comunicacionDao) {
        this.comunicacionDao = comunicacionDao;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
    }

    @RequestMapping(value = "/list")
    public String listComunicaciones(Model model) {
        List<ComunicacionUsuarioOVIPAP> comunicaciones = comunicacionDao.getComunicaciones();
        model.addAttribute("comunicaciones", comunicaciones);
        return "comunicacionUsuarioOVIPAP/list";
    }

    @RequestMapping(value = "/add")
    public String addComunicacionForm(Model model) {
        model.addAttribute("comunicacion", new ComunicacionUsuarioOVIPAP());
        return "comunicacionUsuarioOVIPAP/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addComunicacion(@ModelAttribute("comunicacion") @Validated ComunicacionUsuarioOVIPAP comunicacion,
                                  BindingResult bindingResult, Model model,
                                  RedirectAttributes redirectAttributes) {
        ComunicacionUsuarioOVIPAPValidator validator = new ComunicacionUsuarioOVIPAPValidator();
        validator.validate(comunicacion, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("comunicacion", comunicacion);
            return "comunicacionUsuarioOVIPAP/add";
        }

        comunicacionDao.addComunicacion(comunicacion);
        redirectAttributes.addFlashAttribute("successMessage", "Comunicación creada correctamente");
        return "redirect:/comunicacionUsuarioOVIPAP/list";
    }

    @RequestMapping(value = "/update/{id}")
    public String updateComunicacionForm(Model model, @PathVariable int id) {
        model.addAttribute("comunicacion", comunicacionDao.getComunicacion(id));
        return "comunicacionUsuarioOVIPAP/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateComunicacion(@ModelAttribute("comunicacion") @Validated ComunicacionUsuarioOVIPAP comunicacion,
                                     BindingResult bindingResult, Model model,
                                     RedirectAttributes redirectAttributes) {
        ComunicacionUsuarioOVIPAPValidator validator = new ComunicacionUsuarioOVIPAPValidator();
        validator.validate(comunicacion, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("comunicacion", comunicacion);
            return "comunicacionUsuarioOVIPAP/update";
        }

        comunicacionDao.updateComunicacion(comunicacion);
        redirectAttributes.addFlashAttribute("successMessage", "Comunicación actualizada correctamente");
        return "redirect:/comunicacionUsuarioOVIPAP/list";
    }

    @RequestMapping(value = "/delete/{id}")
    public String deleteComunicacion(@PathVariable int id, RedirectAttributes redirectAttributes) {
        comunicacionDao.deleteComunicacion(id);
        redirectAttributes.addFlashAttribute("successMessage", "Comunicación eliminada correctamente");
        return "redirect:/comunicacionUsuarioOVIPAP/list";
    }

    @RequestMapping(value = "/bySeleccion/{idSeleccion}")
    public String getComunicacionesBySeleccion(Model model, @PathVariable int idSeleccion) {
        List<ComunicacionUsuarioOVIPAP> comunicaciones = comunicacionDao.getComunicacionesBySeleccion(idSeleccion);
        model.addAttribute("comunicaciones", comunicaciones);
        return "comunicacionUsuarioOVIPAP/list";
    }

    @RequestMapping(value = "/byEmisor/{emisor}")
    public String getComunicacionesByEmisor(Model model, @PathVariable String emisor) {
        List<ComunicacionUsuarioOVIPAP> comunicaciones = comunicacionDao.getComunicacionesByEmisor(emisor);
        model.addAttribute("comunicaciones", comunicaciones);
        return "comunicacionUsuarioOVIPAP/list";
    }
}
