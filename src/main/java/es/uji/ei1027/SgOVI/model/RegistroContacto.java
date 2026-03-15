package es.uji.ei1027.SgOVI.model;

import java.time.LocalDate;

public class RegistroContacto {

    private int idReg;
    private int idSeleccion;
    private String tipoContacto;
    private LocalDate fechaContacto;
    private String observaciones;
    private String resultado;

    public RegistroContacto() {}

    public RegistroContacto(int idReg, int idSeleccion, String tipoContacto, 
                          LocalDate fechaContacto, String observaciones, String resultado) {
        this.idReg = idReg;
        this.idSeleccion = idSeleccion;
        this.tipoContacto = tipoContacto;
        this.fechaContacto = fechaContacto;
        this.observaciones = observaciones;
        this.resultado = resultado;
    }

    public int getIdReg() {
        return idReg;
    }

    public void setIdReg(int idReg) {
        this.idReg = idReg;
    }

    public int getIdSeleccion() {
        return idSeleccion;
    }

    public void setIdSeleccion(int idSeleccion) {
        this.idSeleccion = idSeleccion;
    }

    public String getTipoContacto() {
        return tipoContacto;
    }

    public void setTipoContacto(String tipoContacto) {
        this.tipoContacto = tipoContacto;
    }

    public LocalDate getFechaContacto() {
        return fechaContacto;
    }

    public void setFechaContacto(LocalDate fechaContacto) {
        this.fechaContacto = fechaContacto;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    @Override
    public String toString() {
        return "RegistroContacto{" +
                "idReg=" + idReg +
                ", idSeleccion=" + idSeleccion +
                ", tipoContacto='" + tipoContacto + '\'' +
                ", fechaContacto=" + fechaContacto +
                ", observaciones='" + observaciones + '\'' +
                ", resultado='" + resultado + '\'' +
                '}';
    }
}
