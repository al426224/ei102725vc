package es.uji.ei1027.SgOVI.controller;

import es.uji.ei1027.SgOVI.dao.AsistentePersonalDao;
import es.uji.ei1027.SgOVI.dao.PeticionAPRDao;
import es.uji.ei1027.SgOVI.dao.SeleccionDao;
import es.uji.ei1027.SgOVI.model.AsistentePersonal;
import es.uji.ei1027.SgOVI.model.PeticionAPR;
import es.uji.ei1027.SgOVI.model.Seleccion;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/peticionAP")
public class PeticionAPController {

    private final PeticionAPRDao peticionAPRDao;
    private final SeleccionDao seleccionDao;
    private final AsistentePersonalDao asistentePersonalDao;
    private final PeticionAPRSignupValidator validator = new PeticionAPRSignupValidator();

    @Autowired
    public PeticionAPController(PeticionAPRDao peticionAPRDao, SeleccionDao seleccionDao, AsistentePersonalDao asistentePersonalDao) {
        this.peticionAPRDao = peticionAPRDao;
        this.seleccionDao = seleccionDao;
        this.asistentePersonalDao = asistentePersonalDao;
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

    @GetMapping("/candidatos/{idSolicitud}")
    public String verCandidatos(@PathVariable int idSolicitud, HttpSession session, Model model) {
        UsuarioOVI usuario = getUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }

        PeticionAPR peticion = peticionAPRDao.getPeticion(idSolicitud);
        if (peticion == null || peticion.getIdUsuario() != usuario.getIdUsuario()) {
            return "redirect:/peticionAP/mis-solicitudes";
        }

        if (!"aprobada".equals(peticion.getEstado())) {
            return "redirect:/peticionAP/detalle/" + idSolicitud;
        }

        List<Seleccion> propuestas = seleccionDao.getSeleccionesBySolicitudAndEstado(idSolicitud, "propuesta");
        List<CandidatoOVI> candidatos = new ArrayList<>();
        for (Seleccion s : propuestas) {
            AsistentePersonal a = asistentePersonalDao.getAsistente(s.getIdAsistente());
            if (a != null) {
                CandidatoOVI c = new CandidatoOVI();
                c.setSeleccion(s);
                c.setAsistente(a);
                candidatos.add(c);
            }
        }

        model.addAttribute("peticion", peticion);
        model.addAttribute("candidatos", candidatos);
        model.addAttribute("estadoLabels", Map.of(
                "en_revision", "En revision",
                "aprobada", "Aprobada",
                "rechazada", "Rechazada",
                "cancelada", "Cancelada",
                "cerrada_contrato", "Cerrada (contrato)",
                "cerrada_contrato_finalizado", "Finalizada"
        ));

        return "peticionAP/candidatos";
    }

    public static class CandidatoOVI {
        private Seleccion seleccion;
        private AsistentePersonal asistente;

        public Seleccion getSeleccion() { return seleccion; }
        public void setSeleccion(Seleccion seleccion) { this.seleccion = seleccion; }
        public AsistentePersonal getAsistente() { return asistente; }
        public void setAsistente(AsistentePersonal asistente) { this.asistente = asistente; }
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