package es.uji.ei1027.SgOVI.controller;

import es.uji.ei1027.SgOVI.dao.AsistentePersonalDao;
import es.uji.ei1027.SgOVI.dao.PeticionAPRDao;
import es.uji.ei1027.SgOVI.dao.RegistroContactoDao;
import es.uji.ei1027.SgOVI.dao.SeleccionDao;
import es.uji.ei1027.SgOVI.dao.UsuarioOVIDao;
import es.uji.ei1027.SgOVI.model.AsistentePersonal;
import es.uji.ei1027.SgOVI.model.PeticionAPR;
import es.uji.ei1027.SgOVI.model.RegistroContacto;
import es.uji.ei1027.SgOVI.model.Seleccion;
import es.uji.ei1027.SgOVI.model.UsuarioOVI;
import es.uji.ei1027.SgOVI.validator.UsuarioOVIEditarPerfilValidator;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/usuarioOVI")
public class UsuarioOVIController {

    private final UsuarioOVIDao usuarioOVIDao;
    private final PeticionAPRDao peticionAPRDao;
    private final RegistroContactoDao registroContactoDao;
    private final SeleccionDao seleccionDao;
    private final AsistentePersonalDao asistentePersonalDao;
    private final Logger logger = Logger.getLogger(UsuarioOVIController.class.getName());

    @Autowired
    public UsuarioOVIController(UsuarioOVIDao usuarioOVIDao, PeticionAPRDao peticionAPRDao,
                                RegistroContactoDao registroContactoDao,
                                SeleccionDao seleccionDao,
                                AsistentePersonalDao asistentePersonalDao) {
        this.usuarioOVIDao = usuarioOVIDao;
        this.peticionAPRDao = peticionAPRDao;
        this.registroContactoDao = registroContactoDao;
        this.seleccionDao = seleccionDao;
        this.asistentePersonalDao = asistentePersonalDao;
    }

    public static class ContratoInfo {
        private final RegistroContacto contrato;
        private final String nombreAsistente;
        private final String emailAsistente;

        public ContratoInfo(RegistroContacto contrato, String nombreAsistente, String emailAsistente) {
            this.contrato = contrato;
            this.nombreAsistente = nombreAsistente;
            this.emailAsistente = emailAsistente;
        }

        public RegistroContacto getContrato() { return contrato; }
        public String getNombreAsistente() { return nombreAsistente; }
        public String getEmailAsistente() { return emailAsistente; }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
    }

    private String getRedirectUrl(String redirectUrl, String defaultUrl) {
        return (redirectUrl != null) ? "redirect:" + redirectUrl : "redirect:" + defaultUrl;
    }

    @RequestMapping(value = "/list")
    public String listUsuarios(Model model) {
        List<UsuarioOVI> usuarios = usuarioOVIDao.getUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "usuarioOVI/list";
    }

    @RequestMapping(value = "/search")
    public String searchForm(Model model) {
        return "usuarioOVI/search";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String processSearch(@RequestParam String tipo, @RequestParam String valor, Model model, RedirectAttributes redirectAttributes) {
        UsuarioOVI usuario = null;
        
        if (tipo.equals("id")) {
            usuario = usuarioOVIDao.getUsuario(Integer.parseInt(valor));
        } else if (tipo.equals("email")) {
            usuario = usuarioOVIDao.getUsuarioByEmail(valor);
        } else if (tipo.equals("dni")) {
            usuario = usuarioOVIDao.getUsuarioByDni(valor);
        }
        
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/usuarioOVI/search";
        }
        
        model.addAttribute("usuario", usuario);
        return "usuarioOVI/search";
    }

    @RequestMapping(value = "/update/{id}")
    public String editUsuario(Model model, @PathVariable int id) {
        model.addAttribute("usuario", usuarioOVIDao.getUsuario(id));
        return "usuarioOVI/update";
    }

@RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("usuario") @Validated UsuarioOVI usuario, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        UsuarioOVIEditarPerfilValidator validator = new UsuarioOVIEditarPerfilValidator(usuarioOVIDao, String.valueOf(usuario.getIdUsuario()));
        validator.validate(usuario, bindingResult);

        if (bindingResult.hasErrors()) {
            return "usuarioOVI/update";
        }
        
        UsuarioOVI original = usuarioOVIDao.getUsuario(usuario.getIdUsuario());
        if (original != null) {
            usuario.setDni(original.getDni());
            usuario.setFechaNacimiento(original.getFechaNacimiento());
            usuario.setEstado(original.getEstado());
            usuario.setContrasena(original.getContrasena());
            usuario.setFechaRegistro(original.getFechaRegistro());
            usuario.setConsentimientoLOPD(original.isConsentimientoLOPD());
        }
        
