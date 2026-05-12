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
            errors.rejectValue("idSeleccion", "obligatorio", "La seleccion es obligatoria.");
        }

        if (registro.getTipoContrato() == null || registro.getTipoContrato().trim().isEmpty()) {
            errors.rejectValue("tipoContrato", "obligatorio", "El tipo de contrato es obligatorio.");
        } else if (registro.getTipoContrato().length() > 20) {
            errors.rejectValue("tipoContrato", "longitud", "El tipo de contrato no puede superar los 20 caracteres.");
        }

        if (registro.getFechaInicio() == null) {
            errors.rejectValue("fechaInicio", "obligatoria", "La fecha de inicio es obligatoria.");
        }

        if (registro.getObservaciones() == null || registro.getObservaciones().trim().isEmpty()) {
            errors.rejectValue("observaciones", "obligatorio", "Las observaciones son obligatorias.");
        } else if (registro.getObservaciones().length() > 200) {
            errors.rejectValue("observaciones", "longitud", "Las observaciones no pueden superar los 200 caracteres.");
        }

        if (registro.getResultado() == null || registro.getResultado().trim().isEmpty()) {
            errors.rejectValue("resultado", "obligatorio", "El resultado es obligatorio.");
        } else if (registro.getResultado().length() > 50) {
            errors.rejectValue("resultado", "longitud", "El resultado no puede superar los 50 caracteres.");
        }
    }
}
