package es.uji.ei1027.SgOVI.services;

import es.uji.ei1027.SgOVI.dao.UsuarioOVIDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class ProyectoVidaService {

    @Value("${app.upload.base-path}")
    private String uploadPath;

    private final UsuarioOVIDao usuarioOVIDao;

    @Autowired
    public ProyectoVidaService(UsuarioOVIDao usuarioOVIDao) {
        this.usuarioOVIDao = usuarioOVIDao;
    }

    public String guardarProyectoVida(MultipartFile archivo, int idUsuario) {
        String proyectoVidaDir = uploadPath.replace("\\", "/") + "/proyectosvida";
        File dir = new File(proyectoVidaDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String nombreArchivo = "usuario_" + idUsuario + ".pdf";
        File dest = new File(dir, nombreArchivo);
        try {
            archivo.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo: " + e.getMessage(), e);
        }

        String rutaPublica = "/uploads/proyectosvida/" + nombreArchivo;
        usuarioOVIDao.actualizarProyectoVida(idUsuario, rutaPublica);

        return rutaPublica;
    }
}
