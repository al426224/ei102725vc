package es.uji.ei1027.SgOVI.rowMapper;

import es.uji.ei1027.SgOVI.model.PeticionAPR;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PeticionAPRRowMapper implements RowMapper<PeticionAPR> {

    @Override
    public PeticionAPR mapRow(ResultSet rs, int rowNum) throws SQLException {
        PeticionAPR peticion = new PeticionAPR();
        peticion.setIdSolicitud(rs.getString("id_solicitud"));
        peticion.setIdUsuario(rs.getString("id_usuario"));
        peticion.setTipoAsistencia(rs.getString("tipo_asistencia"));
        peticion.setDescripcion(rs.getString("descripcion"));
        peticion.setHorasSemanales(rs.getInt("horas_semanales"));
        peticion.setEstado(rs.getString("estado"));
        peticion.setFechaSolicitud(rs.getDate("fecha_solicitud").toLocalDate());
        return peticion;
    }
}
