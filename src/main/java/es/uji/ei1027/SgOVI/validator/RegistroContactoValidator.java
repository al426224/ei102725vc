package es.uji.ei1027.SgOVI.validator;

import es.uji.ei1027.SgOVI.model.RegistroContacto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

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
        } else if (registro.getTipoContrato().length() > 200) {
            errors.rejectValue("tipoContrato", "longitud", "El tipo de contrato no puede superar los 200 caracteres.");
        }

        if (registro.getFechaInicio() == null) {
            errors.rejectValue("fechaInicio", "obligatorio", "La fecha de inicio es obligatoria.");
        }

        if (registro.getFechaFin() == null) {
            errors.rejectValue("fechaFin", "obligatorio", "La fecha de fin es obligatoria.");
        } else if (registro.getFechaInicio() != null && registro.getFechaFin().isBefore(registro.getFechaInicio())) {
            errors.rejectValue("fechaFin", "formato", "La fecha de fin debe ser posterior a la fecha de inicio.");
        }

        if (registro.getPdfData() == null || registro.getPdfData().length == 0) {
            errors.rejectValue("pdfData", "obligatorio", "El archivo PDF del contrato es obligatorio.");
        }

        if (registro.getObservaciones() != null && registro.getObservaciones().length() > 500) {
            errors.rejectValue("observaciones", "longitud", "Las observaciones no pueden superar los 500 caracteres.");
        }
    }
}
