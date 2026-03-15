package es.uji.ei1027.SgOVI.model;

public class Seleccion {

    private int idSeleccion;
    private String idSolicitud;
    private String idAsistente;
    private String estadoSeleccion;
    private Integer puntuacionMatch;

    public Seleccion() {}

    public Seleccion(int idSeleccion, String idSolicitud, String idAsistente, 
                   String estadoSeleccion, Integer puntuacionMatch) {
        this.idSeleccion = idSeleccion;
        this.idSolicitud = idSolicitud;
        this.idAsistente = idAsistente;
        this.estadoSeleccion = estadoSeleccion;
        this.puntuacionMatch = puntuacionMatch;
    }

    public int getIdSeleccion() {
        return idSeleccion;
    }

    public void setIdSeleccion(int idSeleccion) {
        this.idSeleccion = idSeleccion;
    }

    public String getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(String idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public String getIdAsistente() {
        return idAsistente;
    }

    public void setIdAsistente(String idAsistente) {
        this.idAsistente = idAsistente;
    }

    public String getEstadoSeleccion() {
        return estadoSeleccion;
    }

    public void setEstadoSeleccion(String estadoSeleccion) {
        this.estadoSeleccion = estadoSeleccion;
    }

    public Integer getPuntuacionMatch() {
        return puntuacionMatch;
    }

    public void setPuntuacionMatch(Integer puntuacionMatch) {
        this.puntuacionMatch = puntuacionMatch;
    }

    @Override
    public String toString() {
        return "Seleccion{" +
                "idSeleccion=" + idSeleccion +
                ", idSolicitud='" + idSolicitud + '\'' +
                ", idAsistente='" + idAsistente + '\'' +
                ", estadoSeleccion='" + estadoSeleccion + '\'' +
                ", puntuacionMatch=" + puntuacionMatch +
                '}';
    }
}
