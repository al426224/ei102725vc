package es.uji.ei1027.SgOVI.dao;

import es.uji.ei1027.SgOVI.model.Formador;
import es.uji.ei1027.SgOVI.rowMapper.FormadorRowMapper;
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
public class FormadorDao {

    private JdbcTemplate jdbcTemplate;
    private final Logger logger = Logger.getLogger(FormadorDao.class.getName());

    private static final String TABLE_NAME = "formador";
    
    private static final String GET_FORMADOR_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE id_formador = ?";
    private static final String GET_FORMADOR_BY_EMAIL = "SELECT * FROM " + TABLE_NAME + " WHERE email = ?";
    private static final String GET_FORMADORES_BY_ESPECIALIDAD = "SELECT * FROM " + TABLE_NAME + " WHERE especialidad = ?";
    private static final String ADD_FORMADOR = "INSERT INTO " + TABLE_NAME + " (nombre, email, contrasena, telefono, especialidad, historialsesiones) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String DELETE_FORMADOR = "DELETE FROM " + TABLE_NAME + " WHERE id_formador = ?";
    private static final String UPDATE_FORMADOR = "UPDATE " + TABLE_NAME + " SET nombre = ?, email = ?, contrasena = ?, telefono = ?, especialidad = ?, historialsesiones = ? WHERE id_formador = ?";
    private static final String GET_FORMADORES = "SELECT * FROM " + TABLE_NAME;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Formador getFormador(int id) {
        try {
            return jdbcTemplate.queryForObject(GET_FORMADOR_BY_ID, new FormadorRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontró el formador con id: " + id);
            return null;
        }
    }

    public Formador getFormadorByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject(GET_FORMADOR_BY_EMAIL, new FormadorRowMapper(), email);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontró el formador con email: " + email);
            return null;
        }
    }

    public List<Formador> getFormadoresByEspecialidad(String especialidad) {
        try {
            return jdbcTemplate.query(GET_FORMADORES_BY_ESPECIALIDAD, new FormadorRowMapper(), especialidad);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron formadores con especialidad: " + especialidad);
            return new ArrayList<>();
        }
    }

    public void addFormador(Formador formador) {
        jdbcTemplate.update(ADD_FORMADOR, formador.getNombre(), formador.getEmail(),
                formador.getContrasena(), formador.getTelefono(), formador.getEspecialidad(), 
                formador.getHistorialSesiones());
    }

    public void updateFormador(Formador formador) {
        jdbcTemplate.update(UPDATE_FORMADOR, formador.getNombre(), formador.getEmail(),
                formador.getContrasena(), formador.getTelefono(), formador.getEspecialidad(), 
                formador.getHistorialSesiones(), formador.getIdFormador());
    }

    public void deleteFormador(int id) {
        jdbcTemplate.update(DELETE_FORMADOR, id);
    }

    public List<Formador> getFormadores() {
        try {
            return jdbcTemplate.query(GET_FORMADORES, new FormadorRowMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron formadores.");
            return new ArrayList<>();
        }
    }

    public Formador auth(String email, String password) {
        try {
            Formador user = jdbcTemplate.queryForObject(GET_FORMADOR_BY_EMAIL, new FormadorRowMapper(), email);
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