package es.uji.ei1027.SgOVI.controller;

import es.uji.ei1027.SgOVI.dao.AsistentePersonalDao;
import es.uji.ei1027.SgOVI.dao.PeticionAPRDao;
import es.uji.ei1027.SgOVI.dao.SeleccionDao;
import es.uji.ei1027.SgOVI.model.AsistentePersonal;
import es.uji.ei1027.SgOVI.model.PeticionAPR;
import es.uji.ei1027.SgOVI.model.Seleccion;
import es.uji.ei1027.SgOVI.services.MatchingService;
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

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/asistentePersonal")
public class AsistentePersonalController {

    private final AsistentePersonalDao asistentePersonalDao;
    private final SeleccionDao seleccionDao;
    private final PeticionAPRDao peticionAPRDao;
    private final MatchingService matchingService;

    @Autowired
    public AsistentePersonalController(AsistentePersonalDao asistentePersonalDao, SeleccionDao seleccionDao, PeticionAPRDao peticionAPRDao, MatchingService matchingService) {
        this.asistentePersonalDao = asistentePersonalDao;
        this.seleccionDao = seleccionDao;
        this.peticionAPRDao = peticionAPRDao;
        this.matchingService = matchingService;
    }

@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
    }

    @GetMapping("/home")
    public String homeAsistente(HttpSession session, Model model) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"asistente".equals(tipo)) {
            return "redirect:/login";
        }
        Integer idAsistente = (Integer) session.getAttribute("userId");
        if (idAsistente == null) {
            return "redirect:/login";
        }

        AsistentePersonal asistente = asistentePersonalDao.getAsistente(idAsistente);
        if (asistente == null) {
            return "redirect:/login";
        }

        List<Seleccion> selecciones = seleccionDao.getSeleccionesByAsistenteNoRechazada(idAsistente);
        List<PropuestaInfo> propuestas = new ArrayList<>();
        for (Seleccion s : selecciones) {
            PeticionAPR p = peticionAPRDao.getPeticion(s.getIdSolicitud());
            if (p != null) {
                propuestas.add(new PropuestaInfo(s, p));
            }
        }

        int propuestasRecibidas = propuestas.size();
        int enContacto = (int) propuestas.stream().filter(p -> "contactado".equals(p.seleccion.getEstadoSeleccion())).count();
        int aceptadas = (int) propuestas.stream().filter(p -> "aceptada".equals(p.seleccion.getEstadoSeleccion())).count();

        model.addAttribute("asistente", asistente);
        model.addAttribute("propuestas", propuestas);
        model.addAttribute("propuestasRecibidas", propuestasRecibidas);
        model.addAttribute("enContacto", enContacto);
        model.addAttribute("aceptadas", aceptadas);
        return "asistentePersonal/home";
    }

    public static class PropuestaInfo {
        public Seleccion seleccion;
        public PeticionAPR peticion;

        public PropuestaInfo(Seleccion seleccion, PeticionAPR peticion) {
            this.seleccion = seleccion;
            this.peticion = peticion;
        }
    }

    @GetMapping("/perfil/{id}")
    public String perfilAsistente(@PathVariable int id, HttpSession session, Model model) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"asistente".equals(tipo)) {
            return "redirect:/login";
        }
        Integer sessionId = (Integer) session.getAttribute("userId");
        if (sessionId == null || sessionId != id) {
            return "redirect:/asistentePersonal/home";
        }
        AsistentePersonal asistente = asistentePersonalDao.getAsistente(id);
        if (asistente == null) {
            return "redirect:/asistentePersonal/home";
        }
        model.addAttribute("asistente", asistente);
        return "asistentePersonal/perfil";
    }

    @GetMapping("/propuesta/{id}")
    public String verPropuesta(@PathVariable int id, HttpSession session, Model model) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"asistente".equals(tipo)) {
            return "redirect:/login";
        }

        PeticionAPR peticion = peticionAPRDao.getPeticion(id);
        if (peticion == null) {
            return "redirect:/asistentePersonal/home";
        }

        Seleccion seleccion = seleccionDao.getSeleccionesByAsistenteNoRechazada((Integer) session.getAttribute("userId"))
                .stream().filter(s -> s.getIdSolicitud() == id).findFirst().orElse(null);
        if (seleccion == null) {
            return "redirect:/asistentePersonal/home";
        }

        model.addAttribute("peticion", peticion);
        model.addAttribute("seleccion", seleccion);
        model.addAttribute("asistente", asistentePersonalDao.getAsistente((Integer) session.getAttribute("userId")));
        return "asistentePersonal/propuesta";
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
    public String updateAsistenteForm(@PathVariable int id, HttpSession session, Model model) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"asistente".equals(tipo)) {
            return "redirect:/login";
        }
        Integer sessionId = (Integer) session.getAttribute("userId");
        if (sessionId == null || sessionId != id) {
            return "redirect:/login";
        }

        AsistentePersonal asistente = asistentePersonalDao.getAsistente(id);
        if (asistente == null) {
            return "redirect:/asistentePersonal/home";
        }
        if (!"aceptado".equals(asistente.getEstadoValidacion())) {
            return "redirect:/asistentePersonal/perfil/" + id;
        }

        model.addAttribute("asistente", asistente);
        return "asistentePersonal/update";
    }

    @PostMapping("/update")
    public String updateAsistente(@ModelAttribute("asistente") AsistentePersonal asistente,
                                  BindingResult bindingResult, HttpSession session,
                                  Model model, RedirectAttributes redirectAttributes) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"asistente".equals(tipo)) {
            return "redirect:/login";
        }
        Integer sessionId = (Integer) session.getAttribute("userId");
        if (sessionId == null || sessionId != asistente.getIdAsistente()) {
            return "redirect:/login";
        }

        asistentePersonalDao.updateAsistente(asistente);

        List<Seleccion> selecciones = seleccionDao.getSeleccionesByAsistente(asistente.getIdAsistente());
        for (Seleccion s : selecciones) {
            PeticionAPR peticion = peticionAPRDao.getPeticion(s.getIdSolicitud());
            if (peticion != null) {
                MatchingService.CandidatoSugerido cs = new MatchingService.CandidatoSugerido();
                cs.setAsistente(asistente);
                int puntos = matchingService.calcularPuntuacion(peticion, asistente, cs);
                seleccionDao.updatePuntuacionMatch(s.getIdSeleccion(), puntos);
            }
        }

        redirectAttributes.addFlashAttribute("successMessage", "Perfil actualizado correctamente");
        return "redirect:/asistentePersonal/perfil/" + asistente.getIdAsistente();
    }

    @GetMapping("/delete/{id}")
    public String deleteAsistente(@PathVariable int id, RedirectAttributes redirectAttributes) {
        asistentePersonalDao.deleteAsistente(id);
        redirectAttributes.addFlashAttribute("successMessage", "Asistente eliminado correctamente");
        return "redirect:/asistentePersonal/list";
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