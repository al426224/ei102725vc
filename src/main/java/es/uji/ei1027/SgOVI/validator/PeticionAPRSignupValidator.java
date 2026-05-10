package es.uji.ei1027.SgOVI.validator;

import es.uji.ei1027.SgOVI.model.PeticionAPR;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PeticionAPRSignupValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return PeticionAPR.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        if (obj instanceof PeticionAPR) {
            validatePeticionAPR((PeticionAPR) obj, errors);
        }
    }

    private void validatePeticionAPR(PeticionAPR peticionAPR, Errors errors) {
        if (peticionAPR.getTipoAsistencia() == null || peticionAPR.getTipoAsistencia().trim().isEmpty()) {
            errors.rejectValue("tipoAsistencia", "obligatorio", "El tipo de asistencia es obligatorio.");
        }

        if (peticionAPR.getHorasSemanales() <= 0) {
            errors.rejectValue("horasSemanales", "obligatorio", "Las horas semanales son obligatorias.");
        }

        if (peticionAPR.getMunicipio() == null || peticionAPR.getMunicipio().trim().isEmpty()) {
            errors.rejectValue("municipio", "obligatorio", "El municipio es obligatorio.");
        }

        if (peticionAPR.getTipoTareas() == null || peticionAPR.getTipoTareas().trim().isEmpty()) {
            errors.rejectValue("tipoTareas", "obligatorio", "El tipo de tareas es obligatorio.");
        }

        if (peticionAPR.getFechaInicioPrevista() == null) {
            errors.rejectValue("fechaInicioPrevista", "obligatorio", "La fecha de inicio prevista es obligatoria.");
        }
    }
}