package es.uji.ei1027.SgOVI.dao;

import es.uji.ei1027.SgOVI.model.ComunicacionUsuarioOVIPAP;
import es.uji.ei1027.SgOVI.rowMapper.ComunicacionUsuarioOVIPAPRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class ComunicacionUsuarioOVIPAPDao {

    private JdbcTemplate jdbcTemplate;
    private final Logger logger = Logger.getLogger(ComunicacionUsuarioOVIPAPDao.class.getName());

    private static final String TABLE_NAME = "comunicacionusuarioovipap";
    
    private static final String GET_COMUNICACION_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE id_comu = ?";
    private static final String GET_COMUNICACIONES_BY_SELECCION = "SELECT * FROM " + TABLE_NAME + " WHERE id_seleccion = ? ORDER BY hora ASC, id_comu ASC";
    private static final String GET_COMUNICACIONES_BY_EMISOR = "SELECT * FROM " + TABLE_NAME + " WHERE emisor = ?";
    private static final String GET_ULTIMA_COMUNICACION_BY_SELECCION = "SELECT * FROM " + TABLE_NAME + " WHERE id_seleccion = ? ORDER BY hora DESC, id_comu DESC LIMIT 1";
    private static final String ADD_COMUNICACION = "INSERT INTO " + TABLE_NAME + " (id_seleccion, emisor, mensaje) VALUES (?, ?, ?)";
    private static final String DELETE_COMUNICACION = "DELETE FROM " + TABLE_NAME + " WHERE id_comu = ?";
    private static final String UPDATE_COMUNICACION = "UPDATE " + TABLE_NAME + " SET id_seleccion = ?, emisor = ?, mensaje = ? WHERE id_comu = ?";
    private static final String GET_COMUNICACIONES = "SELECT * FROM " + TABLE_NAME;

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
        return jdbcTemplate.query(GET_COMUNICACIONES_BY_SELECCION, new ComunicacionUsuarioOVIPAPRowMapper(), idSeleccion);
    }

    public List<ComunicacionUsuarioOVIPAP> getComunicacionesByEmisor(String emisor) {
        return jdbcTemplate.query(GET_COMUNICACIONES_BY_EMISOR, new ComunicacionUsuarioOVIPAPRowMapper(), emisor);
    }

    public void addComunicacion(ComunicacionUsuarioOVIPAP comunicacion) {
        jdbcTemplate.update(ADD_COMUNICACION, comunicacion.getIdSeleccion(), comunicacion.getEmisor(),
                comunicacion.getMensaje());
    }

    public void updateComunicacion(ComunicacionUsuarioOVIPAP comunicacion) {
        jdbcTemplate.update(UPDATE_COMUNICACION, comunicacion.getIdSeleccion(), comunicacion.getEmisor(),
                comunicacion.getMensaje(), comunicacion.getIdComu());
    }

    public void deleteComunicacion(int id) {
        jdbcTemplate.update(DELETE_COMUNICACION, id);
    }

    public List<ComunicacionUsuarioOVIPAP> getComunicaciones() {
        return jdbcTemplate.query(GET_COMUNICACIONES, new ComunicacionUsuarioOVIPAPRowMapper());
    }

    public ComunicacionUsuarioOVIPAP getUltimaComunicacionBySeleccion(int idSeleccion) {
        try {
            return jdbcTemplate.queryForObject(GET_ULTIMA_COMUNICACION_BY_SELECCION,
                    new ComunicacionUsuarioOVIPAPRowMapper(), idSeleccion);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
