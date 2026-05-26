package es.uji.ei1027.SgOVI.services;

import es.uji.ei1027.SgOVI.model.AsistentePersonal;
import es.uji.ei1027.SgOVI.model.UsuarioOVI;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class EdadCompatibilidadService {

    public boolean esMayorDeEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            return false;
        }
        return Period.between(fechaNacimiento, LocalDate.now()).getYears() >= 18;
    }

    public boolean sonCompatiblesPorEdad(LocalDate fechaNacimientoUsuario, LocalDate fechaNacimientoAsistente) {
        if (fechaNacimientoUsuario == null || fechaNacimientoAsistente == null) {
            return false;
        }
        return esMayorDeEdad(fechaNacimientoUsuario) == esMayorDeEdad(fechaNacimientoAsistente);
    }

    public boolean sonCompatiblesPorEdad(UsuarioOVI usuario, AsistentePersonal asistente) {
        if (usuario == null || asistente == null) {
            return false;
        }
        return sonCompatiblesPorEdad(usuario.getFechaNacimiento(), asistente.getFechaNacimiento());
    }
}
