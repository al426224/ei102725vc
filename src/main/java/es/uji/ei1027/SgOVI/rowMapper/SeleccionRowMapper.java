package es.uji.ei1027.SgOVI.rowMapper;

import es.uji.ei1027.SgOVI.model.Seleccion;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SeleccionRowMapper implements RowMapper<Seleccion> {

    @Override
    public Seleccion mapRow(ResultSet rs, int rowNum) throws SQLException {
        Seleccion seleccion = new Seleccion();
        seleccion.setIdSeleccion(rs.getInt("id_seleccion"));
        seleccion.setIdSolicitud(rs.getInt("id_solicitud"));
        seleccion.setIdAsistente(rs.getInt("id_asistente"));
        seleccion.setEstadoSeleccion(rs.getString("estado_seleccion"));
        seleccion.setPuntuacionMatch(rs.getObject("puntuacion_match", Integer.class));
        return seleccion;
    }
}
