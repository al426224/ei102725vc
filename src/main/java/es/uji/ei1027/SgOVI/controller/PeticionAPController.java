package es.uji.ei1027.SgOVI.controller;

import es.uji.ei1027.SgOVI.dao.PeticionAPRDao;
import es.uji.ei1027.SgOVI.model.PeticionAPR;
import es.uji.ei1027.SgOVI.model.UsuarioOVI;
import es.uji.ei1027.SgOVI.validator.PeticionAPRSignupValidator;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/peticionAP")
public class PeticionAPController {

    private final PeticionAPRDao peticionAPRDao;
    private final PeticionAPRSignupValidator validator = new PeticionAPRSignupValidator();

    @Autowired
    public PeticionAPController(PeticionAPRDao peticionAPRDao) {
        this.peticionAPRDao = peticionAPRDao;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/mis-solicitudes")
    public String misSolicitudes(@RequestParam(value = "estado", required = false) String estado,
                                 @RequestParam(value = "ordenar", required = false) String ordenar,
                                 HttpSession session, Model model) {
        UsuarioOVI usuario = getUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        List<PeticionAPR> peticiones = peticionAPRDao.getPeticionesByUsuarioFiltrado(
                usuario.getIdUsuario(), estado, ordenar);
        model.addAttribute("peticiones", peticiones);
        model.addAttribute("usuario", usuario);
        model.addAttribute("estadoSeleccionado", estado);
        model.addAttribute("ordenarSeleccionado", ordenar);
        model.addAttribute("estadoLabels", Map.of(
                "en_revision", "En revision",
                "aprobada", "Aprobada",
                "rechazada", "Rechazada",
                "cancelada", "Cancelada",
                "cerrada_contrato", "Cerrada (contrato)",
                "cerrada_contrato_finalizado", "Finalizada"
        ));
        return "peticionAP/mis-solicitudes";
    }

    @PostMapping("/cancelar/{id}")
    public String cancelarSolicitud(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes) {
        UsuarioOVI usuario = getUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }

        PeticionAPR peticion = peticionAPRDao.getPeticion(id);
        if (peticion == null || peticion.getIdUsuario() != usuario.getIdUsuario()) {
            return "redirect:/peticionAP/mis-solicitudes";
        }

        if (!"en_revision".equals(peticion.getEstado())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Solo se pueden cancelar solicitudes en revision");
            return "redirect:/peticionAP/mis-solicitudes";
        }

        peticion.setEstado("cancelada");
        peticionAPRDao.updatePeticion(peticion);

        redirectAttributes.addFlashAttribute("successMessage", "Solicitud cancelada correctamente");
        return "redirect:/peticionAP/mis-solicitudes";
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
    public String crearSolicitud(@ModelAttribute("peticionAP") PeticionAPR peticionAP,
                                 BindingResult bindingResult,
                                 HttpSession session,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        UsuarioOVI usuario = getUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }

        validator.validate(peticionAP, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("peticionAP", peticionAP);
            model.addAttribute("usuario", usuario);
            return "peticionAP/nueva-solicitud";
        }

        peticionAP.setIdUsuario(usuario.getIdUsuario());
        peticionAP.setEstado("en_revision");

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
        model.addAttribute("estadoLabels", Map.of(
                "en_revision", "En revision",
                "aprobada", "Aprobada",
                "rechazada", "Rechazada",
                "cancelada", "Cancelada",
                "cerrada_contrato", "Cerrada (contrato)",
                "cerrada_contrato_finalizado", "Finalizada"
        ));
        return "peticionAP/detalle";
    }

    private UsuarioOVI getUsuarioSesion(HttpSession session) {
        Object tipo = session.getAttribute("tipo");
        Object usuario = session.getAttribute("usuario");
        if ("usuarioOVI".equals(tipo) && usuario instanceof UsuarioOVI) {
            UsuarioOVI uovi = (UsuarioOVI) usuario;
            if ("aceptado".equals(uovi.getEstado())) {
                return uovi;
            }
        }
        return null;
    }
}