package es.uji.ei1027.SgOVI.controller;

import es.uji.ei1027.SgOVI.dao.AsistentePersonalDao;
import es.uji.ei1027.SgOVI.dao.PeticionAPRDao;
import es.uji.ei1027.SgOVI.dao.RegistroContactoDao;
import es.uji.ei1027.SgOVI.dao.SeleccionDao;
import es.uji.ei1027.SgOVI.dao.UsuarioOVIDao;
import es.uji.ei1027.SgOVI.services.ContratoPdfService;
import es.uji.ei1027.SgOVI.services.EdadCompatibilidadService;
import es.uji.ei1027.SgOVI.model.AsistentePersonal;
import es.uji.ei1027.SgOVI.model.PeticionAPR;
import es.uji.ei1027.SgOVI.model.RegistroContacto;
import es.uji.ei1027.SgOVI.model.Seleccion;
import es.uji.ei1027.SgOVI.model.UsuarioOVI;
import es.uji.ei1027.SgOVI.validator.PdfFileValidator;
import es.uji.ei1027.SgOVI.validator.PeticionAPRSignupValidator;
import es.uji.ei1027.SgOVI.validator.RegistroContactoValidator;

import java.time.LocalDate;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.validation.BeanPropertyBindingResult;

@Controller
@RequestMapping("/peticionAP")
public class PeticionAPController {

    private final PeticionAPRDao peticionAPRDao;
    private final SeleccionDao seleccionDao;
    private final AsistentePersonalDao asistentePersonalDao;
    private final EdadCompatibilidadService edadCompatibilidadService;
    private final RegistroContactoDao registroContactoDao;
    private final UsuarioOVIDao usuarioOVIDao;
    private final ContratoPdfService contratoPdfService;
    private final PeticionAPRSignupValidator validator = new PeticionAPRSignupValidator();
    private final RegistroContactoValidator registroContactoValidator = new RegistroContactoValidator();

    @Autowired
    public PeticionAPController(PeticionAPRDao peticionAPRDao, SeleccionDao seleccionDao,
                                AsistentePersonalDao asistentePersonalDao,
                                EdadCompatibilidadService edadCompatibilidadService,
                                RegistroContactoDao registroContactoDao,
                                UsuarioOVIDao usuarioOVIDao,
                                ContratoPdfService contratoPdfService) {
        this.peticionAPRDao = peticionAPRDao;
        this.seleccionDao = seleccionDao;
        this.asistentePersonalDao = asistentePersonalDao;
        this.edadCompatibilidadService = edadCompatibilidadService;
        this.registroContactoDao = registroContactoDao;
        this.usuarioOVIDao = usuarioOVIDao;
        this.contratoPdfService = contratoPdfService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value = "/mis-solicitudes")
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

    @RequestMapping(value = "/cancelar/{id}", method = RequestMethod.POST)
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

    @RequestMapping(value = "/nueva")
    public String nuevaSolicitudForm(HttpSession session, Model model) {
        UsuarioOVI usuario = getUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        model.addAttribute("peticionAP", new PeticionAPR());
        model.addAttribute("usuario", usuario);
        return "peticionAP/nueva-solicitud";
    }

    @RequestMapping(value = "/nueva", method = RequestMethod.POST)
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

    @RequestMapping(value = "/detalle/{id}")
    public String detalleSolicitud(@PathVariable int id, HttpSession session, Model model) {
        UsuarioOVI usuario = getUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }

        PeticionAPR peticion = peticionAPRDao.getPeticion(id);
        if (peticion == null || peticion.getIdUsuario() != usuario.getIdUsuario()) {
            return "redirect:/peticionAP/mis-solicitudes";
        }

        Seleccion aceptada = seleccionDao.getSeleccionAceptadaPorSolicitud(id);
        AsistentePersonal asistenteElegido = null;
        if (aceptada != null) {
            asistenteElegido = asistentePersonalDao.getAsistente(aceptada.getIdAsistente());
        }

        List<Seleccion> propuestas = seleccionDao.getSeleccionesBySolicitudAndEstado(id, "propuesta");

        List<RegistroContacto> registros = registroContactoDao.getRegistrosBySeleccion(
                aceptada != null ? aceptada.getIdSeleccion() : 0);
        RegistroContacto contrato = (registros != null && !registros.isEmpty()) ? registros.get(0) : null;

