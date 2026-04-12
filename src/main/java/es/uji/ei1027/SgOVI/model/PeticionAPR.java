package es.uji.ei1027.SgOVI.model;

import java.time.LocalDate;

public class PeticionAPR {

    private int idSolicitud;
    private int idUsuario;
    private String tipoAsistencia;
    private String descripcion;
    private int horasSemanales;
    private String estado;

    public PeticionAPR() {}

    public PeticionAPR(int idSolicitud, int idUsuario, String tipoAsistencia, 
                      String descripcion, int horasSemanales, String estado) {
        this.idSolicitud = idSolicitud;
        this.idUsuario = idUsuario;
        this.tipoAsistencia = tipoAsistencia;
        this.descripcion = descripcion;
        this.horasSemanales = horasSemanales;
        this.estado = estado;
    }

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipoAsistencia() {
        return tipoAsistencia;
    }

    public void setTipoAsistencia(String tipoAsistencia) {
        this.tipoAsistencia = tipoAsistencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getHorasSemanales() {
        return horasSemanales;
    }

    public void setHorasSemanales(int horasSemanales) {
        this.horasSemanales = horasSemanales;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "PeticionAPR{" +
                "idSolicitud='" + idSolicitud + '\'' +
                ", idUsuario='" + idUsuario + '\'' +
                ", tipoAsistencia='" + tipoAsistencia + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", horasSemanales=" + horasSemanales +
                ", estado='" + estado + '\'' +
                '}';
    }
}
