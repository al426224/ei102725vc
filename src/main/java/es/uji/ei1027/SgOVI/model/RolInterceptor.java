package es.uji.ei1027.SgOVI.model;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class RolInterceptor implements HandlerInterceptor {

    private final Rol requiredRole;

    public RolInterceptor(Rol requiredRole) {
        this.requiredRole = requiredRole;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("/login");
            return false;
        }

        Object tipo = session.getAttribute("tipo");
        Object rolAttr = session.getAttribute("rol");

        if (rolAttr instanceof Rol) {
            Rol userRol = (Rol) rolAttr;
            if (userRol != requiredRole) {
                response.sendRedirect("/");
                return false;
            }
        } else {
            if (!matchesTipo(tipo, requiredRole)) {
                response.sendRedirect("/");
                return false;
            }
        }

        return true;
    }

    private boolean matchesTipo(Object tipo, Rol rol) {
        if (tipo == null) return false;
        String t = tipo.toString();
        switch (rol) {
            case USUARIOOVI: return "usuarioOVI".equals(t);
            case ASISTENTE: return "asistente".equals(t);
            case FORMADOR: return "formador".equals(t);
            case TECNICOOVI: return "tecnicoovi".equals(t);
            default: return false;
        }
    }
}