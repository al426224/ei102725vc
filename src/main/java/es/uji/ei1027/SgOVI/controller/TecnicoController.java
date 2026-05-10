package es.uji.ei1027.SgOVI.controller;

import es.uji.ei1027.SgOVI.dao.AsistentePersonalDao;
import es.uji.ei1027.SgOVI.dao.PeticionAPRDao;
import es.uji.ei1027.SgOVI.dao.SeleccionDao;
import es.uji.ei1027.SgOVI.dao.TecnicoOVIDao;
import es.uji.ei1027.SgOVI.dao.UsuarioOVIDao;
import es.uji.ei1027.SgOVI.model.AsistentePersonal;
import es.uji.ei1027.SgOVI.model.PeticionAPR;
import es.uji.ei1027.SgOVI.model.TecnicoOVI;
import es.uji.ei1027.SgOVI.model.Seleccion;
import es.uji.ei1027.SgOVI.model.UsuarioOVI;
import es.uji.ei1027.SgOVI.services.MatchingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/tecnico")
public class TecnicoController {

    private final TecnicoOVIDao tecnicoOVIDao;
    private final UsuarioOVIDao usuarioOVIDao;
    private final AsistentePersonalDao asistentePersonalDao;
    private final PeticionAPRDao peticionAPRDao;
    private final SeleccionDao seleccionDao;
    private final MatchingService matchingService;

    @Autowired
    public TecnicoController(TecnicoOVIDao tecnicoOVIDao, UsuarioOVIDao usuarioOVIDao,
                              AsistentePersonalDao asistentePersonalDao, PeticionAPRDao peticionAPRDao,
                              SeleccionDao seleccionDao, MatchingService matchingService) {
        this.tecnicoOVIDao = tecnicoOVIDao;
        this.usuarioOVIDao = usuarioOVIDao;
        this.asistentePersonalDao = asistentePersonalDao;
        this.peticionAPRDao = peticionAPRDao;
        this.seleccionDao = seleccionDao;
        this.matchingService = matchingService;
    }

    @GetMapping("/home")
    public String homeTecnico(@RequestParam(value = "estado", required = false) String estado,
                               HttpSession session, Model model) {
        Object usuario = session.getAttribute("usuario");
        Object tipo = session.getAttribute("tipo");

        if (usuario == null || !"tecnicoovi".equals(tipo)) {
            return "redirect:/login";
        }

        if (usuario instanceof TecnicoOVI) {
            model.addAttribute("tecnico", (TecnicoOVI) usuario);
        }

        List<UsuarioOVI> usuarios = usuarioOVIDao.getUsuariosByEstado(estado);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("estadoSeleccionado", estado);

        return "tecnico/listUsuarioOVI";
    }

