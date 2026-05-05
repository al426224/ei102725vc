package es.uji.ei1027.SgOVI.model;

import java.time.LocalDate;

public class PeticionAPR {

    private int idSolicitud;
    private int idUsuario;
    private String tipoAsistencia;
    private String descripcion;
    private int horasSemanales;
    private String estado;
    private String franjasHorarias;
    private String tipoTareas;
    private String ubicacion;
    private String preferenciasGenero;
    private int experienciaMinima;
    private String formacionEspecifica;
    private String idiomas;
    private String otrasPreferencias;
    private String municipio;
    private LocalDate fechaInicioPrevista;
    private String tiempoPreferido;
    private String preferenciaGenero;
    private String preferencias;
    private LocalDate fechaRevision;
    private String motivoRechazo;
    private String observacionesTecnico;

    public PeticionAPR() {}

    public PeticionAPR(int idSolicitud, int idUsuario, String tipoAsistencia, 
                      String descripcion, int horasSemanales, String estado,
                      String franjasHorarias, String tipoTareas, String ubicacion,
                      String preferenciasGenero, int experienciaMinima, 
                      String formacionEspecifica, String idiomas, String otrasPreferencias,
                      String municipio, LocalDate fechaInicioPrevista, String tiempoPreferido,
                      String preferenciaGenero, String preferencias,
                      LocalDate fechaRevision, String motivoRechazo, String observacionesTecnico) {
        this.idSolicitud = idSolicitud;
        this.idUsuario = idUsuario;
        this.tipoAsistencia = tipoAsistencia;
        this.descripcion = descripcion;
        this.horasSemanales = horasSemanales;
        this.estado = estado;
        this.franjasHorarias = franjasHorarias;
        this.tipoTareas = tipoTareas;
        this.ubicacion = ubicacion;
        this.preferenciasGenero = preferenciasGenero;
        this.experienciaMinima = experienciaMinima;
        this.formacionEspecifica = formacionEspecifica;
        this.idiomas = idiomas;
        this.otrasPreferencias = otrasPreferencias;
        this.municipio = municipio;
        this.fechaInicioPrevista = fechaInicioPrevista;
        this.tiempoPreferido = tiempoPreferido;
        this.preferenciaGenero = preferenciaGenero;
        this.preferencias = preferencias;
        this.fechaRevision = fechaRevision;
        this.motivoRechazo = motivoRechazo;
        this.observacionesTecnico = observacionesTecnico;
    }

    public int getIdSolicitud() { return idSolicitud; }
    public void setIdSolicitud(int idSolicitud) { this.idSolicitud = idSolicitud; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getTipoAsistencia() { return tipoAsistencia; }
    public void setTipoAsistencia(String tipoAsistencia) { this.tipoAsistencia = tipoAsistencia; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getHorasSemanales() { return horasSemanales; }
    public void setHorasSemanales(int horasSemanales) { this.horasSemanales = horasSemanales; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFranjasHorarias() { return franjasHorarias; }
    public void setFranjasHorarias(String franjasHorarias) { this.franjasHorarias = franjasHorarias; }

    public String getTipoTareas() { return tipoTareas; }
    public void setTipoTareas(String tipoTareas) { this.tipoTareas = tipoTareas; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getPreferenciasGenero() { return preferenciasGenero; }
    public void setPreferenciasGenero(String preferenciasGenero) { this.preferenciasGenero = preferenciasGenero; }

    public int getExperienciaMinima() { return experienciaMinima; }
    public void setExperienciaMinima(int experienciaMinima) { this.experienciaMinima = experienciaMinima; }

    public String getFormacionEspecifica() { return formacionEspecifica; }
    public void setFormacionEspecifica(String formacionEspecifica) { this.formacionEspecifica = formacionEspecifica; }

    public String getIdiomas() { return idiomas; }
    public void setIdiomas(String idiomas) { this.idiomas = idiomas; }

    public String getOtrasPreferencias() { return otrasPreferencias; }
    public void setOtrasPreferencias(String otrasPreferencias) { this.otrasPreferencias = otrasPreferencias; }

    public String getMunicipio() { return municipio; }
    public void setMunicipio(String municipio) { this.municipio = municipio; }

    public LocalDate getFechaInicioPrevista() { return fechaInicioPrevista; }
    public void setFechaInicioPrevista(LocalDate fechaInicioPrevista) { this.fechaInicioPrevista = fechaInicioPrevista; }

    public String getTiempoPreferido() { return tiempoPreferido; }
    public void setTiempoPreferido(String tiempoPreferido) { this.tiempoPreferido = tiempoPreferido; }

    public String getPreferenciaGenero() { return preferenciaGenero; }
    public void setPreferenciaGenero(String preferenciaGenero) { this.preferenciaGenero = preferenciaGenero; }

    public String getPreferencias() { return preferencias; }
    public void setPreferencias(String preferencias) { this.preferencias = preferencias; }

    public LocalDate getFechaRevision() { return fechaRevision; }
    public void setFechaRevision(LocalDate fechaRevision) { this.fechaRevision = fechaRevision; }

    public String getMotivoRechazo() { return motivoRechazo; }
    public void setMotivoRechazo(String motivoRechazo) { this.motivoRechazo = motivoRechazo; }

    public String getObservacionesTecnico() { return observacionesTecnico; }
    public void setObservacionesTecnico(String observacionesTecnico) { this.observacionesTecnico = observacionesTecnico; }

    @Override
    public String toString() {
        return "PeticionAPR{" +
                "idSolicitud=" + idSolicitud +
                ", idUsuario=" + idUsuario +
                ", tipoAsistencia='" + tipoAsistencia + '\'' +
                ", horasSemanales=" + horasSemanales +
                ", estado='" + estado + '\'' +
                '}';
    }
}