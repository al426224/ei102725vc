package es.uji.ei1027.SgOVI.controller;

import es.uji.ei1027.SgOVI.dao.AsistentePersonalDao;
import es.uji.ei1027.SgOVI.dao.PeticionAPRDao;
import es.uji.ei1027.SgOVI.dao.RegistroContactoDao;
import es.uji.ei1027.SgOVI.dao.SeleccionDao;
import es.uji.ei1027.SgOVI.dao.TecnicoOVIDao;
import es.uji.ei1027.SgOVI.dao.UsuarioOVIDao;
import es.uji.ei1027.SgOVI.model.AsistentePersonal;
import es.uji.ei1027.SgOVI.model.PeticionAPR;
import es.uji.ei1027.SgOVI.model.RegistroContacto;
import es.uji.ei1027.SgOVI.model.TecnicoOVI;
import es.uji.ei1027.SgOVI.model.Seleccion;
import es.uji.ei1027.SgOVI.model.UsuarioOVI;
import es.uji.ei1027.SgOVI.services.EdadCompatibilidadService;
import es.uji.ei1027.SgOVI.services.EmailService;
import es.uji.ei1027.SgOVI.services.MatchingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    private final EdadCompatibilidadService edadCompatibilidadService;
    private final EmailService emailService;
    private final RegistroContactoDao registroContactoDao;

    @Autowired
    public TecnicoController(TecnicoOVIDao tecnicoOVIDao, UsuarioOVIDao usuarioOVIDao,
                               AsistentePersonalDao asistentePersonalDao, PeticionAPRDao peticionAPRDao,
                               SeleccionDao seleccionDao, MatchingService matchingService,
                               EdadCompatibilidadService edadCompatibilidadService,
                               EmailService emailService,
                               RegistroContactoDao registroContactoDao) {
        this.tecnicoOVIDao = tecnicoOVIDao;
        this.usuarioOVIDao = usuarioOVIDao;
        this.asistentePersonalDao = asistentePersonalDao;
        this.peticionAPRDao = peticionAPRDao;
        this.seleccionDao = seleccionDao;
        this.matchingService = matchingService;
        this.edadCompatibilidadService = edadCompatibilidadService;
        this.emailService = emailService;
        this.registroContactoDao = registroContactoDao;
    }

    @RequestMapping(value = "/home")
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

    @RequestMapping(value = "/usuario/{id}")
    public String verUsuario(@PathVariable int id, HttpSession session, Model model) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"tecnicoovi".equals(tipo)) {
            return "redirect:/login";
        }

        UsuarioOVI usuario = usuarioOVIDao.getUsuario(id);
        if (usuario == null) {
            return "redirect:/tecnico/home";
        }

        List<RegistroContacto> contratos = registroContactoDao.getRegistrosByUsuarioOVI(id);
        List<RegistroContacto> contratosActivos = new ArrayList<>();
        List<RegistroContacto> contratosFinalizados = new ArrayList<>();
        for (RegistroContacto c : contratos) {
            if ("finalizado".equals(c.getResultado()) || "cancelado".equals(c.getResultado())) {
                contratosFinalizados.add(c);
            } else {
                contratosActivos.add(c);
            }
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("contratosActivos", contratosActivos);
        model.addAttribute("contratosFinalizados", contratosFinalizados);
        return "tecnico/infoUsuarioOVI";
    }

    @RequestMapping(value = "/usuario/approve/{id}", method = RequestMethod.POST)
    public String approveUsuario(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"tecnicoovi".equals(tipo)) {
            return "redirect:/login";
        }

        UsuarioOVI usuario = usuarioOVIDao.getUsuario(id);
        if (usuario != null) {
            usuario.setEstado("aceptado");
            usuarioOVIDao.updateUsuario(usuario);
            emailService.sendEmail(usuario.getEmail(), "Solicitud aprobada",
                    "Hola " + usuario.getNombre() + ",\n\nSu solicitud ha sido aprobada.\n\nUn cordial saludo,\nEl equipo de SgOVI");
            redirectAttributes.addFlashAttribute("successMessage", "Usuario aprobado correctamente");
        }
        return "redirect:/tecnico/home";
    }


    @RequestMapping(value = "/usuario/reject/{id}", method = RequestMethod.POST)
    public String rejectUsuario(@PathVariable int id,
                                @RequestParam(value = "observaciones", required = false) String observaciones,
                                HttpSession session, RedirectAttributes redirectAttributes) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"tecnicoovi".equals(tipo)) {
            return "redirect:/login";
        }

        UsuarioOVI usuario = usuarioOVIDao.getUsuario(id);
        if (usuario != null) {
            usuario.setEstado("rechazado");
            usuario.setMotivoRechazo(observaciones);
            usuario.setFechaRevision(java.time.LocalDate.now());
            usuarioOVIDao.updateUsuario(usuario);
            String motivo = (observaciones != null && !observaciones.isEmpty()) ? "\nMotivo: " + observaciones : "";
            emailService.sendEmail(usuario.getEmail(), "Solicitud rechazada",
                    "Hola " + usuario.getNombre() + ",\n\nSu solicitud ha sido rechazada." + motivo + "\n\nUn cordial saludo,\nEl equipo de SgOVI");
            redirectAttributes.addFlashAttribute("successMessage", "Usuario rechazado");
        }
        return "redirect:/tecnico/home";
    }

    @RequestMapping(value = "/asistentes")
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

    @RequestMapping(value = "/peticiones")
    public String listPeticiones(@RequestParam(value = "estado", required = false) String estado,
                                 @RequestParam(value = "nombre", required = false) String nombre,
                                 HttpSession session, Model model) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"tecnicoovi".equals(tipo)) {
            return "redirect:/login";
        }

        Object usuario = session.getAttribute("usuario");
        if (usuario instanceof TecnicoOVI) {
            model.addAttribute("tecnico", (TecnicoOVI) usuario);
        }

        List<PeticionAPR> peticiones = peticionAPRDao.getPeticionesByEstadoFiltrado(estado, nombre);
        model.addAttribute("peticiones", peticiones);
        model.addAttribute("estadoSeleccionado", estado);
        model.addAttribute("nombreSeleccionado", nombre);
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

    @RequestMapping(value = "/asistente/{id}")
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

    @RequestMapping(value = "/asistente/approve/{id}", method = RequestMethod.POST)
    public String approveAsistente(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"tecnicoovi".equals(tipo)) {
            return "redirect:/login";
        }

        AsistentePersonal asistente = asistentePersonalDao.getAsistente(id);
        if (asistente != null) {
            asistente.setEstadoValidacion("aceptado");
            asistentePersonalDao.updateAsistente(asistente);
            emailService.sendEmail(asistente.getEmail(), "Validacion aprobada",
                    "Hola " + asistente.getNombre() + ",\n\nSu validacion como asistente personal ha sido aprobada.\n\nUn cordial saludo,\nEl equipo de SgOVI");
            redirectAttributes.addFlashAttribute("successMessage", "Asistente aprobado correctamente");
        }
        return "redirect:/tecnico/asistentes";
    }


    @RequestMapping(value = "/asistente/reject/{id}", method = RequestMethod.POST)
    public String rejectAsistente(@PathVariable int id,
                                  @RequestParam(value = "observaciones", required = false) String observaciones,
                                  HttpSession session, RedirectAttributes redirectAttributes) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"tecnicoovi".equals(tipo)) {
            return "redirect:/login";
        }

        AsistentePersonal asistente = asistentePersonalDao.getAsistente(id);
        if (asistente != null) {
            asistente.setEstadoValidacion("rechazado");
            asistente.setMotivoRechazo(observaciones);
            asistente.setFechaRevision(java.time.LocalDate.now());
            asistentePersonalDao.updateAsistente(asistente);
            String motivo = (observaciones != null && !observaciones.isEmpty()) ? "\nMotivo: " + observaciones : "";
            emailService.sendEmail(asistente.getEmail(), "Validacion rechazada",
                    "Hola " + asistente.getNombre() + ",\n\nSu validacion como asistente personal ha sido rechazada." + motivo + "\n\nUn cordial saludo,\nEl equipo de SgOVI");
            redirectAttributes.addFlashAttribute("successMessage", "Asistente rechazado");
        }
        return "redirect:/tecnico/asistentes";
    }

    @RequestMapping(value = "/peticion/{id}")
    public String verPeticion(@PathVariable int id, HttpSession session, Model model) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"tecnicoovi".equals(tipo)) {
            return "redirect:/login";
        }

        PeticionAPR peticion = peticionAPRDao.getPeticionWithUser(id);
        if (peticion == null) {
            return "redirect:/tecnico/peticiones";
        }

        Seleccion aceptada = seleccionDao.getSeleccionAceptadaPorSolicitud(id);
        AsistentePersonal asistenteElegido = null;
        RegistroContacto contrato = null;
        if (aceptada != null) {
            asistenteElegido = asistentePersonalDao.getAsistente(aceptada.getIdAsistente());
            List<RegistroContacto> registros = registroContactoDao.getRegistrosBySeleccion(aceptada.getIdSeleccion());
            if (!registros.isEmpty()) {
                contrato = registros.get(0);
            }
        }
        List<Seleccion> propuestas = seleccionDao.getSeleccionesBySolicitudAndEstado(id, "propuesta");

        model.addAttribute("peticion", peticion);
        model.addAttribute("asistenteElegido", asistenteElegido);
        model.addAttribute("contrato", contrato);
        model.addAttribute("tieneCandidatos", !propuestas.isEmpty());
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

