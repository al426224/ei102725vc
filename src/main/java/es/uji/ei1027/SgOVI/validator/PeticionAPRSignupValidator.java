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
        if (peticionAPR.getIdUsuario() <= 0) {
            errors.rejectValue("idUsuario", "obligatorio", "El usuario es obligatorio.");
        }

        if (peticionAPR.getTipoAsistencia() == null || peticionAPR.getTipoAsistencia().trim().isEmpty()) {
            errors.rejectValue("tipoAsistencia", "obligatorio", "El tipo de asistencia es obligatorio.");
        }

        if (peticionAPR.getDescripcion() == null || peticionAPR.getDescripcion().trim().isEmpty()) {
            errors.rejectValue("descripcion", "obligatoria", "La descripción es obligatoria.");
        }

        if (peticionAPR.getHorasSemanales() <= 0) {
            errors.rejectValue("horasSemanales", "obligatorio", "Las horas semanales son obligatorias.");
        }

        if (peticionAPR.getEstado() == null || peticionAPR.getEstado().trim().isEmpty()) {
            errors.rejectValue("estado", "obligatorio", "El estado es obligatorio.");
        }
    }
}