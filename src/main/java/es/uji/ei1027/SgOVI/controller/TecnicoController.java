package es.uji.ei1027.SgOVI.controller;

import es.uji.ei1027.SgOVI.dao.AsistentePersonalDao;
import es.uji.ei1027.SgOVI.dao.TecnicoOVIDao;
import es.uji.ei1027.SgOVI.dao.UsuarioOVIDao;
import es.uji.ei1027.SgOVI.model.AsistentePersonal;
import es.uji.ei1027.SgOVI.model.TecnicoOVI;
import es.uji.ei1027.SgOVI.model.UsuarioOVI;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/tecnico")
public class TecnicoController {

    private final TecnicoOVIDao tecnicoOVIDao;
    private final UsuarioOVIDao usuarioOVIDao;
    private final AsistentePersonalDao asistentePersonalDao;

    @Autowired
    public TecnicoController(TecnicoOVIDao tecnicoOVIDao, UsuarioOVIDao usuarioOVIDao, AsistentePersonalDao asistentePersonalDao) {
        this.tecnicoOVIDao = tecnicoOVIDao;
        this.usuarioOVIDao = usuarioOVIDao;
        this.asistentePersonalDao = asistentePersonalDao;
    }

    @GetMapping("/home")
    public String homeTecnico(HttpSession session, Model model) {
        Object usuario = session.getAttribute("usuario");
        Object tipo = session.getAttribute("tipo");

        if (usuario == null || !"tecnicoovi".equals(tipo)) {
            return "redirect:/login";
        }

        if (usuario instanceof TecnicoOVI) {
            model.addAttribute("tecnico", (TecnicoOVI) usuario);
        }

        List<UsuarioOVI> usuariosPendientes = usuarioOVIDao.getUsuariosByEstado("pendiente");
        model.addAttribute("usuariosPendientes", usuariosPendientes);

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
            usuario.setEstado("activo");
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
    public String listAsistentes(HttpSession session, Model model) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"tecnicoovi".equals(tipo)) {
            return "redirect:/login";
        }

        Object usuario = session.getAttribute("usuario");
        if (usuario instanceof TecnicoOVI) {
            model.addAttribute("tecnico", (TecnicoOVI) usuario);
        }

        List<AsistentePersonal> asistentes = asistentePersonalDao.getAsistentes();
        model.addAttribute("asistentes", asistentes);
        return "tecnico/listTecnicos";
    }

    @GetMapping("/asistente/{id}")
    public String verAsistente(@PathVariable int id, HttpSession session, Model model) {
        Object tipo = session.getAttribute("tipo");
        if (tipo == null || !"tecnicoovi".equals(tipo)) {
            return "redirect:/login";
        }

        AsistentePersonal asistente = asistentePersonalDao.getAsistente(id);
        if (asistente == null) {
            return "redirect:/tecnico/asistentes";
        }

        model.addAttribute("asistente", asistente);
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
            asistente.setEstadoValidacion("validado");
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
}