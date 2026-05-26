package es.uji.ei1027.SgOVI.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class AsistentePersonal {

    private int idAsistente;
    private String nombre;
    private String email;
    private String contrasena;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;
    private String estadoValidacion;
    private String formacionPrevia;
    private String disponibilidad;
    private String municipio;

    public AsistentePersonal() {}

    public AsistentePersonal(int idAsistente, String nombre, String email, String contrasena, LocalDate fechaNacimiento,
                           String estadoValidacion, String formacionPrevia, String disponibilidad, String municipio) {
        this.idAsistente = idAsistente;
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.fechaNacimiento = fechaNacimiento;
        this.estadoValidacion = estadoValidacion;
        this.formacionPrevia = formacionPrevia;
        this.disponibilidad = disponibilidad;
        this.municipio = municipio;
    }

    public int getIdAsistente() {
        return idAsistente;
    }

    public void setIdAsistente(int idAsistente) {
        this.idAsistente = idAsistente;
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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEstadoValidacion() {
        return estadoValidacion;
    }

    public void setEstadoValidacion(String estadoValidacion) {
        this.estadoValidacion = estadoValidacion;
    }

    public String getFormacionPrevia() {
        return formacionPrevia;
    }

    public void setFormacionPrevia(String formacionPrevia) {
        this.formacionPrevia = formacionPrevia;
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(String disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    @Override
    public String toString() {
        return "AsistentePersonal{" +
                "idAsistente=" + idAsistente +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", estadoValidacion='" + estadoValidacion + '\'' +
                ", formacionPrevia='" + formacionPrevia + '\'' +
", disponibilidad='" + disponibilidad + '\'' +
                ", municipio='" + municipio + '\'' +
                '}';
    }
}
