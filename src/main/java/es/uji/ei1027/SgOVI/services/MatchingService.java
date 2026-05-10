package es.uji.ei1027.SgOVI.services;

import es.uji.ei1027.SgOVI.dao.AsistentePersonalDao;
import es.uji.ei1027.SgOVI.model.AsistentePersonal;
import es.uji.ei1027.SgOVI.model.PeticionAPR;
import es.uji.ei1027.SgOVI.model.Seleccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MatchingService {

    private final AsistentePersonalDao asistentePersonalDao;

    @Autowired
    public MatchingService(AsistentePersonalDao asistentePersonalDao) {
        this.asistentePersonalDao = asistentePersonalDao;
    }

    public List<CandidatoSugerido> calcularCandidatos(PeticionAPR peticion) {
        List<AsistentePersonal> candidatos = asistentePersonalDao.getCandidatosCompatibles(peticion.getTipoAsistencia());
        List<CandidatoSugerido> resultado = new ArrayList<>();

        for (AsistentePersonal a : candidatos) {
            CandidatoSugerido cs = new CandidatoSugerido();
            cs.setAsistente(a);
            cs.setPuntuacion(calcularPuntuacion(peticion, a, cs));
            cs.setIdSolicitud(peticion.getIdSolicitud());
            cs.setIdAsistente(a.getIdAsistente());
            cs.setEstadoSeleccion("propuesta");
            resultado.add(cs);
        }

        resultado.sort(Comparator.comparing(CandidatoSugerido::getPuntuacion).reversed());
        return resultado;
    }

    private int calcularPuntuacion(PeticionAPR peticion, AsistentePersonal asistente, CandidatoSugerido cs) {
        int puntos = 0;

        if (peticion.getMunicipio() != null && !peticion.getMunicipio().isEmpty()
                && asistente.getMunicipio() != null && !asistente.getMunicipio().isEmpty()) {
            if (asistente.getMunicipio().equalsIgnoreCase(peticion.getMunicipio())) {
                puntos += 40;
                cs.getEtiquetas().add("Municipio coincidido (+40)");
            }
        }

        if (peticion.getPreferenciaGenero() != null && !peticion.getPreferenciaGenero().isEmpty()
                && asistente.getNombre() != null) {
            boolean hombre = !asistente.getNombre().contains(" ") && (
                    asistente.getNombre().toLowerCase().endsWith("o") || asistente.getNombre().toLowerCase().endsWith("os")
            );
            boolean mujer = asistente.getNombre().toLowerCase().contains("a") && !hombre;

            if (peticion.getPreferenciaGenero().equalsIgnoreCase("Hombre") && hombre) {
                puntos += 30;
                cs.getEtiquetas().add("Genero preferido coincide (+30)");
            } else if (peticion.getPreferenciaGenero().equalsIgnoreCase("Mujer") && mujer) {
                puntos += 30;
                cs.getEtiquetas().add("Genero preferido coincide (+30)");
            }
        }

        if (peticion.getIdiomasRequeridos() != null && !peticion.getIdiomasRequeridos().isEmpty()
                && asistente.getFormacionPrevia() != null && !asistente.getFormacionPrevia().isEmpty()) {
            if (asistente.getFormacionPrevia().toLowerCase().contains(peticion.getIdiomasRequeridos().toLowerCase())) {
                puntos += 20;
                cs.getEtiquetas().add("Idioma requerido en formacion (+20)");
            }
        }

        if (peticion.getTipoTareas() != null && !peticion.getTipoTareas().isEmpty()
                && asistente.getFormacionPrevia() != null && !asistente.getFormacionPrevia().isEmpty()) {
            if (asistente.getFormacionPrevia().toLowerCase().contains(peticion.getTipoTareas().toLowerCase())) {
                puntos += 10;
                cs.getEtiquetas().add("Tipo de tareas en formacion (+10)");
            }
        }

        return puntos;
    }

    public static class CandidatoSugerido extends Seleccion {
        private AsistentePersonal asistente;
        private int puntuacion;
        private List<String> etiquetas = new ArrayList<>();

        public CandidatoSugerido() {}

        public AsistentePersonal getAsistente() {
            return asistente;
        }

        public void setAsistente(AsistentePersonal asistente) {
            this.asistente = asistente;
        }

        public int getPuntuacion() {
            return puntuacion;
        }

        public void setPuntuacion(int puntuacion) {
            this.puntuacion = puntuacion;
        }

        public List<String> getEtiquetas() {
            return etiquetas;
        }

        public void setPuntuacionMatch(Integer puntuacionMatch) {
            this.puntuacion = puntuacionMatch != null ? puntuacionMatch : 0;
        }

        public Integer getPuntuacionMatch() {
            return puntuacion;
        }
    }
}