package es.uji.ei1027.SgOVI.model;

import java.time.LocalDate;

public class ActividadFormacion {

    private int idActivitat;
    private int idFormador;
    private String titulo;
    private LocalDate fechaActividad;
    private String tipoEvento;

    public ActividadFormacion() {}

    public ActividadFormacion(int idActivitat, int idFormador, String titulo,
                              LocalDate fechaActividad, String tipoEvento) {
        this.idActivitat = idActivitat;
        this.idFormador = idFormador;
        this.titulo = titulo;
        this.fechaActividad = fechaActividad;
        this.tipoEvento = tipoEvento;
    }

    public int getIdActivitat() {
        return idActivitat;
    }

    public void setIdActivitat(int idActivitat) {
        this.idActivitat = idActivitat;
    }

    public int getIdFormador() {
        return idFormador;
    }

    public void setIdFormador(int idFormador) {
        this.idFormador = idFormador;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDate getFechaActividad() {
        return fechaActividad;
    }

    public void setFechaActividad(LocalDate fechaActividad) {
        this.fechaActividad = fechaActividad;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    @Override
    public String toString() {
        return "ActividadFormacion{" +
                "idActivitat=" + idActivitat +
                ", idFormador=" + idFormador +
                ", titulo='" + titulo + '\'' +
                ", fechaActividad=" + fechaActividad +
                ", tipoEvento='" + tipoEvento + '\'' +
                '}';
    }
}