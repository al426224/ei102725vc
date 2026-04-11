package es.uji.ei1027.SgOVI.dao;

import es.uji.ei1027.SgOVI.rowMapper.AsistentePersonalRowMapper;
import es.uji.ei1027.SgOVI.rowMapper.ActividadFormacionRowMapper;
import es.uji.ei1027.SgOVI.model.AsistentePersonal;
import es.uji.ei1027.SgOVI.model.ActividadFormacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class AsistenciaFormacionDao {

    private JdbcTemplate jdbcTemplate;
    private final Logger logger = Logger.getLogger(AsistenciaFormacionDao.class.getName());

    public static final String TABLE_NAME = "assistenciaformacion";
    
    public static final String GET_ASISTENTES_BY_ACTIVIDAD =
            "SELECT a.* FROM asistentepersonal a JOIN " + TABLE_NAME + " af ON a.id_asistente = af.id_asistent WHERE af.id_activitat = ?";

    public static final String GET_ACTIVIDADES_BY_ASISTENTE =
            "SELECT af2.* FROM actividadformacion af2 JOIN " + TABLE_NAME + " af ON af2.id_activitat = af.id_activitat WHERE af.id_asistent = ?";

    public static final String ADD_ASISTENCIA = "INSERT INTO " + TABLE_NAME + " (id_activitat, id_usuario, id_asistent) VALUES (?, ?, ?)";
    public static final String DELETE_ASISTENCIA = "DELETE FROM " + TABLE_NAME + " WHERE id_activitat = ? AND (id_usuario = ? OR id_asistent = ?)";
    public static final String EXISTS_ASISTENCIA = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE id_activitat = ? AND (id_usuario = ? OR id_asistent = ?)";

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<AsistentePersonal> getAsistentesByActividad(int idActividad) {
        try {
            return jdbcTemplate.query(GET_ASISTENTES_BY_ACTIVIDAD, new AsistentePersonalRowMapper(), idActividad);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron asistentes para la actividad: " + idActividad);
            return new ArrayList<>();
        }
    }

    public List<ActividadFormacion> getActividadesByAsistente(String idAsistente) {
        try {
            return jdbcTemplate.query(GET_ACTIVIDADES_BY_ASISTENTE, new ActividadFormacionRowMapper(), idAsistente);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron actividades para el asistente: " + idAsistente);
            return new ArrayList<>();
        }
    }

    public void addAsistencia(int idActividad, String idAsistente) {
        jdbcTemplate.update(ADD_ASISTENCIA, idActividad, idAsistente);
    }

    public void deleteAsistencia(int idActividad, String idAsistente) {
        jdbcTemplate.update(DELETE_ASISTENCIA, idActividad, idAsistente);
    }

    public boolean existsAsistencia(int idActividad, String idAsistente) {
        int count = jdbcTemplate.queryForObject(EXISTS_ASISTENCIA, Integer.class, idActividad, idAsistente);
        return count > 0;
    }
}
