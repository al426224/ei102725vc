package es.uji.ei1027.SgOVI.model;

import es.uji.ei1027.SgOVI.model.PeticionAPR;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class PeticionAPR {

    private int idSolicitud;
    private int idUsuario;
    private String tipoAsistencia;
    private String descripcion;
    private int horasSemanales;
    private String estado;
    private String tiempoPreferido;
    private String tipoTareas;
    private String municipio;
    private LocalDate fechaInicioPrevista;
    private String preferenciaGenero;
    private String preferencias;
    private String idiomasRequeridos;
    private LocalDate fechaRevision;
    private String motivoRechazo;
    private String observacionesTecnico;
    private String nombreUsuario;

    public PeticionAPR() {}

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

    public String getTiempoPreferido() { return tiempoPreferido; }
    public void setTiempoPreferido(String tiempoPreferido) { this.tiempoPreferido = tiempoPreferido; }

    public String getTipoTareas() { return tipoTareas; }
    public void setTipoTareas(String tipoTareas) {
        if (tipoTareas != null && !tipoTareas.isEmpty()) {
            this.tipoTareas = tipoTareas.substring(0, 1).toUpperCase() + tipoTareas.substring(1);
        } else {
            this.tipoTareas = tipoTareas;
        }
    }

    public String getMunicipio() { return municipio; }
    public void setMunicipio(String municipio) { this.municipio = municipio; }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public LocalDate getFechaInicioPrevista() { return fechaInicioPrevista; }
    public void setFechaInicioPrevista(LocalDate fechaInicioPrevista) { this.fechaInicioPrevista = fechaInicioPrevista; }

    public String getPreferenciaGenero() { return preferenciaGenero; }
    public void setPreferenciaGenero(String preferenciaGenero) { this.preferenciaGenero = preferenciaGenero; }

    public String getPreferencias() { return preferencias; }
    public void setPreferencias(String preferencias) { this.preferencias = preferencias; }

    public String getIdiomasRequeridos() { return idiomasRequeridos; }
    public void setIdiomasRequeridos(String idiomasRequeridos) { this.idiomasRequeridos = idiomasRequeridos; }

    public LocalDate getFechaRevision() { return fechaRevision; }
    public void setFechaRevision(LocalDate fechaRevision) { this.fechaRevision = fechaRevision; }

    public String getMotivoRechazo() { return motivoRechazo; }
    public void setMotivoRechazo(String motivoRechazo) { this.motivoRechazo = motivoRechazo; }

    public String getObservacionesTecnico() { return observacionesTecnico; }
    public void setObservacionesTecnico(String observacionesTecnico) { this.observacionesTecnico = observacionesTecnico; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

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