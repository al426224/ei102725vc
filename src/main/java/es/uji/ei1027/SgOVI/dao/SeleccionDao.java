package es.uji.ei1027.SgOVI.dao;

import es.uji.ei1027.SgOVI.model.Seleccion;
import es.uji.ei1027.SgOVI.rowMapper.SeleccionRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class SeleccionDao {

    private JdbcTemplate jdbcTemplate;
    private final Logger logger = Logger.getLogger(SeleccionDao.class.getName());

    public static final String GET_SELECCION_BY_ID = "SELECT * FROM Seleccion WHERE id_seleccion = ?";
    public static final String GET_SELECCIONES_BY_SOLICITUD = "SELECT * FROM Seleccion WHERE id_solicitud = ?";
    public static final String GET_SELECCIONES_BY_ASISTENTE = "SELECT * FROM Seleccion WHERE id_asistente = ?";
    public static final String GET_SELECCIONES_BY_ESTADO = "SELECT * FROM Seleccion WHERE estado_seleccion = ?";
    public static final String ADD_SELECCION = "INSERT INTO Seleccion (id_solicitud, id_asistente, estado_seleccion, puntuacion_match) VALUES (?, ?, ?, ?)";
    public static final String DELETE_SELECCION = "DELETE FROM Seleccion WHERE id_seleccion = ?";
    public static final String UPDATE_SELECCION = "UPDATE Seleccion SET id_solicitud = ?, id_asistente = ?, estado_seleccion = ?, puntuacion_match = ? WHERE id_seleccion = ?";
    public static final String GET_SELECCIONES = "SELECT * FROM Seleccion";

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Seleccion getSeleccion(int id) {
        try {
            return jdbcTemplate.queryForObject(GET_SELECCION_BY_ID, new SeleccionRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontró la selección con id: " + id);
            return null;
        }
    }

    public List<Seleccion> getSeleccionesBySolicitud(String idSolicitud) {
        try {
            return jdbcTemplate.query(GET_SELECCIONES_BY_SOLICITUD, new SeleccionRowMapper(), idSolicitud);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron selecciones para la solicitud: " + idSolicitud);
            return new ArrayList<>();
        }
    }

    public List<Seleccion> getSeleccionesByAsistente(String idAsistente) {
        try {
            return jdbcTemplate.query(GET_SELECCIONES_BY_ASISTENTE, new SeleccionRowMapper(), idAsistente);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron selecciones para el asistente: " + idAsistente);
            return new ArrayList<>();
        }
    }

    public List<Seleccion> getSeleccionesByEstado(String estado) {
        try {
            return jdbcTemplate.query(GET_SELECCIONES_BY_ESTADO, new SeleccionRowMapper(), estado);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron selecciones con estado: " + estado);
            return new ArrayList<>();
        }
    }

    public void addSeleccion(Seleccion seleccion) {
        jdbcTemplate.update(ADD_SELECCION, seleccion.getIdSolicitud(), seleccion.getIdAsistente(), 
                seleccion.getEstadoSeleccion(), seleccion.getPuntuacionMatch());
    }

    public void updateSeleccion(Seleccion seleccion) {
        jdbcTemplate.update(UPDATE_SELECCION, seleccion.getIdSolicitud(), seleccion.getIdAsistente(), 
                seleccion.getEstadoSeleccion(), seleccion.getPuntuacionMatch(), seleccion.getIdSeleccion());
    }

    public void deleteSeleccion(int id) {
        jdbcTemplate.update(DELETE_SELECCION, id);
    }

    public List<Seleccion> getSelecciones() {
        try {
            return jdbcTemplate.query(GET_SELECCIONES, new SeleccionRowMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron selecciones.");
            return new ArrayList<>();
        }
    }
}
