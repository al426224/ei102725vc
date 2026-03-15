package es.uji.ei1027.SgOVI.rowMapper;

import es.uji.ei1027.SgOVI.model.AsistentePersonal;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AsistentePersonalRowMapper implements RowMapper<AsistentePersonal> {

    @Override
    public AsistentePersonal mapRow(ResultSet rs, int rowNum) throws SQLException {
        AsistentePersonal assistent = new AsistentePersonal();
        assistent.setIdAssistent(rs.getInt("id_assistent"));
        assistent.setNombre(rs.getString("nombre"));
        assistent.setEmail(rs.getString("email"));
        assistent.setTipoAsistente(rs.getString("tipo_asistente"));
        assistent.setEstadoValidacion(rs.getString("estado_validacion"));
        assistent.setFormacionPrevia(rs.getString("formacion_previa"));
        return assistent;
    }
}