        usuarioOVIDao.updateUsuario(usuario);
        redirectAttributes.addFlashAttribute("successMessage", "Usuario actualizado correctamente");
        return "redirect:/usuarioOVI/perfil/" + usuario.getIdUsuario();
    }

    @RequestMapping(value = "/delete/{id}")
    public String deleteUsuario(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        usuarioOVIDao.deleteUsuario(id);
        redirectAttributes.addFlashAttribute("successMessage", "Usuario eliminado correctamente");
        return "redirect:/usuarioOVI/list";
    }

    @RequestMapping(value = "/perfil/{id}")
    public String perfilUsuario(Model model, @PathVariable int id) {
        UsuarioOVI usuario = usuarioOVIDao.getUsuario(id);

        if (usuario == null) {
            return "redirect:/usuarioOVI/list";
        }

        model.addAttribute("usuario", usuario);
        return "usuarioOVI/perfilUsuarioOVI";
    }

    @RequestMapping(value = "/homeUsuarioOVI")
    public String homeUsuarioOVI(HttpSession session, Model model) {
        UsuarioOVI usuario = (UsuarioOVI) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        List<PeticionAPR> peticionesAprobadas = peticionAPRDao.getPeticionesByUsuarioAndEstado(usuario.getIdUsuario(), "aprobada");
        List<PeticionAPR> peticionesEnRevision = peticionAPRDao.getPeticionesByUsuarioAndEstado(usuario.getIdUsuario(), "en_revision");

        model.addAttribute("usuario", usuario);
        model.addAttribute("solicitudesAprobadas", peticionesAprobadas.size());
        model.addAttribute("solicitudesEnRevision", peticionesEnRevision.size());
        model.addAttribute("asistentesAsignados", 0);
        return "usuarioOVI/home";
    }

    @RequestMapping(value = "/mensajes")
    public String mensajes(HttpSession session, Model model) {
        UsuarioOVI usuario = (UsuarioOVI) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("tipo", "usuarioOVI");
        return "mensajes/mensajes";
    }

    @RequestMapping(value = "/contratos")
    public String contratos(HttpSession session, Model model) {
        UsuarioOVI usuario = (UsuarioOVI) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        List<RegistroContacto> contratos = registroContactoDao.getRegistrosByUsuarioOVI(usuario.getIdUsuario());

        List<Integer> idsSeleccion = contratos.stream()
                .map(RegistroContacto::getIdSeleccion).distinct().collect(Collectors.toList());
        List<Seleccion> selecciones = idsSeleccion.isEmpty()
                ? new ArrayList<>() : seleccionDao.getSelecciones(idsSeleccion);
        Map<Integer, Seleccion> seleccionMap = selecciones.stream()
                .collect(Collectors.toMap(Seleccion::getIdSeleccion, s -> s));

        List<Integer> idsAsistente = selecciones.stream()
                .map(Seleccion::getIdAsistente).distinct().collect(Collectors.toList());
        List<AsistentePersonal> asistentes = idsAsistente.isEmpty()
                ? new ArrayList<>() : asistentePersonalDao.getAsistentes(idsAsistente);
        Map<Integer, AsistentePersonal> asistenteMap = asistentes.stream()
                .collect(Collectors.toMap(AsistentePersonal::getIdAsistente, a -> a));

        List<ContratoInfo> contratosActivos = new ArrayList<>();
        List<ContratoInfo> contratosFinalizados = new ArrayList<>();
        for (RegistroContacto c : contratos) {
            String nombreAsistente = "";
            String emailAsistente = "";
            Seleccion s = seleccionMap.get(c.getIdSeleccion());
            if (s != null) {
                AsistentePersonal a = asistenteMap.get(s.getIdAsistente());
                if (a != null) {
                    nombreAsistente = a.getNombre();
                    emailAsistente = a.getEmail() != null ? a.getEmail() : "";
                }
            }
            ContratoInfo info = new ContratoInfo(c, nombreAsistente, emailAsistente);
            if ("finalizado".equals(c.getResultado()) || "cancelado".equals(c.getResultado())) {
                contratosFinalizados.add(info);
            } else {
                contratosActivos.add(info);
            }
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("contratosActivos", contratosActivos);
        model.addAttribute("contratosFinalizados", contratosFinalizados);
        return "usuarioOVI/contratos";
    }
}