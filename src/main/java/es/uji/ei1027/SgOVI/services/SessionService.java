package es.uji.ei1027.SgOVI.services;

import es.uji.ei1027.SgOVI.model.AsistentePersonal;
import es.uji.ei1027.SgOVI.model.Formador;
import es.uji.ei1027.SgOVI.model.TecnicoOVI;
import es.uji.ei1027.SgOVI.model.UsuarioOVI;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    public boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("usuario") != null;
    }

    public boolean isUsuarioOVI(HttpSession session) {
        Object tipo = session.getAttribute("tipo");
        return "usuarioOVI".equals(tipo);
    }

    public boolean isTecnicoOVI(HttpSession session) {
        Object tipo = session.getAttribute("tipo");
        return "tecnicoovi".equals(tipo);
    }

    public boolean isAsistente(HttpSession session) {
        Object tipo = session.getAttribute("tipo");
        return "asistente".equals(tipo);
    }

    public boolean isFormador(HttpSession session) {
        Object tipo = session.getAttribute("tipo");
        return "formador".equals(tipo);
    }

    public String requireLogin(HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        return null;
    }

    public String requireUsuarioOVI(HttpSession session) {
        if (!isUsuarioOVI(session)) {
            return "redirect:/login";
        }
        Object usuario = session.getAttribute("usuario");
        if (usuario instanceof UsuarioOVI) {
            UsuarioOVI uovi = (UsuarioOVI) usuario;
            if (!"aceptado".equals(uovi.getEstado())) {
                return "redirect:/login";
            }
        } else {
            return "redirect:/login";
        }
        return null;
    }

    public String requireTecnicoOVI(HttpSession session) {
        if (!isTecnicoOVI(session)) {
            return "redirect:/login";
        }
        return null;
    }

    public String requireAsistente(HttpSession session) {
        if (!isAsistente(session)) {
            return "redirect:/login";
        }
        return null;
    }

    public String requireFormador(HttpSession session) {
        if (!isFormador(session)) {
            return "redirect:/login";
        }
        return null;
    }

    public UsuarioOVI getUsuarioOVI(HttpSession session) {
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

    public TecnicoOVI getTecnicoOVI(HttpSession session) {
        Object tipo = session.getAttribute("tipo");
        Object usuario = session.getAttribute("usuario");
        if ("tecnicoovi".equals(tipo) && usuario instanceof TecnicoOVI) {
            return (TecnicoOVI) usuario;
        }
        return null;
    }

    public AsistentePersonal getAsistente(HttpSession session) {
        Object tipo = session.getAttribute("tipo");
        Object usuario = session.getAttribute("usuario");
        if ("asistente".equals(tipo) && usuario instanceof AsistentePersonal) {
            return (AsistentePersonal) usuario;
        }
        return null;
    }

    public Formador getFormador(HttpSession session) {
        Object tipo = session.getAttribute("tipo");
        Object usuario = session.getAttribute("usuario");
        if ("formador".equals(tipo) && usuario instanceof Formador) {
            return (Formador) usuario;
        }
        return null;
    }
}