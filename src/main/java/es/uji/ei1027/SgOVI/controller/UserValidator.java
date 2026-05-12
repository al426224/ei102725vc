package es.uji.ei1027.SgOVI.controller;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import es.uji.ei1027.SgOVI.model.UserDetails;

class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return UserDetails.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        UserDetails user = (UserDetails) obj;
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            errors.rejectValue("username", "username.empty", "El email no puede estar vacío");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            errors.rejectValue("password", "password.empty", "La contraseña no puede estar vacía");
        }
    }
}
