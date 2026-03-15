package es.uji.ei1027.SgOVI.rowMapper;

import es.uji.ei1027.SgOVI.model.ActividadFormacion;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ActividadFormacionRowMapper implements RowMapper<ActividadFormacion> {

    @Override
    public ActividadFormacion mapRow(ResultSet rs, int rowNum) throws SQLException {
        ActividadFormacion actividad = new ActividadFormacion();
        actividad.setIdActivitat(rs.getInt("id_activitat"));
        actividad.setIdFormador(rs.getInt("id_formador"));
        actividad.setTitulo(rs.getString("titulo"));
        actividad.setFechaActividad(rs.getDate("fecha_actividad").toLocalDate());
        actividad.setTipoEvento(rs.getString("tipo_evento"));
        return actividad;
    }
}