package es.uji.ei1027.SgOVI.controller;

import es.uji.ei1027.SgOVI.dao.AsistentePersonalDao;
import es.uji.ei1027.SgOVI.dao.PeticionAPRDao;
import es.uji.ei1027.SgOVI.dao.RegistroContactoDao;
import es.uji.ei1027.SgOVI.dao.SeleccionDao;
import es.uji.ei1027.SgOVI.model.AsistentePersonal;
import es.uji.ei1027.SgOVI.model.PeticionAPR;
import es.uji.ei1027.SgOVI.model.RegistroContacto;
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
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/asistentePersonal")
public class AsistentePersonalController {

    private final AsistentePersonalDao asistentePersonalDao;
    private final SeleccionDao seleccionDao;
    private final PeticionAPRDao peticionAPRDao;
    private final RegistroContactoDao registroContactoDao;
    private final MatchingService matchingService;

    @Autowired
    public AsistentePersonalController(AsistentePersonalDao asistentePersonalDao, SeleccionDao seleccionDao,
                                       PeticionAPRDao peticionAPRDao, RegistroContactoDao registroContactoDao,
                                       MatchingService matchingService) {
        this.asistentePersonalDao = asistentePersonalDao;
        this.seleccionDao = seleccionDao;
        this.peticionAPRDao = peticionAPRDao;
        this.registroContactoDao = registroContactoDao;
        this.matchingService = matchingService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
    }

    @RequestMapping(value = "/home")
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
        int enContacto = 0, aceptadas = 0;
        for (PropuestaInfo p : propuestas) {
            String estado = p.seleccion.getEstadoSeleccion();
            if ("contactado".equals(estado)) enContacto++;
            else if ("aceptada".equals(estado)) aceptadas++;
        }

