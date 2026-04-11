package es.uji.ei1027.SgOVI.rowMapper;

import es.uji.ei1027.SgOVI.model.RegistroContacto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistroContactoRowMapper implements RowMapper<RegistroContacto> {

    @Override
    public RegistroContacto mapRow(ResultSet rs, int rowNum) throws SQLException {
        RegistroContacto registro = new RegistroContacto();
        registro.setIdReg(rs.getInt("id_reg"));
        registro.setIdSeleccion(rs.getInt("id_seleccion"));
        registro.setTipoContrato(rs.getString("tipo_contrato"));
        registro.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
        registro.setObservaciones(rs.getString("observaciones"));
        registro.setResultado(rs.getString("resultado"));
        registro.setFechaFin(rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toLocalDate() : null);
        return registro;
    }
}
