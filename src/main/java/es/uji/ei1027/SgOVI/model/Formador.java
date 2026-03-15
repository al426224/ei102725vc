package es.uji.ei1027.SgOVI.model;

public class Formador {

    private int idFormador;
    private String nombre;
    private String email;
    private String telefono;
    private String especialidad;
    private String historialSesiones;

    public Formador() {}

    public Formador(int idFormador, String nombre, String email, String telefono,
                    String especialidad, String historialSesiones) {
        this.idFormador = idFormador;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.especialidad = especialidad;
        this.historialSesiones = historialSesiones;
    }

    public int getIdFormador() {
        return idFormador;
    }

    public void setIdFormador(int idFormador) {
        this.idFormador = idFormador;
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

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getHistorialSesiones() {
        return historialSesiones;
    }

    public void setHistorialSesiones(String historialSesiones) {
        this.historialSesiones = historialSesiones;
    }

    @Override
    public String toString() {
        return "Formador{" +
                "idFormador=" + idFormador +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", especialidad='" + especialidad + '\'' +
                ", historialSesiones='" + historialSesiones + '\'' +
                '}';
    }
}
