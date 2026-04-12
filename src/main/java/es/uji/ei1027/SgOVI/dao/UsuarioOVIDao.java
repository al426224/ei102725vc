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

    public static final String TABLE_NAME = "usuariovi";
    
    public static final String GET_USUARIO_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE id_usuario = ?";
    public static final String GET_USUARIO_BY_EMAIL = "SELECT * FROM " + TABLE_NAME + " WHERE email = ?";
    public static final String GET_USUARIO_BY_DNI = "SELECT * FROM " + TABLE_NAME + " WHERE dni = ?";
    public static final String ADD_USUARIO = "INSERT INTO " + TABLE_NAME + " (nombre, email, contrasena, telefono, fecha_registro, consentimiento_lopd, dni, fecha_nacimiento, proyecto_vida, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String DELETE_USUARIO = "DELETE FROM " + TABLE_NAME + " WHERE id_usuario = ?";
    public static final String UPDATE_USUARIO = "UPDATE " + TABLE_NAME + " SET nombre = ?, email = ?, contrasena = ?, telefono = ?, consentimiento_lopd = ?, dni = ?, fecha_nacimiento = ?, proyecto_vida = ?, estado = ? WHERE id_usuario = ?";
    public static final String GET_USUARIOS = "SELECT * FROM " + TABLE_NAME;
    public static final String GET_USUARIOS_BY_ESTADO = "SELECT * FROM " + TABLE_NAME + " WHERE estado = ?";

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
        jdbcTemplate.update(ADD_USUARIO, usuario.getNombre(), usuario.getEmail(), 
                usuario.getContrasena(), usuario.getTelefono(), usuario.getFechaRegistro(), 
                usuario.isConsentimientoLOPD(), usuario.getDni(), usuario.getFechaNacimiento(), 
                usuario.getProyectoVida(), usuario.getEstado());
    }

    public void updateUsuario(UsuarioOVI usuario) {
        jdbcTemplate.update(UPDATE_USUARIO, usuario.getNombre(), usuario.getEmail(), usuario.getContrasena(),
                usuario.getTelefono(), usuario.isConsentimientoLOPD(), usuario.getDni(), 
                usuario.getFechaNacimiento(), usuario.getProyectoVida(), usuario.getEstado(), 
                usuario.getIdUsuario());
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

    public List<UsuarioOVI> getUsuariosByEstado(String estado) {
        try {
            return jdbcTemplate.query(GET_USUARIOS_BY_ESTADO, new UsuarioOVIRowMapper(), estado);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron usuarios con estado: " + estado);
            return new ArrayList<>();
        }
    }
}