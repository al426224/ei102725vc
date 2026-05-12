package es.uji.ei1027.SgOVI.dao;

import es.uji.ei1027.SgOVI.model.TecnicoOVI;
import es.uji.ei1027.SgOVI.rowMapper.TecnicoOVIRowMapper;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.logging.Logger;

@Repository
public class TecnicoOVIDao {

    private JdbcTemplate jdbcTemplate;
    private final Logger logger = Logger.getLogger(TecnicoOVIDao.class.getName());

    private static final String TABLE_NAME = "tecnicoovi";
    
    private static final String GET_TECNICO_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE id_tecnico = ?";
    private static final String GET_TECNICO_BY_EMAIL = "SELECT * FROM " + TABLE_NAME + " WHERE email = ?";

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public TecnicoOVI getTecnico(int id) {
        try {
            return jdbcTemplate.queryForObject(GET_TECNICO_BY_ID, new TecnicoOVIRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontró el técnico con id: " + id);
            return null;
        }
    }

    public TecnicoOVI getTecnicoByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject(GET_TECNICO_BY_EMAIL, new TecnicoOVIRowMapper(), email);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontró el técnico con email: " + email);
            return null;
        }
    }

    public TecnicoOVI auth(String email, String password) {
        try {
            TecnicoOVI user = jdbcTemplate.queryForObject(GET_TECNICO_BY_EMAIL, new TecnicoOVIRowMapper(), email);
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