package es.uji.ei1027.SgOVI.validator;

import es.uji.ei1027.SgOVI.model.RegistroContacto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class RegistroContactoValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return RegistroContacto.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        if (obj instanceof RegistroContacto) {
            validateRegistroContacto((RegistroContacto) obj, errors);
        }
    }

    private void validateRegistroContacto(RegistroContacto registro, Errors errors) {
        if (registro.getIdSeleccion() <= 0) {
            errors.rejectValue("idSeleccion", "obligatorio", "La selección es obligatoria.");
        }

        if (registro.getTipoContrato() == null || registro.getTipoContrato().trim().isEmpty()) {
            errors.rejectValue("tipoContrato", "obligatorio", "El tipo de contrato es obligatorio.");
        }

        if (registro.getFechaInicio() == null) {
            errors.rejectValue("fechaInicio", "obligatoria", "La fecha de inicio es obligatoria.");
        }

        if (registro.getObservaciones() == null || registro.getObservaciones().trim().isEmpty()) {
            errors.rejectValue("observaciones", "obligatorio", "Las observaciones son obligatorias.");
        }

        if (registro.getResultado() == null || registro.getResultado().trim().isEmpty()) {
            errors.rejectValue("resultado", "obligatorio", "El resultado es obligatorio.");
        }
    }
}