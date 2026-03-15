package es.uji.ei1027.SgOVI.dao;

import es.uji.ei1027.SgOVI.model.AsistentePersonal;
import es.uji.ei1027.SgOVI.rowMapper.AsistentePersonalRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class AsistentePersonalDao {

    private JdbcTemplate jdbcTemplate;
    private final Logger logger = Logger.getLogger(AsistentePersonalDao.class.getName());

    public static final String GET_ASSISTENT_BY_ID = "SELECT * FROM AsistentePersonal WHERE id_asistente = ?";
    public static final String GET_ASSISTENT_BY_EMAIL = "SELECT * FROM AsistentePersonal WHERE email = ?";
    public static final String GET_ASSISTENT_BY_TIPO = "SELECT * FROM AsistentePersonal WHERE tipo_asistente = ?";
    public static final String GET_ASSISTENTS_BY_ESTADO = "SELECT * FROM AsistentePersonal WHERE estado_validacion = ?";
    public static final String ADD_ASSISTENT = "INSERT INTO AsistentePersonal (id_asistente, nombre, email, tipo_asistente, estado_validacion, formacion_previa, disponibilidad) VALUES (?, ?, ?, ?, ?, ?, ?)";
    public static final String DELETE_ASSISTENT = "DELETE FROM AsistentePersonal WHERE id_asistente = ?";
    public static final String UPDATE_ASSISTENT = "UPDATE AsistentePersonal SET nombre = ?, email = ?, tipo_asistente = ?, estado_validacion = ?, formacion_previa = ?, disponibilidad = ? WHERE id_asistente = ?";
    public static final String GET_ASSISTANTS = "SELECT * FROM AsistentePersonal";

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public AsistentePersonal getAsistente(String id) {
        try {
            return jdbcTemplate.queryForObject(GET_ASSISTENT_BY_ID, new AsistentePersonalRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontró el asistente con id: " + id);
            return null;
        }
    }

    public AsistentePersonal getAsistenteByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject(GET_ASSISTENT_BY_EMAIL, new AsistentePersonalRowMapper(), email);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontró el asistente con email: " + email);
            return null;
        }
    }

    public List<AsistentePersonal> getAsistentesByTipo(String tipoAsistente) {
        try {
            return jdbcTemplate.query(GET_ASSISTENT_BY_TIPO, new AsistentePersonalRowMapper(), tipoAsistente);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron asistentes de tipo: " + tipoAsistente);
            return new ArrayList<>();
        }
    }

    public List<AsistentePersonal> getAsistentesByEstado(String estadoValidacion) {
        try {
            return jdbcTemplate.query(GET_ASSISTENTS_BY_ESTADO, new AsistentePersonalRowMapper(), estadoValidacion);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron asistentes con estado: " + estadoValidacion);
            return new ArrayList<>();
        }
    }

    public void addAsistente(AsistentePersonal asistente) {
        jdbcTemplate.update(ADD_ASSISTENT, asistente.getIdAsistente(), asistente.getNombre(), asistente.getEmail(), 
                asistente.getTipoAsistente(), asistente.getEstadoValidacion(), asistente.getFormacionPrevia(),
                asistente.getDisponibilidad());
    }

    public void updateAsistente(AsistentePersonal asistente) {
        jdbcTemplate.update(UPDATE_ASSISTENT, asistente.getNombre(), asistente.getEmail(), 
                asistente.getTipoAsistente(), asistente.getEstadoValidacion(), 
                asistente.getFormacionPrevia(), asistente.getDisponibilidad(), asistente.getIdAsistente());
    }

    public void deleteAsistente(String id) {
        jdbcTemplate.update(DELETE_ASSISTENT, id);
    }

    public List<AsistentePersonal> getAsistentes() {
        try {
            return jdbcTemplate.query(GET_ASSISTANTS, new AsistentePersonalRowMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron asistentes.");
            return new ArrayList<>();
        }
    }
}