    @GetMapping("/usuario/{id}")
    public String verUsuario(@PathVariable int id, HttpSession session, Model model) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"tecnicoovi".equals(tipo)) {
            return "redirect:/login";
        }

        UsuarioOVI usuario = usuarioOVIDao.getUsuario(id);
        if (usuario == null) {
            return "redirect:/tecnico/home";
        }

        model.addAttribute("usuario", usuario);
        return "tecnico/infoUsuarioOVI";
    }

    @PostMapping("/usuario/approve/{id}")
    public String approveUsuario(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"tecnicoovi".equals(tipo)) {
            return "redirect:/login";
        }

        UsuarioOVI usuario = usuarioOVIDao.getUsuario(id);
        if (usuario != null) {
            usuario.setEstado("aceptado");
            usuarioOVIDao.updateUsuario(usuario);
            redirectAttributes.addFlashAttribute("successMessage", "Usuario aprobado correctamente");
        }
        return "redirect:/tecnico/home";
    }

    @PostMapping("/usuario/reject/{id}")
    public String rejectUsuario(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"tecnicoovi".equals(tipo)) {
            return "redirect:/login";
        }

        UsuarioOVI usuario = usuarioOVIDao.getUsuario(id);
        if (usuario != null) {
            usuario.setEstado("rechazado");
            usuarioOVIDao.updateUsuario(usuario);
            redirectAttributes.addFlashAttribute("successMessage", "Usuario rechazado");
        }
        return "redirect:/tecnico/home";
    }

    @GetMapping("/asistentes")
    public String listAsistentes(@RequestParam(value = "estado", required = false) String estado,
                                 HttpSession session, Model model) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"tecnicoovi".equals(tipo)) {
            return "redirect:/login";
        }

        Object usuario = session.getAttribute("usuario");
        if (usuario instanceof TecnicoOVI) {
            model.addAttribute("tecnico", (TecnicoOVI) usuario);
        }

        List<AsistentePersonal> asistentes = asistentePersonalDao.getAsistentesByEstado(estado);
        model.addAttribute("asistentes", asistentes);
        model.addAttribute("estadoSeleccionado", estado);

        return "tecnico/listTecnicos";
    }

    @GetMapping("/peticiones")
    public String listPeticiones(@RequestParam(value = "estado", required = false) String estado,
                                 HttpSession session, Model model) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"tecnicoovi".equals(tipo)) {
            return "redirect:/login";
        }

        Object usuario = session.getAttribute("usuario");
        if (usuario instanceof TecnicoOVI) {
            model.addAttribute("tecnico", (TecnicoOVI) usuario);
        }

        List<PeticionAPR> peticiones = peticionAPRDao.getPeticionesByEstadoFiltrado(estado);
        model.addAttribute("peticiones", peticiones);
        model.addAttribute("estadoSeleccionado", estado);
        model.addAttribute("estadoLabels", Map.of(
                "en_revision", "En revision",
                "aprobada", "Aprobada",
                "rechazada", "Rechazada",
                "cancelada", "Cancelada",
                "cerrada_contrato", "Cerrada (contrato)",
                "cerrada_contrato_finalizado", "Finalizada"
        ));

        return "tecnico/listPeticiones";
    }

    @GetMapping("/asistente/{id}")
    public String verAsistente(@PathVariable int id, @RequestParam(required = false) Integer idSolicitud, HttpSession session, Model model) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"tecnicoovi".equals(tipo)) {
            return "redirect:/login";
        }

        AsistentePersonal asistente = asistentePersonalDao.getAsistente(id);
        if (asistente == null) {
            return "redirect:/tecnico/asistentes";
        }

        model.addAttribute("asistente", asistente);
        if (idSolicitud != null) {
            model.addAttribute("volverACandidatos", idSolicitud);
        }
        return "tecnico/infoAsistente";
    }

    @PostMapping("/asistente/approve/{id}")
    public String approveAsistente(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"tecnicoovi".equals(tipo)) {
            return "redirect:/login";
        }

        AsistentePersonal asistente = asistentePersonalDao.getAsistente(id);
        if (asistente != null) {
            asistente.setEstadoValidacion("aceptado");
            asistentePersonalDao.updateAsistente(asistente);
            redirectAttributes.addFlashAttribute("successMessage", "Asistente aprobado correctamente");
        }
        return "redirect:/tecnico/asistentes";
    }

    @PostMapping("/asistente/reject/{id}")
    public String rejectAsistente(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"tecnicoovi".equals(tipo)) {
            return "redirect:/login";
        }

        AsistentePersonal asistente = asistentePersonalDao.getAsistente(id);
        if (asistente != null) {
            asistente.setEstadoValidacion("rechazado");
            asistentePersonalDao.updateAsistente(asistente);
            redirectAttributes.addFlashAttribute("successMessage", "Asistente rechazado");
        }
        return "redirect:/tecnico/asistentes";
    }

    @GetMapping("/peticion/{id}")
    public String verPeticion(@PathVariable int id, HttpSession session, Model model) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"tecnicoovi".equals(tipo)) {
            return "redirect:/login";
        }

        PeticionAPR peticion = peticionAPRDao.getPeticionWithUser(id);
        if (peticion == null) {
            return "redirect:/tecnico/peticiones";
        }

        model.addAttribute("peticion", peticion);
        model.addAttribute("estadoLabels", Map.of(
                "en_revision", "En revision",
                "aprobada", "Aprobada",
                "rechazada", "Rechazada",
                "cancelada", "Cancelada",
                "cerrada_contrato", "Cerrada (contrato)",
                "cerrada_contrato_finalizado", "Finalizada"
        ));
        return "tecnico/infoPeticion";
    }

@PostMapping("/peticion/approve/{id}")
    public String approvePeticion(@PathVariable int id,
                                  @RequestParam(value = "observaciones", required = false) String observaciones,
                                  HttpSession session, RedirectAttributes redirectAttributes) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"tecnicoovi".equals(tipo)) {
            return "redirect:/login";
        }

        PeticionAPR peticion = peticionAPRDao.getPeticionWithUser(id);
        if (peticion != null) {
            peticion.setEstado("aprobada");
            peticion.setObservacionesTecnico(observaciones);
            peticion.setFechaRevision(java.time.LocalDate.now());
            peticionAPRDao.updatePeticion(peticion);
            redirectAttributes.addFlashAttribute("successMessage", "Solicitud aprobada correctamente");
        }
        return "redirect:/tecnico/peticiones";
    }

@PostMapping("/peticion/reject/{id}")
    public String rejectPeticion(@PathVariable int id,
                                  @RequestParam(value = "observaciones", required = false) String observaciones,
                                  HttpSession session, RedirectAttributes redirectAttributes) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"tecnicoovi".equals(tipo)) {
            return "redirect:/login";
        }

        PeticionAPR peticion = peticionAPRDao.getPeticionWithUser(id);
        if (peticion != null) {
            peticion.setEstado("rechazada");
            peticion.setMotivoRechazo(observaciones);
            peticion.setFechaRevision(java.time.LocalDate.now());
            peticionAPRDao.updatePeticion(peticion);
            redirectAttributes.addFlashAttribute("successMessage", "Solicitud rechazada");
        }
        return "redirect:/tecnico/peticiones";
    }

