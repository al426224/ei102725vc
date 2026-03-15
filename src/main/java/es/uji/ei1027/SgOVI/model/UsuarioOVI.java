package es.uji.ei1027.SgOVI.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.time.LocalDate;

public class UsuarioOVI {

    private String idUsuario;
    private String nombre;
    private String email;
    private String telefono;
    private LocalDate fechaRegistro;
    private boolean consentimientoLOPD;
    private String dni;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;
    private String proyectoVida;
    private String estado;

    public UsuarioOVI() {}

    public UsuarioOVI(String idUsuario, String nombre, String email, String telefono, 
                      LocalDate fechaRegistro, boolean consentimientoLOPD, String dni,
                      LocalDate fechaNacimiento, String proyectoVida, String estado) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.fechaRegistro = fechaRegistro;
        this.consentimientoLOPD = consentimientoLOPD;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.proyectoVida = proyectoVida;
        this.estado = estado;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getProyectoVida() {
        return proyectoVida;
    }

    public void setProyectoVida(String proyectoVida) {
        this.proyectoVida = proyectoVida;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "UsuarioOVI{" +
                "idUsuario='" + idUsuario + '\'' +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                ", consentimientoLOPD=" + consentimientoLOPD +
                ", dni='" + dni + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", proyectoVida='" + proyectoVida + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
