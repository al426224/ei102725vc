package es.uji.ei1027.SgOVI.controller;

import es.uji.ei1027.SgOVI.dao.PeticionAPRDao;
import es.uji.ei1027.SgOVI.model.PeticionAPR;
import es.uji.ei1027.SgOVI.validator.PeticionAPRSignupValidator;
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
@RequestMapping("/peticionAPR")
public class PeticionAPRController {

    private final PeticionAPRDao peticionAPRDao;
    private final Logger logger = Logger.getLogger(PeticionAPRController.class.getName());

    @Autowired
    public PeticionAPRController(PeticionAPRDao peticionAPRDao) {
        this.peticionAPRDao = peticionAPRDao;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
    }

    @GetMapping("/list")
    public String listPeticiones(Model model) {
        List<PeticionAPR> peticiones = peticionAPRDao.getPeticiones();
        model.addAttribute("peticiones", peticiones);
        return "peticionAPR/list";
    }

    @GetMapping("/add")
    public String addPeticionForm(Model model) {
        model.addAttribute("peticionAPR", new PeticionAPR());
        return "peticionAPR/add";
    }

    @PostMapping("/add")
    public String addPeticion(@ModelAttribute("peticionAPR") @Validated PeticionAPR peticionAPR,
                               BindingResult bindingResult, Model model,
                               RedirectAttributes redirectAttributes) {
        PeticionAPRSignupValidator validator = new PeticionAPRSignupValidator();
        validator.validate(peticionAPR, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("peticionAPR", peticionAPR);
            return "peticionAPR/add";
        }

        if (peticionAPR.getEstado() == null || peticionAPR.getEstado().trim().isEmpty()) {
            peticionAPR.setEstado("pendiente");
        }

        peticionAPRDao.addPeticion(peticionAPR);
        redirectAttributes.addFlashAttribute("successMessage", "Petición creada correctamente");
        return "redirect:/peticionAPR/list";
    }

    @GetMapping("/update/{id}")
    public String updatePeticionForm(Model model, @PathVariable int id) {
        model.addAttribute("peticionAPR", peticionAPRDao.getPeticion(id));
        return "peticionAPR/update";
    }

    @PostMapping("/update")
    public String updatePeticion(@ModelAttribute("peticionAPR") @Validated PeticionAPR peticionAPR,
                                   BindingResult bindingResult, Model model,
                                   RedirectAttributes redirectAttributes) {
        PeticionAPRSignupValidator validator = new PeticionAPRSignupValidator();
        validator.validate(peticionAPR, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("peticionAPR", peticionAPR);
            return "peticionAPR/update";
        }

        peticionAPRDao.updatePeticion(peticionAPR);
        redirectAttributes.addFlashAttribute("successMessage", "Petición actualizada correctamente");
        return "redirect:/peticionAPR/list";
    }

    @GetMapping("/delete/{id}")
    public String deletePeticion(@PathVariable int id, RedirectAttributes redirectAttributes) {
        peticionAPRDao.deletePeticion(id);
        redirectAttributes.addFlashAttribute("successMessage", "Petición eliminada correctamente");
        return "redirect:/peticionAPR/list";
    }

    @GetMapping("/byUsuario/{idUsuario}")
    public String getPeticionesByUsuario(Model model, @PathVariable int idUsuario) {
        List<PeticionAPR> peticiones = peticionAPRDao.getPeticionesByUsuario(idUsuario);
        model.addAttribute("peticiones", peticiones);
        return "peticionAPR/list";
    }

    @GetMapping("/byEstado/{estado}")
    public String getPeticionesByEstado(Model model, @PathVariable String estado) {
        List<PeticionAPR> peticiones = peticionAPRDao.getPeticionesByEstado(estado);
        model.addAttribute("peticiones", peticiones);
        return "peticionAPR/list";
    }

    @GetMapping("/byTipo/{tipo}")
    public String getPeticionesByTipo(Model model, @PathVariable String tipo) {
        List<PeticionAPR> peticiones = peticionAPRDao.getPeticionesByTipo(tipo);
        model.addAttribute("peticiones", peticiones);
        return "peticionAPR/list";
    }
}