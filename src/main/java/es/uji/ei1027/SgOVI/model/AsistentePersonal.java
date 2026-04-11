package es.uji.ei1027.SgOVI.model;

public class AsistentePersonal {

    private String idAsistente;
    private String nombre;
    private String email;
    private String contrasena;
    private String tipoAsistente;
    private String estadoValidacion;
    private String formacionPrevia;
    private String disponibilidad;

    public AsistentePersonal() {}

    public AsistentePersonal(String idAsistente, String nombre, String email, String tipoAsistente, 
                           String estadoValidacion, String formacionPrevia, String disponibilidad) {
        this.idAsistente = idAsistente;
        this.nombre = nombre;
        this.email = email;
        this.tipoAsistente = tipoAsistente;
        this.estadoValidacion = estadoValidacion;
        this.formacionPrevia = formacionPrevia;
        this.disponibilidad = disponibilidad;
    }

    public String getIdAsistente() {
        return idAsistente;
    }

    public void setIdAsistente(String idAsistente) {
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

    public String getTipoAsistente() {
        return tipoAsistente;
    }

    public void setTipoAsistente(String tipoAsistente) {
        this.tipoAsistente = tipoAsistente;
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

    @Override
    public String toString() {
        return "AsistentePersonal{" +
                "idAsistente='" + idAsistente + '\'' +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", tipoAsistente='" + tipoAsistente + '\'' +
                ", estadoValidacion='" + estadoValidacion + '\'' +
                ", formacionPrevia='" + formacionPrevia + '\'' +
                ", disponibilidad='" + disponibilidad + '\'' +
                '}';
    }
}