@GetMapping("/peticion/{id}/candidatos")
    public String verCandidatos(@PathVariable int id, HttpSession session, Model model) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"tecnicoovi".equals(tipo)) {
            return "redirect:/login";
        }

        PeticionAPR peticion = peticionAPRDao.getPeticionWithUser(id);
        if (peticion == null) {
            return "redirect:/tecnico/peticiones";
        }

        if (!"aprobada".equals(peticion.getEstado())) {
            return "redirect:/tecnico/peticion/" + id;
        }

        List<AsistentePersonal> todosAsistentes = asistentePersonalDao.getCandidatosCompatibles(peticion.getTipoAsistencia());
        List<MatchingService.CandidatoSugerido> candidatos = new java.util.ArrayList<>();

        for (AsistentePersonal a : todosAsistentes) {
            MatchingService.CandidatoSugerido cs = new MatchingService.CandidatoSugerido();
            cs.setAsistente(a);
            int puntuacion = calcularPuntuacionManual(peticion, a, cs);
            cs.setPuntuacion(puntuacion);
            candidatos.add(cs);
        }

        candidatos.sort((c1, c2) -> Integer.compare(c2.getPuntuacion(), c1.getPuntuacion()));

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

        return "tecnico/candidatosPeticion";
    }

private int calcularPuntuacionManual(PeticionAPR peticion, AsistentePersonal asistente, MatchingService.CandidatoSugerido cs) {
        int puntos = 0;

        if (peticion.getMunicipio() != null && !peticion.getMunicipio().isEmpty()
                && asistente.getMunicipio() != null && !asistente.getMunicipio().isEmpty()) {
            if (asistente.getMunicipio().equalsIgnoreCase(peticion.getMunicipio())) {
                puntos += 40;
            }
        }

        if (peticion.getPreferenciaGenero() != null && !peticion.getPreferenciaGenero().isEmpty()
                && asistente.getNombre() != null) {
            boolean hombre = !asistente.getNombre().contains(" ") && (
                    asistente.getNombre().toLowerCase().endsWith("o") || asistente.getNombre().toLowerCase().endsWith("os")
            );
            boolean mujer = asistente.getNombre().toLowerCase().contains("a") && !hombre;

            if (peticion.getPreferenciaGenero().equalsIgnoreCase("Hombre") && hombre) {
                puntos += 30;
            } else if (peticion.getPreferenciaGenero().equalsIgnoreCase("Mujer") && mujer) {
                puntos += 30;
            }
        }

        if (peticion.getIdiomasRequeridos() != null && !peticion.getIdiomasRequeridos().isEmpty()
                && asistente.getFormacionPrevia() != null && !asistente.getFormacionPrevia().isEmpty()) {
            if (asistente.getFormacionPrevia().toLowerCase().contains(peticion.getIdiomasRequeridos().toLowerCase())) {
                puntos += 20;
            }
        }

        if (peticion.getTipoTareas() != null && !peticion.getTipoTareas().isEmpty()
                && asistente.getFormacionPrevia() != null && !asistente.getFormacionPrevia().isEmpty()) {
            if (asistente.getFormacionPrevia().toLowerCase().contains(peticion.getTipoTareas().toLowerCase())) {
                puntos += 10;
            }
        }

        return puntos;
    }

@PostMapping("/peticion/{id}/candidatos/guardar")
    public String guardarCandidatos(@PathVariable int id,
                                     @RequestParam(value = "candidatosSeleccionados", required = false) List<Integer> idsAsistentes,
                                     HttpSession session, RedirectAttributes redirectAttributes) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"tecnicoovi".equals(tipo)) {
            return "redirect:/login";
        }

        PeticionAPR peticion = peticionAPRDao.getPeticionWithUser(id);
        if (peticion == null || !"aprobada".equals(peticion.getEstado())) {
            return "redirect:/tecnico/peticiones";
        }

        List<AsistentePersonal> todosAsistentes = asistentePersonalDao.getCandidatosCompatibles(peticion.getTipoAsistencia());
        List<Seleccion> aGuardar = new ArrayList<>();

        if (idsAsistentes != null) {
            for (AsistentePersonal a : todosAsistentes) {
                if (idsAsistentes.contains(a.getIdAsistente())) {
                    MatchingService.CandidatoSugerido cs = new MatchingService.CandidatoSugerido();
                    cs.setAsistente(a);
                    int puntuacion = calcularPuntuacionManual(peticion, a, cs);
                    Seleccion s = new Seleccion();
                    s.setIdSolicitud(id);
                    s.setIdAsistente(a.getIdAsistente());
                    s.setEstadoSeleccion("propuesta");
                    s.setPuntuacionMatch(puntuacion);
                    aGuardar.add(s);
                }
            }
        }

        if (!aGuardar.isEmpty()) {
            seleccionDao.guardarCandidatosSugeridos(id, aGuardar);
            redirectAttributes.addFlashAttribute("successMessage", "Propuesta guardada correctamente");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Debes seleccionar al menos un candidato");
        }

        return "redirect:/tecnico/peticion/" + id;
    }
}