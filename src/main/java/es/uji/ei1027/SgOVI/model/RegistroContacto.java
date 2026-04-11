package es.uji.ei1027.SgOVI.model;

import java.time.LocalDate;

public class RegistroContacto {

    private int idReg;
    private int idSeleccion;
    private String tipoContrato;
    private LocalDate fechaInicio;
    private String observaciones;
    private String resultado;
    private LocalDate fechaFin;

    public RegistroContacto() {}

    public RegistroContacto(int idReg, int idSeleccion, String tipoContrato, 
                          LocalDate fechaInicio, String observaciones, String resultado, LocalDate fechaFin) {
        this.idReg = idReg;
        this.idSeleccion = idSeleccion;
        this.tipoContrato = tipoContrato;
        this.fechaInicio = fechaInicio;
        this.observaciones = observaciones;
        this.resultado = resultado;
        this.fechaFin = fechaFin;
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

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
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

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    @Override
    public String toString() {
        return "RegistroContacto{" +
                "idReg=" + idReg +
                ", idSeleccion=" + idSeleccion +
                ", tipoContrato='" + tipoContrato + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", observaciones='" + observaciones + '\'' +
                ", resultado='" + resultado + '\'' +
                ", fechaFin=" + fechaFin +
                '}';
    }
}