package es.uji.ei1027.SgOVI.rowMapper;

import es.uji.ei1027.SgOVI.model.AsistentePersonal;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AsistentePersonalRowMapper implements RowMapper<AsistentePersonal> {

    @Override
    public AsistentePersonal mapRow(ResultSet rs, int rowNum) throws SQLException {
        AsistentePersonal asistent = new AsistentePersonal();
        asistent.setIdAsistente(rs.getString("id_asistente"));
        asistent.setNombre(rs.getString("nombre"));
        asistent.setEmail(rs.getString("email"));
        asistent.setTipoAsistente(rs.getString("tipo_asistente"));
        asistent.setEstadoValidacion(rs.getString("estado_validacion"));
        asistent.setFormacionPrevia(rs.getString("formacion_previa"));
        asistent.setDisponibilidad(rs.getString("disponibilidad"));
        return asistent;
    }
}
