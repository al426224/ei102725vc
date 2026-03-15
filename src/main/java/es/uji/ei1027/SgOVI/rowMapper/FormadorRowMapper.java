package es.uji.ei1027.SgOVI.rowMapper;

import es.uji.ei1027.SgOVI.model.Formador;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FormadorRowMapper implements RowMapper<Formador> {

    @Override
    public Formador mapRow(ResultSet rs, int rowNum) throws SQLException {
        Formador formador = new Formador();
        formador.setIdFormador(rs.getInt("id_formador"));
        formador.setNombre(rs.getString("nombre"));
        formador.setEmail(rs.getString("email"));
        formador.setTelefono(rs.getString("telefono"));
        formador.setEspecialidad(rs.getString("especialidad"));
        formador.setHistorialSesiones(rs.getString("historialsesiones"));
        return formador;
    }
}