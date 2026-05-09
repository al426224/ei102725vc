package es.uji.ei1027.SgOVI.model;

public class TecnicoOVI {

    private int idTecnico;
    private String nombre;
    private String email;
    private String contrasena;

    public TecnicoOVI() {}

    public TecnicoOVI(int idTecnico, String nombre, String email, String contrasena) {
        this.idTecnico = idTecnico;
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
    }

    public int getIdTecnico() {
        return idTecnico;
    }

    public void setIdTecnico(int idTecnico) {
        this.idTecnico = idTecnico;
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

    @Override
    public String toString() {
        return "TecnicoOVI{" +
                "idTecnico=" + idTecnico +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}