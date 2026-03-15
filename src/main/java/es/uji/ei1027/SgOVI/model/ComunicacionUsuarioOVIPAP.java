package es.uji.ei1027.SgOVI.model;

import java.time.LocalDateTime;

public class ComunicacionUsuarioOVIPAP {

    private int idComu;
    private int idSeleccion;
    private String emisor;
    private String mensaje;
    private LocalDateTime hora;
    private String medio;

    public ComunicacionUsuarioOVIPAP() {}

    public ComunicacionUsuarioOVIPAP(int idComu, int idSeleccion, String emisor, 
                                    String mensaje, LocalDateTime hora, String medio) {
        this.idComu = idComu;
        this.idSeleccion = idSeleccion;
        this.emisor = emisor;
        this.mensaje = mensaje;
        this.hora = hora;
        this.medio = medio;
    }

    public int getIdComu() {
        return idComu;
    }

    public void setIdComu(int idComu) {
        this.idComu = idComu;
    }

    public int getIdSeleccion() {
        return idSeleccion;
    }

    public void setIdSeleccion(int idSeleccion) {
        this.idSeleccion = idSeleccion;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getHora() {
        return hora;
    }

    public void setHora(LocalDateTime hora) {
        this.hora = hora;
    }

    public String getMedio() {
        return medio;
    }

    public void setMedio(String medio) {
        this.medio = medio;
    }

    @Override
    public String toString() {
        return "ComunicacionUsuarioOVIPAP{" +
                "idComu=" + idComu +
                ", idSeleccion=" + idSeleccion +
                ", emisor='" + emisor + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", hora=" + hora +
                ", medio='" + medio + '\'' +
                '}';
    }
}
