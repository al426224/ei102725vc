package es.uji.ei1027.SgOVI.dao;

import es.uji.ei1027.SgOVI.model.ActividadFormacion;
import es.uji.ei1027.SgOVI.rowMapper.ActividadFormacionRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class ActividadFormacioDao {
    private JdbcTemplate jdbcTemplate;
    private final Logger logger = Logger.getLogger(ActividadFormacionDao.class.getName());

    public static final String GET_ACTIVIDAD_BY_ID = "SELECT * FROM ActividadFormacion WHERE id_activitat = ?";
    public static final String GET_ACTIVIDADES_BY_FORMADOR = "SELECT * FROM ActividadFormacion WHERE id_formador = ?";
    public static final String GET_ACTIVIDADES_BY_TIPO = "SELECT * FROM ActividadFormacion WHERE tipo_evento = ?";
    public static final String ADD_ACTIVIDAD = "INSERT INTO ActividadFormacion (id_formador, titulo, fecha_actividad, tipo_evento) VALUES (?, ?, ?, ?)";
    public static final String DELETE_ACTIVIDAD = "DELETE FROM ActividadFormacion WHERE id_activitat = ?";
    public static final String UPDATE_ACTIVIDAD = "UPDATE ActividadFormacion SET id_formador = ?, titulo = ?, fecha_actividad = ?, tipo_evento = ? WHERE id_activitat = ?";
    public static final String GET_ACTIVIDADES = "SELECT * FROM ActividadFormacion";

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public ActividadFormacion getActividad(int id) {
        try {
            return jdbcTemplate.queryForObject(GET_ACTIVIDAD_BY_ID, new ActividadFormacionRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontró la actividad con id: " + id);
            return null;
        }
    }

    public List<ActividadFormacion> getActividadesByFormador(int idFormador) {
        try {
            return jdbcTemplate.query(GET_ACTIVIDADES_BY_FORMADOR, new ActividadFormacionRowMapper(), idFormador);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron actividades para el formador: " + idFormador);
            return new ArrayList<>();
        }
    }

    public List<ActividadFormacion> getActividadesByTipo(String tipoEvento) {
        try {
            return jdbcTemplate.query(GET_ACTIVIDADES_BY_TIPO, new ActividadFormacionRowMapper(), tipoEvento);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron actividades de tipo: " + tipoEvento);
            return new ArrayList<>();
        }
    }

    public void addActividad(ActividadFormacion actividad) {
        jdbcTemplate.update(ADD_ACTIVIDAD, actividad.getIdFormador(), actividad.getTitulo(),
                actividad.getFechaActividad(), actividad.getTipoEvento());
    }

    public void updateActividad(ActividadFormacion actividad) {
        jdbcTemplate.update(UPDATE_ACTIVIDAD, actividad.getIdFormador(), actividad.getTitulo(),
                actividad.getFechaActividad(), actividad.getTipoEvento(), actividad.getIdActivitat());
    }

    public void deleteActividad(int id) {
        jdbcTemplate.update(DELETE_ACTIVIDAD, id);
    }

    public List<ActividadFormacion> getActividades() {
        try {
            return jdbcTemplate.query(GET_ACTIVIDADES, new ActividadFormacionRowMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron actividades.");
            return new ArrayList<>();
        }
    }
}
