package es.uji.ei1027.SgOVI.rowMapper;

import es.uji.ei1027.SgOVI.model.PeticionAPR;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

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
        peticion.setFranjasHorarias(rs.getString("franjas_horarias"));
        peticion.setTipoTareas(rs.getString("tipo_tareas"));
        peticion.setUbicacion(rs.getString("ubicacion"));
        peticion.setPreferenciasGenero(rs.getString("preferencias_genero"));
        peticion.setExperienciaMinima(rs.getInt("experiencia_minima"));
        peticion.setFormacionEspecifica(rs.getString("formacion_especifica"));
        peticion.setIdiomas(rs.getString("idiomas"));
        peticion.setOtrasPreferencias(rs.getString("otras_preferencias"));
        peticion.setMunicipio(rs.getString("municipio"));
        peticion.setFechaInicioPrevista(rs.getDate("fecha_inicio_prevista") != null ? 
                rs.getDate("fecha_inicio_prevista").toLocalDate() : null);
        peticion.setTiempoPreferido(rs.getString("tiempo_preferido"));
        peticion.setPreferenciaGenero(rs.getString("preferencia_genero"));
        peticion.setPreferencias(rs.getString("preferencias"));
        peticion.setFechaRevision(rs.getDate("fecha_revision") != null ? 
                rs.getDate("fecha_revision").toLocalDate() : null);
        peticion.setMotivoRechazo(rs.getString("motivo_rechazo"));
        peticion.setObservacionesTecnico(rs.getString("observaciones_tecnico"));
        return peticion;
    }
}