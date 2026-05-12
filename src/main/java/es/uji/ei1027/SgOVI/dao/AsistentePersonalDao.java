package es.uji.ei1027.SgOVI.dao;

import es.uji.ei1027.SgOVI.model.AsistentePersonal;
import es.uji.ei1027.SgOVI.rowMapper.AsistentePersonalRowMapper;
import org.jasypt.util.password.BasicPasswordEncryptor;
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

    private static final String TABLE_NAME = "asistentepersonal";
    
    private static final String GET_ASSISTENT_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE id_asistente = ?";
    private static final String GET_ASSISTENT_BY_EMAIL = "SELECT * FROM " + TABLE_NAME + " WHERE email = ?";
    private static final String GET_ASSISTENT_BY_TIPO = "SELECT * FROM " + TABLE_NAME + " WHERE tipo_asistente = ?";
    private static final String GET_ASSISTENTS_BY_ESTADO = "SELECT * FROM " + TABLE_NAME + " WHERE estado_validacion = ?";
    private static final String GET_ASSISTENTS_COMPATIBLES = "SELECT * FROM " + TABLE_NAME + " WHERE tipo_asistente = ? AND estado_validacion = 'aceptado'";
    private static final String ADD_ASSISTENT = "INSERT INTO " + TABLE_NAME + " (nombre, email, contrasena, tipo_asistente, estado_validacion, formacion_previa, disponibilidad, municipio) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String DELETE_ASSISTENT = "DELETE FROM " + TABLE_NAME + " WHERE id_asistente = ?";
    private static final String UPDATE_ASSISTENT = "UPDATE " + TABLE_NAME + " SET nombre = ?, email = ?, contrasena = ?, tipo_asistente = ?, estado_validacion = ?, formacion_previa = ?, disponibilidad = ?, municipio = ? WHERE id_asistente = ?";
    private static final String GET_ASSISTANTS = "SELECT * FROM " + TABLE_NAME;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public AsistentePersonal getAsistente(int id) {
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
        if (estadoValidacion == null || estadoValidacion.isEmpty()) {
            return getAsistentes();
        }
        try {
            return jdbcTemplate.query(GET_ASSISTENTS_BY_ESTADO, new AsistentePersonalRowMapper(), estadoValidacion);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron asistentes con estado: " + estadoValidacion);
            return new ArrayList<>();
        }
    }

public void addAsistente(AsistentePersonal asistente) {
        jdbcTemplate.update(ADD_ASSISTENT, asistente.getNombre(), asistente.getEmail(),
                asistente.getContrasena(), asistente.getTipoAsistente(), asistente.getEstadoValidacion(), 
                asistente.getFormacionPrevia(), asistente.getDisponibilidad(), asistente.getMunicipio());
    }

    public void updateAsistente(AsistentePersonal asistente) {
        jdbcTemplate.update(UPDATE_ASSISTENT, asistente.getNombre(), asistente.getEmail(),
                asistente.getContrasena(), asistente.getTipoAsistente(), asistente.getEstadoValidacion(), 
                asistente.getFormacionPrevia(), asistente.getDisponibilidad(), asistente.getMunicipio(), 
                asistente.getIdAsistente());
    }

    public void deleteAsistente(int id) {
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

    public List<AsistentePersonal> getCandidatosCompatibles(String tipoAsistencia) {
        if (tipoAsistencia == null || tipoAsistencia.trim().isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return jdbcTemplate.query(GET_ASSISTENTS_COMPATIBLES, new AsistentePersonalRowMapper(), tipoAsistencia);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron candidatos compatibles para tipo: " + tipoAsistencia);
            return new ArrayList<>();
        }
    }

public AsistentePersonal auth(String email, String password) {
        try {
            AsistentePersonal user = jdbcTemplate.queryForObject(GET_ASSISTENT_BY_EMAIL, new AsistentePersonalRowMapper(), email);
            if (user == null) {
                return null;
            }
            BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
            if (passwordEncryptor.checkPassword(password, user.getContrasena())) {
                user.setContrasena(null);
                return user;
            }
            return null;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
