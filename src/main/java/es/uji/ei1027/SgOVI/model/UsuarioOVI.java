package es.uji.ei1027.SgOVI.model;

import java.time.LocalDate;

public class UsuarioOVI {
    private int idUsuario;
    private String nombre;
    private String email;
    private String telefono;
    private LocalDate fechaRegistro;
    private boolean consentimientoLOPD;

    public UsuarioOVI() {}

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public boolean isConsentimientoLOPD() {
        return consentimientoLOPD;
    }

    public void setConsentimientoLOPD(boolean consentimientoLOPD) {
        this.consentimientoLOPD = consentimientoLOPD;
    }
}
