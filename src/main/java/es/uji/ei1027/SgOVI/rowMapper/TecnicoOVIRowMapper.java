package es.uji.ei1027.SgOVI.rowMapper;

import es.uji.ei1027.SgOVI.model.TecnicoOVI;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TecnicoOVIRowMapper implements RowMapper<TecnicoOVI> {

    @Override
    public TecnicoOVI mapRow(ResultSet rs, int rowNum) throws SQLException {
        TecnicoOVI tecnico = new TecnicoOVI();
        tecnico.setIdTecnico(rs.getInt("id_tecnico"));
        tecnico.setNombre(rs.getString("nombre"));
        tecnico.setEmail(rs.getString("email"));
        tecnico.setContrasena(rs.getString("contraseña"));
        return tecnico;
    }
}