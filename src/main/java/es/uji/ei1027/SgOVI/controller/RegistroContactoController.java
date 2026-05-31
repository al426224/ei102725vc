package es.uji.ei1027.SgOVI.controller;

import es.uji.ei1027.SgOVI.dao.RegistroContactoDao;
import es.uji.ei1027.SgOVI.model.RegistroContacto;
import es.uji.ei1027.SgOVI.services.ContratoPdfService;
import es.uji.ei1027.SgOVI.validator.PdfFileValidator;
import es.uji.ei1027.SgOVI.validator.RegistroContactoValidator;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/registroContacto")
public class RegistroContactoController {

    private final RegistroContactoDao registroContactoDao;
    private final ContratoPdfService contratoPdfService;
    private final Logger logger = Logger.getLogger(RegistroContactoController.class.getName());

    @Autowired
    public RegistroContactoController(RegistroContactoDao registroContactoDao, ContratoPdfService contratoPdfService) {
        this.registroContactoDao = registroContactoDao;
        this.contratoPdfService = contratoPdfService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
    }

    @RequestMapping(value = "/list")
    public String listRegistros(Model model) {
        List<RegistroContacto> registros = registroContactoDao.getRegistros();
        model.addAttribute("registros", registros);
        return "registroContacto/list";
    }

    @RequestMapping(value = "/add")
    public String addRegistroForm(Model model) {
        model.addAttribute("registroContacto", new RegistroContacto());
        return "registroContacto/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addRegistro(@ModelAttribute("registroContacto") @Validated RegistroContacto registro,
                                BindingResult bindingResult, Model model,
                                RedirectAttributes redirectAttributes) {
        RegistroContactoValidator validator = new RegistroContactoValidator();
        validator.validate(registro, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("registroContacto", registro);
            return "registroContacto/add";
        }

        if (registro.getFechaInicio() == null) {
            registro.setFechaInicio(LocalDate.now());
        }
        if (registro.getResultado() == null || registro.getResultado().trim().isEmpty()) {
            registro.setResultado("pendiente");
        }

        registroContactoDao.addRegistro(registro);
        redirectAttributes.addFlashAttribute("successMessage", "Registro de contacto creado correctamente");
        return "redirect:/registroContacto/list";
    }

    @RequestMapping(value = "/update/{id}")
    public String updateRegistroForm(@PathVariable int id, HttpSession session, Model model) {
        model.addAttribute("registroContacto", registroContactoDao.getRegistro(id));

        Object tipo = session.getAttribute("tipo");
        Object usuario = session.getAttribute("usuario");
        if (tipo == null || usuario == null) {
            return "redirect:/login";
        }

        if ("asistente".equals(tipo)) {
            model.addAttribute("asistente", usuario);
        } else {
            model.addAttribute("usuario", usuario);
        }
        return "registroContacto/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateRegistro(@ModelAttribute("registroContacto") @Validated RegistroContacto registro,
                                   BindingResult bindingResult, Model model,
                                   @RequestParam("archivo") MultipartFile archivo,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        RegistroContacto existing = registroContactoDao.getRegistro(registro.getIdReg());

        if (archivo != null && !archivo.isEmpty()) {
            PdfFileValidator pdfValidator = new PdfFileValidator();
            BeanPropertyBindingResult fileErrors = new BeanPropertyBindingResult(archivo, "archivo");
            pdfValidator.validate(archivo, fileErrors);
            if (fileErrors.hasErrors()) {
                String errorMsg = fileErrors.getGlobalError() != null ?
                                  fileErrors.getGlobalError().getDefaultMessage() :
                                  "El archivo no es valido";
                redirectAttributes.addFlashAttribute("errorMessage", errorMsg);
                return "redirect:/registroContacto/update/" + registro.getIdReg();
            }
            try {
                String ruta = contratoPdfService.guardarContrato(archivo, registro.getIdReg());
                registro.setRutaPdf(ruta);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error al procesar el archivo: " + e.getMessage());
                return "redirect:/registroContacto/update/" + registro.getIdReg();
            }
        } else if (existing != null) {
            registro.setRutaPdf(existing.getRutaPdf());
        }

        if (existing != null) {
            if (registro.getFechaInicio() == null) {
                registro.setFechaInicio(existing.getFechaInicio());
            }
            if (registro.getFechaFin() == null) {
                registro.setFechaFin(existing.getFechaFin());
            }
        }

        RegistroContactoValidator validator = new RegistroContactoValidator();
        validator.validate(registro, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("registroContacto", registro);
            return "registroContacto/update";
        }

        registroContactoDao.updateRegistro(registro);
        redirectAttributes.addFlashAttribute("successMessage", "Contrato actualizado correctamente");

        Object tipo = session.getAttribute("tipo");
        if ("asistente".equals(tipo)) {
            return "redirect:/asistentePersonal/contratos";
        }
        return "redirect:/usuarioOVI/contratos";
    }

    @RequestMapping(value = "/delete/{id}")
    public String deleteRegistro(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes) {
        registroContactoDao.deleteRegistro(id);
        redirectAttributes.addFlashAttribute("successMessage", "Registro eliminado correctamente");

        Object tipo = session.getAttribute("tipo");
        if ("asistente".equals(tipo)) {
            return "redirect:/asistentePersonal/contratos";
        }
        return "redirect:/usuarioOVI/contratos";
    }

    @RequestMapping(value = "/bySeleccion/{idSeleccion}")
    public String getRegistrosBySeleccion(Model model, @PathVariable int idSeleccion) {
        List<RegistroContacto> registros = registroContactoDao.getRegistrosBySeleccion(idSeleccion);
        model.addAttribute("registros", registros);
        return "registroContacto/list";
    }

    @RequestMapping(value = "/byResultado/{resultado}")
    public String getRegistrosByResultado(Model model, @PathVariable String resultado) {
        List<RegistroContacto> registros = registroContactoDao.getRegistrosByResultado(resultado);
        model.addAttribute("registros", registros);
        return "registroContacto/list";
    }

    @RequestMapping(value = "/finalize/{id}", method = RequestMethod.POST)
    public String finalizeRegistro(@PathVariable int id, RedirectAttributes redirectAttributes) {
        RegistroContacto registro = registroContactoDao.getRegistro(id);
        if (registro != null) {
            registro.setResultado("Finalizado");
            registro.setFechaFin(LocalDate.now());
            registroContactoDao.updateRegistro(registro);
            redirectAttributes.addFlashAttribute("successMessage", "Registro finalizado correctamente");
        }
        return "redirect:/registroContacto/list";
    }

    @RequestMapping(value = "/cancel/{id}", method = RequestMethod.POST)
    public String cancelRegistro(@PathVariable int id, RedirectAttributes redirectAttributes) {
        RegistroContacto registro = registroContactoDao.getRegistro(id);
        if (registro != null) {
            registro.setResultado("cancelado");
            registro.setFechaFin(LocalDate.now());
            registroContactoDao.updateRegistro(registro);
            redirectAttributes.addFlashAttribute("successMessage", "Registro cancelado correctamente");
        }
        return "redirect:/registroContacto/list";
    }

}