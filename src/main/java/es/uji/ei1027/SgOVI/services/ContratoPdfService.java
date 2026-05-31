package es.uji.ei1027.SgOVI.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class ContratoPdfService {

    @Value("${app.upload.base-path}")
    private String uploadPath;

    public String guardarContrato(MultipartFile archivo, int idRegistro) {
        String contratosDir = uploadPath.replace("\\", "/") + "/contratos";
        File dir = new File(contratosDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String nombreArchivo = "contrato_" + idRegistro + ".pdf";
        File dest = new File(dir, nombreArchivo);
        try {
            archivo.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo: " + e.getMessage(), e);
        }

        return "contratos/" + nombreArchivo;
    }
}
