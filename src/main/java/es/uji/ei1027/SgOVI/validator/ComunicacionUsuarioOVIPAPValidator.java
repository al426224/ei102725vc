package es.uji.ei1027.SgOVI.validator;

import es.uji.ei1027.SgOVI.model.ComunicacionUsuarioOVIPAP;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ComunicacionUsuarioOVIPAPValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return ComunicacionUsuarioOVIPAP.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        if (obj instanceof ComunicacionUsuarioOVIPAP) {
            validateComunicacion((ComunicacionUsuarioOVIPAP) obj, errors);
        }
    }

    private void validateComunicacion(ComunicacionUsuarioOVIPAP comunicacion, Errors errors) {
        if (comunicacion.getIdSeleccion() <= 0) {
            errors.rejectValue("idSeleccion", "obligatorio", "La seleccion es obligatoria.");
        }

        if (comunicacion.getEmisor() == null || comunicacion.getEmisor().trim().isEmpty()) {
            errors.rejectValue("emisor", "obligatorio", "El emisor es obligatorio.");
        } else if (comunicacion.getEmisor().length() > 20) {
            errors.rejectValue("emisor", "longitud", "El emisor no puede superar los 20 caracteres.");
        }

        if (comunicacion.getMensaje() == null || comunicacion.getMensaje().trim().isEmpty()) {
            errors.rejectValue("mensaje", "obligatorio", "El mensaje es obligatorio.");
        } else if (comunicacion.getMensaje().length() > 200) {
            errors.rejectValue("mensaje", "longitud", "El mensaje no puede superar los 200 caracteres.");
        }
    }
}
