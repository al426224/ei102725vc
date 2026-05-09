package es.uji.ei1027.SgOVI.controller;

import es.uji.ei1027.SgOVI.dao.PeticionAPRDao;
import es.uji.ei1027.SgOVI.model.PeticionAPR;
import es.uji.ei1027.SgOVI.model.UsuarioOVI;
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

@Controller
@RequestMapping("/peticionAP")
public class PeticionAPController {

    private final PeticionAPRDao peticionAPRDao;

    @Autowired
    public PeticionAPController(PeticionAPRDao peticionAPRDao) {
        this.peticionAPRDao = peticionAPRDao;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
    }

    @GetMapping("/mis-solicitudes")
    public String misSolicitudes(HttpSession session, Model model) {
        UsuarioOVI usuario = getUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        List<PeticionAPR> peticiones = peticionAPRDao.getPeticionesByUsuario(usuario.getIdUsuario());
        model.addAttribute("peticiones", peticiones);
        model.addAttribute("usuario", usuario);
        return "peticionAP/mis-solicitudes";
    }

    @GetMapping("/nueva")
    public String nuevaSolicitudForm(HttpSession session, Model model) {
        UsuarioOVI usuario = getUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        model.addAttribute("peticionAP", new PeticionAPR());
        model.addAttribute("usuario", usuario);
        return "peticionAP/nueva-solicitud";
    }

    @PostMapping("/nueva")
    public String crearSolicitud(@ModelAttribute("peticionAP") @Validated PeticionAPR peticionAP,
                                 BindingResult bindingResult,
                                 HttpSession session,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        UsuarioOVI usuario = getUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("peticionAP", peticionAP);
            return "peticionAP/nueva-solicitud";
        }

        peticionAP.setIdUsuario(usuario.getIdUsuario());
        peticionAP.setEstado("en revisión");
        
        peticionAPRDao.addPeticion(peticionAP);
        
        redirectAttributes.addFlashAttribute("successMessage", "Solicitud creada correctamente");
        return "redirect:/peticionAP/mis-solicitudes";
    }

    @GetMapping("/detalle/{id}")
    public String detalleSolicitud(@PathVariable int id, HttpSession session, Model model) {
        UsuarioOVI usuario = getUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        
        PeticionAPR peticion = peticionAPRDao.getPeticion(id);
        if (peticion == null || peticion.getIdUsuario() != usuario.getIdUsuario()) {
            return "redirect:/peticionAP/mis-solicitudes";
        }
        
        model.addAttribute("peticionAP", peticion);
        model.addAttribute("usuario", usuario);
        return "peticionAP/detalle";
    }

private UsuarioOVI getUsuarioSesion(HttpSession session) {
        Object tipo = session.getAttribute("tipo");
        Object usuario = session.getAttribute("usuario");
        if ("usuarioOVI".equals(tipo) && usuario instanceof UsuarioOVI) {
            UsuarioOVI uovi = (UsuarioOVI) usuario;
            if ("activo".equals(uovi.getEstado())) {
                return uovi;
            }
        }
        return null;
    }
}