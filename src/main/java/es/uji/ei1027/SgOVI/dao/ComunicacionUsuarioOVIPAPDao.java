package es.uji.ei1027.SgOVI.dao;

import es.uji.ei1027.SgOVI.model.ComunicacionUsuarioOVIPAP;
import es.uji.ei1027.SgOVI.rowMapper.ComunicacionUsuarioOVIPAPRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class ComunicacionUsuarioOVIPAPDao {

    private JdbcTemplate jdbcTemplate;
    private final Logger logger = Logger.getLogger(ComunicacionUsuarioOVIPAPDao.class.getName());

    public static final String TABLE_NAME = "comunicacionusuarioovipap";
    
    public static final String GET_COMUNICACION_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE id_comu = ?";
    public static final String GET_COMUNICACIONES_BY_SELECCION = "SELECT * FROM " + TABLE_NAME + " WHERE id_seleccion = ?";
    public static final String GET_COMUNICACIONES_BY_EMISOR = "SELECT * FROM " + TABLE_NAME + " WHERE emisor = ?";
    public static final String GET_COMUNICACIONES_BY_MEDIO = "SELECT * FROM " + TABLE_NAME + " WHERE medio = ?";
    public static final String ADD_COMUNICACION = "INSERT INTO " + TABLE_NAME + " (id_seleccion, emisor, mensaje, medio) VALUES (?, ?, ?, ?)";
    public static final String DELETE_COMUNICACION = "DELETE FROM " + TABLE_NAME + " WHERE id_comu = ?";
    public static final String UPDATE_COMUNICACION = "UPDATE " + TABLE_NAME + " SET id_seleccion = ?, emisor = ?, mensaje = ?, medio = ? WHERE id_comu = ?";
    public static final String GET_COMUNICACIONES = "SELECT * FROM " + TABLE_NAME;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public ComunicacionUsuarioOVIPAP getComunicacion(int id) {
        try {
            return jdbcTemplate.queryForObject(GET_COMUNICACION_BY_ID, new ComunicacionUsuarioOVIPAPRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontró la comunicación con id: " + id);
            return null;
        }
    }

    public List<ComunicacionUsuarioOVIPAP> getComunicacionesBySeleccion(int idSeleccion) {
        try {
            return jdbcTemplate.query(GET_COMUNICACIONES_BY_SELECCION, new ComunicacionUsuarioOVIPAPRowMapper(), idSeleccion);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron comunicaciones para la selección: " + idSeleccion);
            return new ArrayList<>();
        }
    }

    public List<ComunicacionUsuarioOVIPAP> getComunicacionesByEmisor(String emisor) {
        try {
            return jdbcTemplate.query(GET_COMUNICACIONES_BY_EMISOR, new ComunicacionUsuarioOVIPAPRowMapper(), emisor);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron comunicaciones del emisor: " + emisor);
            return new ArrayList<>();
        }
    }

    public List<ComunicacionUsuarioOVIPAP> getComunicacionesByMedio(String medio) {
        try {
            return jdbcTemplate.query(GET_COMUNICACIONES_BY_MEDIO, new ComunicacionUsuarioOVIPAPRowMapper(), medio);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron comunicaciones por medio: " + medio);
            return new ArrayList<>();
        }
    }

    public void addComunicacion(ComunicacionUsuarioOVIPAP comunicacion) {
        jdbcTemplate.update(ADD_COMUNICACION, comunicacion.getIdSeleccion(), comunicacion.getEmisor(), 
                comunicacion.getMensaje(), comunicacion.getMedio());
    }

    public void updateComunicacion(ComunicacionUsuarioOVIPAP comunicacion) {
        jdbcTemplate.update(UPDATE_COMUNICACION, comunicacion.getIdSeleccion(), comunicacion.getEmisor(), 
                comunicacion.getMensaje(), comunicacion.getMedio(), comunicacion.getIdComu());
    }

    public void deleteComunicacion(int id) {
        jdbcTemplate.update(DELETE_COMUNICACION, id);
    }

    public List<ComunicacionUsuarioOVIPAP> getComunicaciones() {
        try {
            return jdbcTemplate.query(GET_COMUNICACIONES, new ComunicacionUsuarioOVIPAPRowMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron comunicaciones.");
            return new ArrayList<>();
        }
    }
}