        model.addAttribute("peticionAP", peticion);
        model.addAttribute("usuario", usuario);
        model.addAttribute("asistenteElegido", asistenteElegido);
        model.addAttribute("idSeleccionChat", aceptada != null ? aceptada.getIdSeleccion() : null);
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
        return "peticionAP/detalle";
    }

    @RequestMapping(value = "/candidatos/{idSolicitud}")
    public String verCandidatos(@PathVariable int idSolicitud, HttpSession session, Model model) {
        UsuarioOVI usuario = getUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }

        PeticionAPR peticion = peticionAPRDao.getPeticionWithUser(idSolicitud);
        if (peticion == null || peticion.getIdUsuario() != usuario.getIdUsuario()) {
            return "redirect:/peticionAP/mis-solicitudes";
        }

        if (!"aprobada".equals(peticion.getEstado())) {
            return "redirect:/peticionAP/detalle/" + idSolicitud;
        }

        if (seleccionDao.getSeleccionAceptadaPorSolicitud(idSolicitud) != null) {
            return "redirect:/peticionAP/detalle/" + idSolicitud;
        }

        List<Seleccion> propuestas = seleccionDao.getSeleccionesBySolicitudAndEstado(idSolicitud, "propuesta");
        List<CandidatoOVI> candidatosData = new ArrayList<>();
        for (Seleccion s : propuestas) {
            AsistentePersonal a = asistentePersonalDao.getAsistente(s.getIdAsistente());
            if (a != null && edadCompatibilidadService.sonCompatiblesPorEdad(usuario, a)) {
                CandidatoOVI c = new CandidatoOVI();
                c.setSeleccion(s);
                c.setAsistente(a);
                candidatosData.add(c);
            }
        }

        model.addAttribute("peticion", peticion);
        model.addAttribute("candidatos", candidatosData);
        model.addAttribute("usuario", usuario);
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

    @RequestMapping(value = "/candidatos/{id}/elegir", method = RequestMethod.POST)
    public String elegirCandidato(@PathVariable int id,
                                  @RequestParam int asistenteSeleccionado,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        UsuarioOVI usuario = getUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }

        PeticionAPR peticion = peticionAPRDao.getPeticionWithUser(id);
        if (peticion == null || peticion.getIdUsuario() != usuario.getIdUsuario()) {
            return "redirect:/peticionAP/mis-solicitudes";
        }

        if (!"aprobada".equals(peticion.getEstado())) {
            return "redirect:/peticionAP/detalle/" + id;
        }

        if (seleccionDao.getSeleccionAceptadaPorSolicitud(id) != null) {
            return "redirect:/peticionAP/candidatos/" + id + "/contrato";
        }

        Seleccion propuestaExistente = null;
        for (Seleccion s : seleccionDao.getSeleccionesBySolicitudAndEstado(id, "propuesta")) {
            if (s.getIdAsistente() == asistenteSeleccionado) {
                propuestaExistente = s;
                break;
            }
        }

        if (propuestaExistente == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Asistente no encontrado entre los candidatos");
            return "redirect:/peticionAP/candidatos/" + id;
        }

        AsistentePersonal asistente = asistentePersonalDao.getAsistente(asistenteSeleccionado);
        if (asistente == null || !edadCompatibilidadService.sonCompatiblesPorEdad(usuario, asistente)) {
            redirectAttributes.addFlashAttribute("errorMessage", "El asistente seleccionado no es compatible por edad");
            return "redirect:/peticionAP/candidatos/" + id;
        }

        propuestaExistente.setEstadoSeleccion("aceptada");
        seleccionDao.updateSeleccion(propuestaExistente);

        return "redirect:/peticionAP/candidatos/" + id + "/contrato";
    }

    @RequestMapping(value = "/candidatos/{id}/contrato", method = RequestMethod.GET)
    public String mostrarFormularioContrato(@PathVariable int id, HttpSession session, Model model) {
        UsuarioOVI usuario = getUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }

        PeticionAPR peticion = peticionAPRDao.getPeticionWithUser(id);
        if (peticion == null || peticion.getIdUsuario() != usuario.getIdUsuario()) {
            return "redirect:/peticionAP/mis-solicitudes";
        }

        Seleccion aceptada = seleccionDao.getSeleccionAceptadaPorSolicitud(id);
        if (aceptada == null) {
            return "redirect:/peticionAP/candidatos/" + id;
        }

        List<RegistroContacto> existentes = registroContactoDao.getRegistrosBySeleccion(aceptada.getIdSeleccion());
        if (!existentes.isEmpty()) {
            return "redirect:/peticionAP/detalle/" + id;
        }

        AsistentePersonal asistente = asistentePersonalDao.getAsistente(aceptada.getIdAsistente());

        RegistroContacto registroContacto = new RegistroContacto();
        registroContacto.setIdSeleccion(aceptada.getIdSeleccion());
        model.addAttribute("peticion", peticion);
        model.addAttribute("asistente", asistente);
        model.addAttribute("usuario", usuario);
        model.addAttribute("registroContacto", registroContacto);
        model.addAttribute("idSeleccion", aceptada.getIdSeleccion());
        return "peticionAP/contrato-form";
    }

    @RequestMapping(value = "/candidatos/{id}/contrato", method = RequestMethod.POST)
    public String crearContrato(@PathVariable int id,
                                @ModelAttribute("registroContacto") RegistroContacto registroContacto,
                                BindingResult bindingResult,
                                @RequestParam("archivo") MultipartFile archivo,
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        UsuarioOVI usuario = getUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }

        PeticionAPR peticion = peticionAPRDao.getPeticionWithUser(id);
        if (peticion == null || peticion.getIdUsuario() != usuario.getIdUsuario()) {
            return "redirect:/peticionAP/mis-solicitudes";
        }

        if (!"aprobada".equals(peticion.getEstado())) {
            return "redirect:/peticionAP/detalle/" + id;
        }

        Seleccion aceptada = seleccionDao.getSeleccionAceptadaPorSolicitud(id);
        if (aceptada == null || aceptada.getIdSeleccion() != registroContacto.getIdSeleccion()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: seleccion no valida");
            return "redirect:/peticionAP/candidatos/" + id;
        }

        List<RegistroContacto> existentes = registroContactoDao.getRegistrosBySeleccion(registroContacto.getIdSeleccion());
        if (!existentes.isEmpty()) {
            return "redirect:/peticionAP/detalle/" + id;
        }

        registroContacto.setResultado("En curso");

        registroContactoValidator.validate(registroContacto, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("registroContacto", registroContacto);
            model.addAttribute("peticion", peticion);
            model.addAttribute("asistente", asistentePersonalDao.getAsistente(aceptada.getIdAsistente()));
            model.addAttribute("usuario", usuario);
            model.addAttribute("idSeleccion", registroContacto.getIdSeleccion());
            return "peticionAP/contrato-form";
        }

        if (archivo != null && !archivo.isEmpty()) {
            PdfFileValidator pdfValidator = new PdfFileValidator();
            BeanPropertyBindingResult fileErrors = new BeanPropertyBindingResult(archivo, "archivo");
            pdfValidator.validate(archivo, fileErrors);
            if (fileErrors.hasErrors()) {
                String errorMsg = fileErrors.getGlobalError() != null ?
                                  fileErrors.getGlobalError().getDefaultMessage() :
                                  "El archivo no es valido";
                redirectAttributes.addFlashAttribute("errorMessage", errorMsg);
                return "redirect:/peticionAP/candidatos/" + id + "/contrato";
            }
        }

        try {
            int idReg = registroContactoDao.addRegistro(registroContacto);

            if (archivo != null && !archivo.isEmpty()) {
                String ruta = contratoPdfService.guardarContrato(archivo, idReg);
                registroContacto.setRutaPdf(ruta);
                registroContactoDao.updateRegistro(registroContacto);
            }

            peticion.setEstado("cerrada_contrato");
            peticionAPRDao.updatePeticion(peticion);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Contrato creado correctamente con " + asistentePersonalDao.getAsistente(aceptada.getIdAsistente()).getNombre());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error al crear el contrato: " + e.getMessage());
            return "redirect:/peticionAP/candidatos/" + id + "/contrato";
        }
        return "redirect:/peticionAP/detalle/" + id;
    }

    @RequestMapping(value = "/candidato/{idAsistente}/detalle")
    public String verDetalleCandidato(@PathVariable int idAsistente,
                                       @RequestParam int idSolicitud,
                                       HttpSession session,
                                       Model model) {
        UsuarioOVI usuario = getUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }

        if (seleccionDao.getSeleccionAceptadaPorSolicitud(idSolicitud) != null) {
            return "redirect:/peticionAP/detalle/" + idSolicitud;
        }

        PeticionAPR peticion = peticionAPRDao.getPeticionWithUser(idSolicitud);
        if (peticion == null || peticion.getIdUsuario() != usuario.getIdUsuario()) {
            return "redirect:/peticionAP/mis-solicitudes";
        }

        AsistentePersonal asistente = asistentePersonalDao.getAsistente(idAsistente);
        if (asistente == null) {
            return "redirect:/peticionAP/candidatos/" + idSolicitud;
        }
        if (!edadCompatibilidadService.sonCompatiblesPorEdad(usuario, asistente)) {
            return "redirect:/peticionAP/candidatos/" + idSolicitud;
        }

        model.addAttribute("asistente", asistente);
        model.addAttribute("peticion", peticion);
        model.addAttribute("usuario", usuario);
        return "peticionAP/detalleAsistente";
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
