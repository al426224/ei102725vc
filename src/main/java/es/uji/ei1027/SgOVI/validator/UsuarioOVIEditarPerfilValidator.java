package es.uji.ei1027.SgOVI.validator;

import es.uji.ei1027.SgOVI.dao.UsuarioOVIDao;
import es.uji.ei1027.SgOVI.model.UsuarioOVI;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UsuarioOVIEditarPerfilValidator implements Validator {
    private final UsuarioOVIDao usuarioOVIDao;
    private final String idUsuarioActual;

    public UsuarioOVIEditarPerfilValidator(UsuarioOVIDao usuarioOVIDao, String idUsuarioActual) {
        this.usuarioOVIDao = usuarioOVIDao;
        this.idUsuarioActual = idUsuarioActual;
    }

    @Override
    public boolean supports(Class<?> cls) {
        return UsuarioOVI.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        if (obj instanceof UsuarioOVI) {
            validateUsuarioOVI((UsuarioOVI) obj, errors);
        }
    }

    private void validateUsuarioOVI(UsuarioOVI usuarioOVI, Errors errors) {

        // 1. Validar nombre (Igual que en Signup)
        if (usuarioOVI.getNombre() == null || usuarioOVI.getNombre().trim().isEmpty()) {
            errors.rejectValue("nombre", "obligatorio", "El nombre es obligatorio.");
        }

        // 2. Validar email y duplicidad (Excluyendo al usuario actual)
        if (usuarioOVI.getEmail() == null || usuarioOVI.getEmail().trim().isEmpty()) {
            errors.rejectValue("email", "obligatorio", "El correo electrónico es obligatorio.");
        } else if (!usuarioOVI.getEmail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            errors.rejectValue("email", "formato", "El correo electrónico no tiene un formato válido.");
        } else {
            UsuarioOVI existente = usuarioOVIDao.getUsuarioByEmail(usuarioOVI.getEmail());
            // Conversión segura: comparamos el ID numérico del usuario encontrado
            // con el ID (String) que viene del controlador
            if (existente != null && !String.valueOf(existente.getIdUsuario()).equals(idUsuarioActual)) {
                errors.rejectValue("email", "duplicado", "El correo electrónico ya está en uso por otro usuario.");
            }
        }

        // 3. Validar teléfono (9 dígitos exactos)
        if (usuarioOVI.getTelefono() == null || usuarioOVI.getTelefono().trim().isEmpty()) {
            errors.rejectValue("telefono", "obligatorio", "El teléfono es obligatorio.");
        } else if (!usuarioOVI.getTelefono().matches("^[0-9]{9}$")) {
            errors.rejectValue("telefono", "formato", "El teléfono debe tener exactamente 9 dígitos.");
        }

        // 4. Validar proyecto de vida
        if (usuarioOVI.getProyectoVida() == null || usuarioOVI.getProyectoVida().trim().isEmpty()) {
            errors.rejectValue("proyectoVida", "obligatorio", "El proyecto de vida es obligatorio.");
        }
    }
}