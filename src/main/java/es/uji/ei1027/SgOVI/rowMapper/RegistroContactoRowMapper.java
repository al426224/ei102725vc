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
        registro.setTipoContacto(rs.getString("tipo_contacto"));
        registro.setFechaContacto(rs.getDate("fecha_contacto").toLocalDate());
        registro.setObservaciones(rs.getString("observaciones"));
        registro.setResultado(rs.getString("resultado"));
        return registro;
    }
}
