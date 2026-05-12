package es.uji.ei1027.SgOVI.validator;

import es.uji.ei1027.SgOVI.dao.UsuarioOVIDao;
import es.uji.ei1027.SgOVI.model.UsuarioOVI;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UsuarioOVIEditarPerfilValidator implements Validator {
    private final UsuarioOVIDao usuarioOVIDao;
    private final String idUsuarioActual;

    public UsuarioOVIEditarPerfilValidator(UsuarioOVIDao usuarioOVIDao, String idUsuarioActual) {
        this.usuarioOVIDao = usuarioOVIDao;
        this.idUsuarioActual = idUsuarioActual;
    }

    @Override
    public boolean supports(Class<?> cls) {
        return UsuarioOVI.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        if (obj instanceof UsuarioOVI) {
            validateUsuarioOVI((UsuarioOVI) obj, errors);
        }
    }

    private void validateUsuarioOVI(UsuarioOVI usuarioOVI, Errors errors) {

        if (usuarioOVI.getNombre() == null || usuarioOVI.getNombre().trim().isEmpty()) {
            errors.rejectValue("nombre", "obligatorio", "El nombre es obligatorio.");
        } else if (usuarioOVI.getNombre().length() > 100) {
            errors.rejectValue("nombre", "longitud", "El nombre no puede superar los 100 caracteres.");
        }

        if (usuarioOVI.getEmail() == null || usuarioOVI.getEmail().trim().isEmpty()) {
            errors.rejectValue("email", "obligatorio", "El correo electronico es obligatorio.");
        } else if (usuarioOVI.getEmail().length() > 100) {
            errors.rejectValue("email", "longitud", "El correo electronico no puede superar los 100 caracteres.");
        } else {
            UsuarioOVI existente = usuarioOVIDao.getUsuarioByEmail(usuarioOVI.getEmail());

            if (existente != null && !String.valueOf(existente.getIdUsuario()).equals(idUsuarioActual)) {
                errors.rejectValue("email", "duplicado", "El correo electronico ya esta en uso por otro usuario.");
            }
        }

        if (usuarioOVI.getTelefono() == null || usuarioOVI.getTelefono().trim().isEmpty()) {
            errors.rejectValue("telefono", "obligatorio", "El telefono es obligatorio.");
        } else if (usuarioOVI.getTelefono().length() > 20) {
            errors.rejectValue("telefono", "longitud", "El telefono no puede superar los 20 caracteres.");
        } else if (!usuarioOVI.getTelefono().matches("^[0-9]{9}$")) {
            errors.rejectValue("telefono", "formato", "El telefono debe tener exactamente 9 digitos.");
        }

        if (usuarioOVI.getProyectoVida() == null || usuarioOVI.getProyectoVida().trim().isEmpty()) {
            errors.rejectValue("proyectoVida", "obligatorio", "El proyecto de vida es obligatorio.");
        } else if (usuarioOVI.getProyectoVida().length() > 1000) {
            errors.rejectValue("proyectoVida", "longitud", "El proyecto de vida no puede superar los 1000 caracteres.");
        }
    }
}
