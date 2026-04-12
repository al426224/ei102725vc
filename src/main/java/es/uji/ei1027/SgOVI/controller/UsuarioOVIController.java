package es.uji.ei1027.SgOVI.controller;

import es.uji.ei1027.SgOVI.dao.UsuarioOVIDao;
import es.uji.ei1027.SgOVI.model.UsuarioOVI;
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
import java.util.logging.Logger;

@Controller
@RequestMapping("/usuarioOVI")
public class UsuarioOVIController {

    private final UsuarioOVIDao usuarioOVIDao;
    private final Logger logger = Logger.getLogger(UsuarioOVIController.class.getName());

    @Autowired
    public UsuarioOVIController(UsuarioOVIDao usuarioOVIDao) {
        this.usuarioOVIDao = usuarioOVIDao;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
    }

    private String getRedirectUrl(String redirectUrl, String defaultUrl) {
        return (redirectUrl != null) ? "redirect:" + redirectUrl : "redirect:" + defaultUrl;
    }

    @GetMapping("/list")
    public String listUsuarios(Model model) {
        List<UsuarioOVI> usuarios = usuarioOVIDao.getUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "usuarioOVI/list";
    }

    @GetMapping("/search")
    public String searchForm(Model model) {
        return "usuarioOVI/search";
    }

    @PostMapping("/search")
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

    @GetMapping("/update/{id}")
    public String editUsuario(Model model, @PathVariable int id) {
        model.addAttribute("usuario", usuarioOVIDao.getUsuario(id));
        return "usuarioOVI/update";
    }

    @PostMapping("/update")
    public String processUpdateSubmit(@ModelAttribute("usuarioOVI") @Validated UsuarioOVI usuario, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "usuarioOVI/update";
        }
        
        usuarioOVIDao.updateUsuario(usuario);
        redirectAttributes.addFlashAttribute("successMessage", "Usuario actualizado correctamente");
        return "redirect:/usuarioOVI/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteUsuario(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        usuarioOVIDao.deleteUsuario(id);
        redirectAttributes.addFlashAttribute("successMessage", "Usuario eliminado correctamente");
        return "redirect:/usuarioOVI/list";
    }

    @GetMapping("/perfil/{id}")
    public String perfilUsuario(Model model, @PathVariable int id) {
        // Obtenemos el usuario usando el ID numérico
        UsuarioOVI usuario = usuarioOVIDao.getUsuario(id);

        if (usuario == null) {
            return "redirect:/usuarioOVI/list";
        }

        // Es vital pasar el objeto "usuario" para que el HTML pueda leer sus datos
        model.addAttribute("usuario", usuario);
        return "usuarioOVI/perfilUsuarioOVI";
    }
}