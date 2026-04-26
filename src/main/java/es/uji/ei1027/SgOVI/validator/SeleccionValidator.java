package es.uji.ei1027.SgOVI.validator;

import es.uji.ei1027.SgOVI.model.Seleccion;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class SeleccionValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return Seleccion.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        if (obj instanceof Seleccion) {
            validateSeleccion((Seleccion) obj, errors);
        }
    }

    private void validateSeleccion(Seleccion seleccion, Errors errors) {
        if (seleccion.getIdSolicitud() <= 0) {
            errors.rejectValue("idSolicitud", "obligatorio", "La solicitud es obligatoria.");
        }

        if (seleccion.getIdAsistente() <= 0) {
            errors.rejectValue("idAsistente", "obligatorio", "El asistente es obligatorio.");
        }

        if (seleccion.getEstadoSeleccion() == null || seleccion.getEstadoSeleccion().trim().isEmpty()) {
            errors.rejectValue("estadoSeleccion", "obligatorio", "El estado de la selección es obligatorio.");
        }

        if (seleccion.getPuntuacionMatch() != null) {
            if (seleccion.getPuntuacionMatch() < 0 || seleccion.getPuntuacionMatch() > 100) {
                errors.rejectValue("puntuacionMatch", "rango", "La puntuación debe estar entre 0 y 100.");
            }
        }
    }
}