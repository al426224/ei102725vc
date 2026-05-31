package es.uji.ei1027.SgOVI.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class PdfFileValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return MultipartFile.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        if (!(obj instanceof MultipartFile)) {
            return;
        }
        MultipartFile archivo = (MultipartFile) obj;

        if (archivo.isEmpty()) {
            errors.reject("archivoVacio", "El archivo esta vacio");
            return;
        }

        String filename = archivo.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".pdf")) {
            errors.reject("formatoInvalido", "Solo se permiten archivos PDF");
            return;
        }

        if (!"application/pdf".equals(archivo.getContentType())) {
            errors.reject("formatoInvalido", "El archivo no es un PDF valido");
            return;
        }

        try {
            byte[] header = new byte[4];
            archivo.getInputStream().read(header, 0, 4);
            if (header[0] != 0x25 || header[1] != 0x50 || header[2] != 0x44 || header[3] != 0x46) {
                errors.reject("formatoInvalido", "El archivo no es un PDF valido");
            }
        } catch (IOException e) {
            errors.reject("errorValidacion", "Error al validar el archivo: " + e.getMessage());
        }
    }
}