@RequestMapping(value = "/peticion/approve/{id}", method = RequestMethod.POST)
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
            UsuarioOVI usuarioPeticion = usuarioOVIDao.getUsuario(peticion.getIdUsuario());
            String emailTo = (usuarioPeticion != null) ? usuarioPeticion.getEmail() : null;
            if (emailTo != null) {
                emailService.sendEmail(emailTo, "Peticion APR aprobada",
                        "Hola " + peticion.getNombreUsuario() + ",\n\nSu peticion APR ha sido aprobada.\n\nUn cordial saludo,\nEl equipo de SgOVI");
            }
            redirectAttributes.addFlashAttribute("successMessage", "Solicitud aprobada correctamente");
        }
        return "redirect:/tecnico/peticiones";
    }


@RequestMapping(value = "/peticion/reject/{id}", method = RequestMethod.POST)
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
            UsuarioOVI usuarioPeticion = usuarioOVIDao.getUsuario(peticion.getIdUsuario());
            String emailTo = (usuarioPeticion != null) ? usuarioPeticion.getEmail() : null;
            if (emailTo != null) {
                String motivo = (observaciones != null && !observaciones.isEmpty()) ? "\nMotivo: " + observaciones : "";
                emailService.sendEmail(emailTo, "Peticion APR rechazada",
                        "Hola " + peticion.getNombreUsuario() + ",\n\nSu peticion APR ha sido rechazada." + motivo + "\n\nUn cordial saludo,\nEl equipo de SgOVI");
            }
            redirectAttributes.addFlashAttribute("successMessage", "Solicitud rechazada");
        }
        return "redirect:/tecnico/peticiones";
    }


