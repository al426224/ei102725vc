package es.uji.ei1027.SgOVI.dao;

import es.uji.ei1027.SgOVI.rowMapper.AsistentePersonalRowMapper;
import es.uji.ei1027.SgOVI.rowMapper.ActividadFormacionRowMapper;
import es.uji.ei1027.SgOVI.model.AsistentePersonal;
import es.uji.ei1027.SgOVI.model.ActividadFormacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class AsistenciaFormacionDao {

    private JdbcTemplate jdbcTemplate;

    private static final String TABLE_NAME = "assistenciaformacion";
    
    private static final String GET_ASISTENTES_BY_ACTIVIDAD =
            "SELECT a.* FROM asistentepersonal a JOIN " + TABLE_NAME + " af ON a.id_asistente = af.id_asistent WHERE af.id_activitat = ?";

    private static final String GET_ACTIVIDADES_BY_ASISTENTE =
            "SELECT af2.* FROM actividadformacion af2 JOIN " + TABLE_NAME + " af ON af2.id_activitat = af.id_activitat WHERE af.id_asistent = ?";

    private static final String ADD_ASISTENCIA = "INSERT INTO " + TABLE_NAME + " (id_activitat, id_usuario, id_asistent) VALUES (?, ?, ?)";
    private static final String DELETE_ASISTENCIA = "DELETE FROM " + TABLE_NAME + " WHERE id_activitat = ? AND (id_usuario = ? OR id_asistent = ?)";
    private static final String EXISTS_ASISTENCIA = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE id_activitat = ? AND (id_usuario = ? OR id_asistent = ?)";

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<AsistentePersonal> getAsistentesByActividad(int idActividad) {
        return jdbcTemplate.query(GET_ASISTENTES_BY_ACTIVIDAD, new AsistentePersonalRowMapper(), idActividad);
    }

    public List<ActividadFormacion> getActividadesByAsistente(int idAsistente) {
        return jdbcTemplate.query(GET_ACTIVIDADES_BY_ASISTENTE, new ActividadFormacionRowMapper(), idAsistente);
    }

    public void addAsistencia(int idActividad, int idUsuario, int idAsistente) {
        jdbcTemplate.update(ADD_ASISTENCIA, idActividad, idUsuario, idAsistente);
    }

    public void deleteAsistencia(int idActividad, int idUsuario, int idAsistente) {
        jdbcTemplate.update(DELETE_ASISTENCIA, idActividad, idUsuario, idAsistente);
    }

    public boolean existsAsistencia(int idActividad, int idUsuario, int idAsistente) {
        int count = jdbcTemplate.queryForObject(EXISTS_ASISTENCIA, Integer.class, idActividad, idUsuario, idAsistente);
        return count > 0;
    }
}
