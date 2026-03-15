package es.uji.ei1027.SgOVI.dao;

import es.uji.ei1027.SgOVI.model.UsuarioOVI;
import es.uji.ei1027.SgOVI.rowMapper.UsuarioOVIRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class UsuarioOVIDao {

    private JdbcTemplate jdbcTemplate;
    private final Logger logger = Logger.getLogger(UsuarioOVIDao.class.getName());

    public static final String GET_USUARIO_BY_ID = "SELECT * FROM UsuarioOVI WHERE id_usuario = ?";
    public static final String GET_USUARIO_BY_EMAIL = "SELECT * FROM UsuarioOVI WHERE email = ?";
    public static final String GET_USUARIO_BY_DNI = "SELECT * FROM UsuarioOVI WHERE dni = ?";
    public static final String ADD_USUARIO = "INSERT INTO UsuarioOVI (nombre, email, telefono, consentimiento_lopd, dni, fecha_nacimiento, proyecto_vida_independiente, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String DELETE_USUARIO = "DELETE FROM UsuarioOVI WHERE id_usuario = ?";
    public static final String UPDATE_USUARIO = "UPDATE UsuarioOVI SET nombre = ?, email = ?, telefono = ?, consentimiento_lopd = ?, dni = ?, fecha_nacimiento = ?, proyecto_vida_independiente = ?, estado = ? WHERE id_usuario = ?";
    public static final String GET_USUARIOS = "SELECT * FROM UsuarioOVI";
    public static final String GET_USUARIOS_ACTIVOS = "SELECT * FROM UsuarioOVI WHERE estado = true";

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public UsuarioOVI getUsuario(int id) {
        try {
            return jdbcTemplate.queryForObject(GET_USUARIO_BY_ID, new UsuarioOVIRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontró el usuario con id: " + id);
            return null;
        }
    }

    public UsuarioOVI getUsuarioByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject(GET_USUARIO_BY_EMAIL, new UsuarioOVIRowMapper(), email);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontró el usuario con email: " + email);
            return null;
        }
    }

    public UsuarioOVI getUsuarioByDni(String dni) {
        try {
            return jdbcTemplate.queryForObject(GET_USUARIO_BY_DNI, new UsuarioOVIRowMapper(), dni);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontró el usuario con dni: " + dni);
            return null;
        }
    }

    public void addUsuario(UsuarioOVI usuario) {
        jdbcTemplate.update(ADD_USUARIO, usuario.getNombre(), usuario.getEmail(), usuario.getTelefono(), 
                usuario.isConsentimientoLOPD(), usuario.getDni(), usuario.getFechaNacimiento(), 
                usuario.getProyectoVidaIndependiente(), usuario.isEstado());
    }

    public void updateUsuario(UsuarioOVI usuario) {
        jdbcTemplate.update(UPDATE_USUARIO, usuario.getNombre(), usuario.getEmail(), usuario.getTelefono(), 
                usuario.isConsentimientoLOPD(), usuario.getDni(), usuario.getFechaNacimiento(), 
                usuario.getProyectoVidaIndependiente(), usuario.isEstado(), usuario.getIdUsuario());
    }

    public void deleteUsuario(int id) {
        jdbcTemplate.update(DELETE_USUARIO, id);
    }

    public List<UsuarioOVI> getUsuarios() {
        try {
            return jdbcTemplate.query(GET_USUARIOS, new UsuarioOVIRowMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron usuarios.");
            return new ArrayList<>();
        }
    }

    public List<UsuarioOVI> getUsuariosActivos() {
        try {
            return jdbcTemplate.query(GET_USUARIOS_ACTIVOS, new UsuarioOVIRowMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron usuarios activos.");
            return new ArrayList<>();
        }
    }
}
