package es.uji.ei1027.SgOVI.validator;

import es.uji.ei1027.SgOVI.dao.FormadorDao;
import es.uji.ei1027.SgOVI.model.Formador;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class FormadorEditarValidator implements Validator {
    private final FormadorDao formadorDao;
    private final String idFormadorActual;

    public FormadorEditarValidator(FormadorDao formadorDao, String idFormadorActual) {
        this.formadorDao = formadorDao;
        this.idFormadorActual = idFormadorActual;
    }

    @Override
    public boolean supports(Class<?> cls) {
        return Formador.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        if (obj instanceof Formador) {
            validateFormador((Formador) obj, errors);
        }
    }

    private void validateFormador(Formador formador, Errors errors) {

        if (formador.getNombre() == null || formador.getNombre().trim().isEmpty()) {
            errors.rejectValue("nombre", "obligatorio", "El nombre es obligatorio.");
        }

        if (formador.getEmail() == null || formador.getEmail().trim().isEmpty()) {
            errors.rejectValue("email", "obligatorio", "El correo electrónico es obligatorio.");
        } else {
            Formador existente = formadorDao.getFormadorByEmail(formador.getEmail());
            if (existente != null && !String.valueOf(existente.getIdFormador()).equals(idFormadorActual)) {
                errors.rejectValue("email", "duplicado", "El correo electrónico ya está en uso por otro formador.");
            }
        }

        if (formador.getTelefono() != null && !formador.getTelefono().trim().isEmpty()
                && !formador.getTelefono().matches("^[0-9]{9}$")) {
            errors.rejectValue("telefono", "formato", "El teléfono debe tener exactamente 9 dígitos.");
        }

        if (formador.getEspecialidad() == null || formador.getEspecialidad().trim().isEmpty()) {
            errors.rejectValue("especialidad", "obligatorio", "La especialidad es obligatoria.");
        }
    }
}