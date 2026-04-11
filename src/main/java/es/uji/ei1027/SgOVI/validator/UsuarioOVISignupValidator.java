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

        // Valida nombre
        if (usuarioOVI.getNombre() == null || usuarioOVI.getNombre().trim().isEmpty()) {
            errors.rejectValue("nombre", "obligatorio", "El nombre es obligatorio.");
        }

        // Valida email
        if (usuarioOVI.getEmail() == null || usuarioOVI.getEmail().trim().isEmpty()) {
            errors.rejectValue("email", "obligatorio", "El email es obligatorio.");
        } else if (usuarioOVIDao.getUsuarioByEmail(usuarioOVI.getEmail()) != null) {
            errors.rejectValue("email", "duplicado", "El email ya está en uso.");
        }

        // Valida contraseña
        if (usuarioOVI.getContrasena() == null || usuarioOVI.getContrasena().trim().isEmpty()) {
            errors.rejectValue("contrasena", "obligatorio", "La contraseña es obligatoria.");
        } else if (usuarioOVI.getContrasena().length() < 8) {
            errors.rejectValue("contrasena", "longitud", "La contraseña debe tener al menos 8 caracteres.");
        }

        // Valida DNI
        if (usuarioOVI.getDni() == null || usuarioOVI.getDni().trim().isEmpty()) {
            errors.rejectValue("dni", "obligatorio", "El DNI es obligatorio.");
        } else if (usuarioOVIDao.getUsuarioByDni(usuarioOVI.getDni()) != null) {
            errors.rejectValue("dni", "duplicado", "El DNI ya está en uso.");
        }

        // Valida fecha de nacimiento
        if (usuarioOVI.getFechaNacimiento() == null) {
            errors.rejectValue("fechaNacimiento", "obligatorio", "La fecha de nacimiento es obligatoria.");
        }

        // Valida consentimiento LOPD
        if (!usuarioOVI.isConsentimientoLOPD()) {
            errors.rejectValue("consentimientoLOPD", "obligatorio", "Debe aceptar el tratamiento de datos.");
        }
    }

}