        model.addAttribute("asistente", asistente);
        model.addAttribute("propuestas", propuestas);
        model.addAttribute("propuestasRecibidas", propuestasRecibidas);
        model.addAttribute("enContacto", enContacto);
        model.addAttribute("aceptadas", aceptadas);
        return "asistentePersonal/home";
    }

    @RequestMapping(value = "/contratos")
    public String contratos(HttpSession session, Model model) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"asistente".equals(tipo)) {
            return "redirect:/login";
        }
        Integer idAsistente = (Integer) session.getAttribute("userId");
        if (idAsistente == null) {
            return "redirect:/login";
        }

        List<RegistroContacto> contratos = registroContactoDao.getRegistrosByAsistente(idAsistente);

        List<Integer> idsSeleccion = contratos.stream()
                .map(RegistroContacto::getIdSeleccion).distinct().collect(Collectors.toList());
        List<Seleccion> selecciones = idsSeleccion.isEmpty()
                ? new ArrayList<>() : seleccionDao.getSelecciones(idsSeleccion);
        Map<Integer, Seleccion> seleccionMap = selecciones.stream()
                .collect(Collectors.toMap(Seleccion::getIdSeleccion, s -> s));

        List<Integer> idsSolicitud = selecciones.stream()
                .map(Seleccion::getIdSolicitud).distinct().collect(Collectors.toList());
        Map<Integer, PeticionAPR> peticionMap = idsSolicitud.stream()
                .collect(Collectors.toMap(id -> id, id -> peticionAPRDao.getPeticionWithUser(id)));

        List<ContratoInfo> contratosActivos = new ArrayList<>();
        List<ContratoInfo> contratosFinalizados = new ArrayList<>();
        for (RegistroContacto c : contratos) {
            String nombreUsuario = "";
            Seleccion s = seleccionMap.get(c.getIdSeleccion());
            if (s != null) {
                PeticionAPR p = peticionMap.get(s.getIdSolicitud());
                if (p != null && p.getNombreUsuario() != null) {
                    nombreUsuario = p.getNombreUsuario();
                }
            }
            ContratoInfo info = new ContratoInfo(c, nombreUsuario);
            if ("finalizado".equals(c.getResultado()) || "cancelado".equals(c.getResultado())) {
                contratosFinalizados.add(info);
            } else {
                contratosActivos.add(info);
            }
        }

        AsistentePersonal asistente = asistentePersonalDao.getAsistente(idAsistente);
        model.addAttribute("asistente", asistente);
        model.addAttribute("contratosActivos", contratosActivos);
        model.addAttribute("contratosFinalizados", contratosFinalizados);
        return "asistentePersonal/contratos";
    }

    public static class ContratoInfo {
        private final RegistroContacto contrato;
        private final String nombreSolicitante;

        public ContratoInfo(RegistroContacto contrato, String nombreSolicitante) {
            this.contrato = contrato;
            this.nombreSolicitante = nombreSolicitante;
        }

        public RegistroContacto getContrato() { return contrato; }
        public String getNombreSolicitante() { return nombreSolicitante; }
    }

    public static class PropuestaInfo {
        public Seleccion seleccion;
        public PeticionAPR peticion;

        public PropuestaInfo(Seleccion seleccion, PeticionAPR peticion) {
            this.seleccion = seleccion;
            this.peticion = peticion;
        }
    }

    @RequestMapping(value = "/perfil/{id}")
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

    @RequestMapping(value = "/propuesta/{id}")
    public String verPropuesta(@PathVariable int id, HttpSession session, Model model) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"asistente".equals(tipo)) {
            return "redirect:/login";
        }

        PeticionAPR peticion = peticionAPRDao.getPeticion(id);
        if (peticion == null) {
            return "redirect:/asistentePersonal/home";
        }

        Seleccion seleccion = null;
        for (Seleccion s : seleccionDao.getSeleccionesByAsistenteNoRechazada((Integer) session.getAttribute("userId"))) {
            if (s.getIdSolicitud() == id) {
                seleccion = s;
                break;
            }
        }
        if (seleccion == null) {
            return "redirect:/asistentePersonal/home";
        }

        model.addAttribute("peticion", peticion);
        model.addAttribute("seleccion", seleccion);
        model.addAttribute("asistente", asistentePersonalDao.getAsistente((Integer) session.getAttribute("userId")));
        return "asistentePersonal/propuesta";
    }

    @RequestMapping(value = "/list")
    public String listAsistentes(Model model) {
        List<AsistentePersonal> asistentes = asistentePersonalDao.getAsistentes();
        model.addAttribute("asistentes", asistentes);
        return "asistentePersonal/list";
    }

    @RequestMapping(value = "/search")
    public String searchForm(Model model) {
        return "asistentePersonal/search";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
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

    @RequestMapping(value = "/add")
    public String addAsistenteForm(Model model) {
        model.addAttribute("asistentePersonal", new AsistentePersonal());
        return "asistentePersonal/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
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

    @RequestMapping(value = "/update/{id}")
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

    @RequestMapping(value = "/update", method = RequestMethod.POST)
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

    @RequestMapping(value = "/delete/{id}")
    public String deleteAsistente(@PathVariable int id, RedirectAttributes redirectAttributes) {
        asistentePersonalDao.deleteAsistente(id);
        redirectAttributes.addFlashAttribute("successMessage", "Asistente eliminado correctamente");
        return "redirect:/asistentePersonal/list";
    }



    @RequestMapping(value = "/validar/{id}")
    public String validarAsistenteForm(Model model, @PathVariable int id) {
        model.addAttribute("asistentePersonal", asistentePersonalDao.getAsistente(id));
        return "asistentePersonal/validar";
    }

    @RequestMapping(value = "/validar", method = RequestMethod.POST)
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

    @RequestMapping(value = "/approve/{id}", method = RequestMethod.POST)
    public String approveAsistente(@PathVariable int id, RedirectAttributes redirectAttributes) {
        AsistentePersonal asistente = asistentePersonalDao.getAsistente(id);
        if (asistente != null) {
            asistente.setEstadoValidacion("validado");
            asistentePersonalDao.updateAsistente(asistente);
            redirectAttributes.addFlashAttribute("successMessage", "Asistente aprobado correctamente");
        }
        return "redirect:/asistentePersonal/list";
    }

    @RequestMapping(value = "/reject/{id}", method = RequestMethod.POST)
    public String rejectAsistente(@PathVariable int id, RedirectAttributes redirectAttributes) {
        AsistentePersonal asistente = asistentePersonalDao.getAsistente(id);
        if (asistente != null) {
            asistente.setEstadoValidacion("rechazado");
            asistentePersonalDao.updateAsistente(asistente);
            redirectAttributes.addFlashAttribute("successMessage", "Asistente rechazado correctamente");
        }
        return "redirect:/asistentePersonal/list";
    }

    @RequestMapping(value = "/byEstado/{estado}")
    public String getAsistentesByEstado(Model model, @PathVariable String estado) {
        List<AsistentePersonal> asistentes = asistentePersonalDao.getAsistentesByEstado(estado);
        model.addAttribute("asistentes", asistentes);
        return "asistentePersonal/list";
    }

    @RequestMapping(value = "/mensajes")
    public String mensajes(HttpSession session, Model model) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"asistente".equals(tipo)) {
            return "redirect:/login";
        }
        Integer idAsistente = (Integer) session.getAttribute("userId");
        if (idAsistente != null) {
            model.addAttribute("asistente", asistentePersonalDao.getAsistente(idAsistente));
        }
        model.addAttribute("tipo", "asistentePersonal");
        return "mensajes/mensajes";
    }
}
