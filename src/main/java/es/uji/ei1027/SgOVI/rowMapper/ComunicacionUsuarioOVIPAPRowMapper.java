package es.uji.ei1027.SgOVI.rowMapper;

import es.uji.ei1027.SgOVI.model.ComunicacionUsuarioOVIPAP;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ComunicacionUsuarioOVIPAPRowMapper implements RowMapper<ComunicacionUsuarioOVIPAP> {

    @Override
    public ComunicacionUsuarioOVIPAP mapRow(ResultSet rs, int rowNum) throws SQLException {
        ComunicacionUsuarioOVIPAP comu = new ComunicacionUsuarioOVIPAP();
        comu.setIdComu(rs.getInt("id_comu"));
        comu.setIdSeleccion(rs.getInt("id_seleccion"));
        comu.setEmisor(rs.getString("emisor"));
        comu.setMensaje(rs.getString("mensaje"));
        comu.setHora(rs.getTimestamp("hora").toLocalDateTime());
        comu.setMedio(rs.getString("medio"));
        return comu;
    }
}
