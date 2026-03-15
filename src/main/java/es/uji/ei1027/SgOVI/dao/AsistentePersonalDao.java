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

    public static final String GET_ASSISTENT_BY_ID = "SELECT * FROM AsistentePersonal WHERE id_assistent = ?";
    public static final String GET_ASSISTENT_BY_EMAIL = "SELECT * FROM AsistentePersonal WHERE email = ?";
    public static final String GET_ASSISTENT_BY_TIPO = "SELECT * FROM AsistentePersonal WHERE tipo_asistente = ?";
    public static final String GET_ASSISTENTS_BY_ESTADO = "SELECT * FROM AsistentePersonal WHERE estado_validacion = ?";
    public static final String ADD_ASSISTENT = "INSERT INTO AsistentePersonal (nombre, email, tipo_asistente, estado_validacion, formacion_previa) VALUES (?, ?, ?, ?, ?)";
    public static final String DELETE_ASSISTENT = "DELETE FROM AsistentePersonal WHERE id_assistent = ?";
    public static final String UPDATE_ASSISTENT = "UPDATE AsistentePersonal SET nombre = ?, email = ?, tipo_asistente = ?, estado_validacion = ?, formacion_previa = ? WHERE id_assistent = ?";
    public static final String GET_ASSISTENTS = "SELECT * FROM AsistentePersonal";

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public AsistentePersonal getAssistent(int id) {
        try {
            return jdbcTemplate.queryForObject(GET_ASSISTENT_BY_ID, new AsistentePersonalRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontró el asistente con id: " + id);
            return null;
        }
    }

    public AsistentePersonal getAssistentByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject(GET_ASSISTENT_BY_EMAIL, new AsistentePersonalRowMapper(), email);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontró el asistente con email: " + email);
            return null;
        }
    }

    public List<AsistentePersonal> getAssistentsByTipo(String tipoAsistente) {
        try {
            return jdbcTemplate.query(GET_ASSISTENT_BY_TIPO, new AsistentePersonalRowMapper(), tipoAsistente);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron asistentes de tipo: " + tipoAsistente);
            return new ArrayList<>();
        }
    }

    public List<AsistentePersonal> getAssistentsByEstado(String estadoValidacion) {
        try {
            return jdbcTemplate.query(GET_ASSISTENTS_BY_ESTADO, new AsistentePersonalRowMapper(), estadoValidacion);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron asistentes con estado: " + estadoValidacion);
            return new ArrayList<>();
        }
    }

    public void addAssistent(AsistentePersonal assistent) {
        jdbcTemplate.update(ADD_ASSISTENT, assistent.getNombre(), assistent.getEmail(), 
                assistent.getTipoAsistente(), assistent.getEstadoValidacion(), assistent.getFormacionPrevia());
    }

    public void updateAssistent(AsistentePersonal assistent) {
        jdbcTemplate.update(UPDATE_ASSISTENT, assistent.getNombre(), assistent.getEmail(), 
                assistent.getTipoAsistente(), assistent.getEstadoValidacion(), 
                assistent.getFormacionPrevia(), assistent.getIdAssistent());
    }

    public void deleteAssistent(int id) {
        jdbcTemplate.update(DELETE_ASSISTENT, id);
    }

    public List<AsistentePersonal> getAssistents() {
        try {
            return jdbcTemplate.query(GET_ASSISTENTS, new AsistentePersonalRowMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron asistentes.");
            return new ArrayList<>();
        }
    }
}
