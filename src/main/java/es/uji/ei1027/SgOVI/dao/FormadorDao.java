package es.uji.ei1027.SgOVI.dao;

import es.uji.ei1027.SgOVI.model.Formador;
import es.uji.ei1027.SgOVI.rowMapper.FormadorRowMapper;
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

    public static final String TABLE_NAME = "formador";
    
    public static final String GET_FORMADOR_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE id_formador = ?";
    public static final String GET_FORMADOR_BY_EMAIL = "SELECT * FROM " + TABLE_NAME + " WHERE email = ?";
    public static final String GET_FORMADORES_BY_ESPECIALIDAD = "SELECT * FROM " + TABLE_NAME + " WHERE especialidad = ?";
    public static final String ADD_FORMADOR = "INSERT INTO " + TABLE_NAME + " (nombre, email, telefono, especialidad, historialsesiones) VALUES (?, ?, ?, ?, ?)";
    public static final String DELETE_FORMADOR = "DELETE FROM " + TABLE_NAME + " WHERE id_formador = ?";
    public static final String UPDATE_FORMADOR = "UPDATE " + TABLE_NAME + " SET nombre = ?, email = ?, telefono = ?, especialidad = ?, historialsesiones = ? WHERE id_formador = ?";
    public static final String GET_FORMADORES = "SELECT * FROM " + TABLE_NAME;

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
                formador.getTelefono(), formador.getEspecialidad(), formador.getHistorialSesiones());
    }

    public void updateFormador(Formador formador) {
        jdbcTemplate.update(UPDATE_FORMADOR, formador.getNombre(), formador.getEmail(),
                formador.getTelefono(), formador.getEspecialidad(), formador.getHistorialSesiones(),
                formador.getIdFormador());
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
}