package es.uji.ei1027.SgOVI.rowMapper;

import es.uji.ei1027.SgOVI.model.PeticionAPR;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PeticionAPRRowMapper implements RowMapper<PeticionAPR> {

    @Override
    public PeticionAPR mapRow(ResultSet rs, int rowNum) throws SQLException {
        PeticionAPR peticion = new PeticionAPR();
        peticion.setIdSolicitud(rs.getInt("id_solicitud"));
        peticion.setIdUsuario(rs.getInt("id_usuario"));
        peticion.setTipoAsistencia(rs.getString("tipo_asistencia"));
        peticion.setDescripcion(rs.getString("descripcion"));
        peticion.setHorasSemanales(rs.getInt("horas_semanales"));
        peticion.setEstado(rs.getString("estado"));
        peticion.setTiempoPreferido(rs.getString("tiempo_preferido"));
        peticion.setTipoTareas(rs.getString("tipo_tareas"));
        peticion.setMunicipio(rs.getString("municipio"));
        peticion.setFechaInicioPrevista(rs.getDate("fecha_inicio_prevista") != null ?
                rs.getDate("fecha_inicio_prevista").toLocalDate() : null);
        peticion.setPreferenciaGenero(rs.getString("preferencia_genero"));
        peticion.setPreferencias(rs.getString("preferencias"));
        peticion.setIdiomasRequeridos(rs.getString("idiomas_requeridos"));
        peticion.setFechaRevision(rs.getDate("fecha_revision") != null ?
                rs.getDate("fecha_revision").toLocalDate() : null);
        peticion.setMotivoRechazo(rs.getString("motivo_rechazo"));
        peticion.setObservacionesTecnico(rs.getString("observaciones_tecnico"));
        peticion.setNombreUsuario(rs.getString("nombre_usuario"));
        return peticion;
    }
}