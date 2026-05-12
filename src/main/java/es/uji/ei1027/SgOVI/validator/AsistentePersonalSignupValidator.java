package es.uji.ei1027.SgOVI.validator;

import es.uji.ei1027.SgOVI.dao.AsistentePersonalDao;
import es.uji.ei1027.SgOVI.model.AsistentePersonal;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class AsistentePersonalSignupValidator implements Validator {
    private final AsistentePersonalDao asistentePersonalDao;

    public AsistentePersonalSignupValidator(AsistentePersonalDao asistentePersonalDao) {
        this.asistentePersonalDao = asistentePersonalDao;
    }

    @Override
    public boolean supports(Class<?> cls) {
        return AsistentePersonal.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        if (obj instanceof AsistentePersonal) {
            validateAsistentePersonal((AsistentePersonal) obj, errors);
        }
    }

    private void validateAsistentePersonal(AsistentePersonal asistente, Errors errors) {
        if (asistente.getNombre() == null || asistente.getNombre().trim().isEmpty()) {
            errors.rejectValue("nombre", "obligatorio", "El nombre es obligatorio.");
        } else if (asistente.getNombre().length() > 100) {
            errors.rejectValue("nombre", "longitud", "El nombre no puede superar los 100 caracteres.");
        }

        if (asistente.getEmail() == null || asistente.getEmail().trim().isEmpty()) {
            errors.rejectValue("email", "obligatorio", "El email es obligatorio.");
        } else if (asistente.getEmail().length() > 100) {
            errors.rejectValue("email", "longitud", "El email no puede superar los 100 caracteres.");
        } else if (asistentePersonalDao.getAsistenteByEmail(asistente.getEmail()) != null) {
            errors.rejectValue("email", "duplicado", "El email ya esta en uso.");
        }

        if (asistente.getContrasena() == null || asistente.getContrasena().trim().isEmpty()) {
            errors.rejectValue("contrasena", "obligatorio", "La contrasena es obligatoria.");
        } else if (asistente.getContrasena().length() < 8) {
            errors.rejectValue("contrasena", "longitud", "La contrasena debe tener al menos 8 caracteres.");
        } else if (asistente.getContrasena().length() > 255) {
            errors.rejectValue("contrasena", "longitud", "La contrasena no puede superar los 255 caracteres.");
        }

        if (asistente.getTipoAsistente() == null || asistente.getTipoAsistente().trim().isEmpty()) {
            errors.rejectValue("tipoAsistente", "obligatorio", "El tipo de asistente es obligatorio.");
        }

        if (asistente.getFormacionPrevia() == null || asistente.getFormacionPrevia().trim().isEmpty()) {
            errors.rejectValue("formacionPrevia", "obligatorio", "La formacion previa es obligatoria.");
        } else if (asistente.getFormacionPrevia().length() > 200) {
            errors.rejectValue("formacionPrevia", "longitud", "La formacion no puede superar los 200 caracteres.");
        }

        if (asistente.getDisponibilidad() == null || asistente.getDisponibilidad().trim().isEmpty()) {
            errors.rejectValue("disponibilidad", "obligatorio", "La disponibilidad es obligatoria.");
        } else if (asistente.getDisponibilidad().length() > 100) {
            errors.rejectValue("disponibilidad", "longitud", "La disponibilidad no puede superar los 100 caracteres.");
        }

        if (asistente.getMunicipio() == null || asistente.getMunicipio().trim().isEmpty()) {
            errors.rejectValue("municipio", "obligatorio", "El municipio es obligatorio.");
        } else if (asistente.getMunicipio().length() > 100) {
            errors.rejectValue("municipio", "longitud", "El municipio no puede superar los 100 caracteres.");
        }
    }
}
