package es.uji.ei1027.SgOVI.model;

public class AsistentePersonal {

    private int idAssistent;
    private String nombre;
    private String email;
    private String tipoAsistente;
    private String estadoValidacion;
    private String formacionPrevia;

    public AsistentePersonal() {}

    public AsistentePersonal(int idAssistent, String nombre, String email, String tipoAsistente, 
                           String estadoValidacion, String formacionPrevia) {
        this.idAssistent = idAssistent;
        this.nombre = nombre;
        this.email = email;
        this.tipoAsistente = tipoAsistente;
        this.estadoValidacion = estadoValidacion;
        this.formacionPrevia = formacionPrevia;
    }

    public int getIdAssistent() {
        return idAssistent;
    }

    public void setIdAssistent(int idAssistent) {
        this.idAssistent = idAssistent;
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

    @Override
    public String toString() {
        return "AsistentePersonal{" +
                "idAssistent=" + idAssistent +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", tipoAsistente='" + tipoAsistente + '\'' +
                ", estadoValidacion='" + estadoValidacion + '\'' +
                ", formacionPrevia='" + formacionPrevia + '\'' +
                '}';
    }
}
