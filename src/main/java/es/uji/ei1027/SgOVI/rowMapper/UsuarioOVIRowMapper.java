package es.uji.ei1027.SgOVI.rowMapper;

import es.uji.ei1027.SgOVI.model.UsuarioOVI;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioOVIRowMapper implements RowMapper<UsuarioOVI> {

    @Override
    public UsuarioOVI mapRow(ResultSet rs, int rowNum) throws SQLException {
        UsuarioOVI usuarioOVI = new UsuarioOVI();
        usuarioOVI.setIdUsuario(rs.getInt("id_usuario"));
        usuarioOVI.setNombre(rs.getString("nombre"));
        usuarioOVI.setEmail(rs.getString("email"));
        usuarioOVI.setContrasena(rs.getString("contrasena"));
        usuarioOVI.setTelefono(rs.getString("telefono"));
        usuarioOVI.setFechaRegistro(rs.getDate("fecha_registro").toLocalDate());
        usuarioOVI.setConsentimientoLOPD(rs.getBoolean("consentimiento_lopd"));
        usuarioOVI.setDni(rs.getString("dni"));
        usuarioOVI.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
        usuarioOVI.setProyectoVida(rs.getString("proyecto_vida"));
        usuarioOVI.setEstado(rs.getString("estado"));
        return usuarioOVI;
    }
}
