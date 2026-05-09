package es.uji.ei1027.SgOVI.validator;

import es.uji.ei1027.SgOVI.dao.TecnicoOVIDao;
import es.uji.ei1027.SgOVI.model.TecnicoOVI;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class TecnicoOVIValidator implements Validator {
    private final TecnicoOVIDao tecnicoOVIDao;
    private final String idTecnicoActual;

    public TecnicoOVIValidator(TecnicoOVIDao tecnicoOVIDao, String idTecnicoActual) {
        this.tecnicoOVIDao = tecnicoOVIDao;
        this.idTecnicoActual = idTecnicoActual;
    }

    @Override
    public boolean supports(Class<?> cls) {
        return TecnicoOVI.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        if (obj instanceof TecnicoOVI) {
            validateTecnicoOVI((TecnicoOVI) obj, errors);
        }
    }

    private void validateTecnicoOVI(TecnicoOVI tecnico, Errors errors) {

        if (tecnico.getNombre() == null || tecnico.getNombre().trim().isEmpty()) {
            errors.rejectValue("nombre", "obligatorio", "El nombre es obligatorio.");
        }

        if (tecnico.getEmail() == null || tecnico.getEmail().trim().isEmpty()) {
            errors.rejectValue("email", "obligatorio", "El correo electrónico es obligatorio.");
        }
        else {
            TecnicoOVI existente = tecnicoOVIDao.getTecnicoByEmail(tecnico.getEmail());
            if (existente != null && !String.valueOf(existente.getIdTecnico()).equals(idTecnicoActual)) {
                errors.rejectValue("email", "duplicado", "El correo electrónico ya está en uso por otro técnico.");
            }
        }
    }
}