@RequestMapping(value = "/peticion/{id}/candidatos")
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

        UsuarioOVI usuarioPeticion = usuarioOVIDao.getUsuario(peticion.getIdUsuario());
        List<AsistentePersonal> todosAsistentes = asistentePersonalDao.getCandidatosCompatibles();
        List<MatchingService.CandidatoSugerido> candidatos = new java.util.ArrayList<>();

        for (AsistentePersonal a : todosAsistentes) {
            if (!edadCompatibilidadService.sonCompatiblesPorEdad(usuarioPeticion, a)) {
                continue;
            }
            MatchingService.CandidatoSugerido cs = new MatchingService.CandidatoSugerido();
            cs.setAsistente(a);
            int puntuacion = matchingService.calcularPuntuacion(peticion, a, cs);
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

        List<Seleccion> yaSeleccionados = seleccionDao.getSeleccionesBySolicitudAndEstado(id, "propuesta");
        List<Integer> idsYaSeleccionados = yaSeleccionados.stream()
                .map(Seleccion::getIdAsistente)
                .toList();
        model.addAttribute("idsYaSeleccionados", idsYaSeleccionados);

        return "tecnico/candidatosPeticion";
    }



@RequestMapping(value = "/peticion/{id}/candidatos/guardar", method = RequestMethod.POST)
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

        UsuarioOVI usuarioPeticion = usuarioOVIDao.getUsuario(peticion.getIdUsuario());
        List<AsistentePersonal> todosAsistentes = asistentePersonalDao.getCandidatosCompatibles();
        List<Seleccion> aGuardar = new ArrayList<>();

        if (idsAsistentes != null) {
            for (AsistentePersonal a : todosAsistentes) {
                if (idsAsistentes.contains(a.getIdAsistente())) {
                    if (!edadCompatibilidadService.sonCompatiblesPorEdad(usuarioPeticion, a)) {
                        continue;
                    }
                    MatchingService.CandidatoSugerido cs = new MatchingService.CandidatoSugerido();
                    cs.setAsistente(a);
                    int puntuacion = matchingService.calcularPuntuacion(peticion, a, cs);
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
