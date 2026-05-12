package es.uji.ei1027.SgOVI.validator;

import es.uji.ei1027.SgOVI.dao.UsuarioOVIDao;
import es.uji.ei1027.SgOVI.model.UsuarioOVI;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UsuarioOVISignupValidator implements Validator {
    private final UsuarioOVIDao usuarioOVIDao;

    public UsuarioOVISignupValidator(UsuarioOVIDao usuarioOVIDao) {
        this.usuarioOVIDao = usuarioOVIDao;
    }

    @Override
    public boolean supports(Class<?> cls){
        return UsuarioOVI.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object obj, Errors errors){
        if (obj instanceof UsuarioOVI){
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
            errors.rejectValue("email", "obligatorio", "El email es obligatorio.");
        } else if (usuarioOVI.getEmail().length() > 100) {
            errors.rejectValue("email", "longitud", "El email no puede superar los 100 caracteres.");
        } else if (usuarioOVIDao.getUsuarioByEmail(usuarioOVI.getEmail()) != null) {
            errors.rejectValue("email", "duplicado", "El email ya esta en uso.");
        }

        if (usuarioOVI.getContrasena() == null || usuarioOVI.getContrasena().trim().isEmpty()) {
            errors.rejectValue("contrasena", "obligatorio", "La contrasena es obligatoria.");
        } else if (usuarioOVI.getContrasena().length() < 8) {
            errors.rejectValue("contrasena", "longitud", "La contrasena debe tener al menos 8 caracteres.");
        } else if (usuarioOVI.getContrasena().length() > 255) {
            errors.rejectValue("contrasena", "longitud", "La contrasena no puede superar los 255 caracteres.");
        }

        if (usuarioOVI.getDni() == null || usuarioOVI.getDni().trim().isEmpty()) {
            errors.rejectValue("dni", "obligatorio", "El DNI es obligatorio.");
        } else if (usuarioOVI.getDni().length() > 9) {
            errors.rejectValue("dni", "longitud", "El DNI no puede superar los 9 caracteres.");
        } else if (usuarioOVIDao.getUsuarioByDni(usuarioOVI.getDni()) != null) {
            errors.rejectValue("dni", "duplicado", "El DNI ya esta en uso.");
        }

        if (usuarioOVI.getFechaNacimiento() == null) {
            errors.rejectValue("fechaNacimiento", "obligatorio", "La fecha de nacimiento es obligatoria.");
        }

        if (usuarioOVI.getTelefono() != null && usuarioOVI.getTelefono().length() > 20) {
            errors.rejectValue("telefono", "longitud", "El telefono no puede superar los 20 caracteres.");
        }

        if (usuarioOVI.getProyectoVida() != null && usuarioOVI.getProyectoVida().length() > 1000) {
            errors.rejectValue("proyectoVida", "longitud", "El proyecto de vida no puede superar los 1000 caracteres.");
        }

        if (!usuarioOVI.isConsentimientoLOPD()) {
            errors.rejectValue("consentimientoLOPD", "obligatorio", "Debe aceptar el tratamiento de datos.");
        }
    }
}
