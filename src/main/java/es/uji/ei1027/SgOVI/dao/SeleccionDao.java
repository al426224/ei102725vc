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

    public static final String TABLE_NAME = "seleccion";
    
    public static final String GET_SELECCION_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE id_seleccion = ?";
    public static final String GET_SELECCIONES_BY_SOLICITUD = "SELECT * FROM " + TABLE_NAME + " WHERE id_solicitud = ? ORDER BY puntuacion_match DESC";
    public static final String GET_SELECCIONES_BY_ASISTENTE = "SELECT * FROM " + TABLE_NAME + " WHERE id_asistente = ?";
    public static final String GET_SELECCIONES_BY_ESTADO = "SELECT * FROM " + TABLE_NAME + " WHERE estado_seleccion = ?";
    public static final String GET_SELECCIONES_BY_SOLICITUD_ESTADO = "SELECT * FROM " + TABLE_NAME + " WHERE id_solicitud = ? AND estado_seleccion = ? ORDER BY puntuacion_match DESC";
    public static final String ADD_SELECCION = "INSERT INTO " + TABLE_NAME + " (id_solicitud, id_asistente, estado_seleccion, puntuacion_match) VALUES (?, ?, ?, ?)";
    public static final String DELETE_SELECCION = "DELETE FROM " + TABLE_NAME + " WHERE id_seleccion = ?";
    public static final String DELETE_SELECCIONES_BY_SOLICITUD = "DELETE FROM " + TABLE_NAME + " WHERE id_solicitud = ? AND estado_seleccion = 'propuesta'";
    public static final String UPDATE_SELECCION = "UPDATE " + TABLE_NAME + " SET id_solicitud = ?, id_asistente = ?, estado_seleccion = ?, puntuacion_match = ? WHERE id_seleccion = ?";
    public static final String GET_SELECCIONES = "SELECT * FROM " + TABLE_NAME;
    public static final String EXISTS_SELECCION = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE id_solicitud = ? AND estado_seleccion = 'propuesta'";
    public static final String GET_SELECCION_ACEPTADA = "SELECT * FROM " + TABLE_NAME + " WHERE id_solicitud = ? AND estado_seleccion = 'aceptada' LIMIT 1";
    public static final String GET_SELECCION_BY_SOLICITUD_ASISTENTE = "SELECT * FROM " + TABLE_NAME + " WHERE id_solicitud = ? AND id_asistente = ? LIMIT 1";
    public static final String GET_SELECCIONES_BY_ASISTENTE_NO_RECHAZADA = "SELECT s.* FROM " + TABLE_NAME + " s " +
            "JOIN peticionapr p ON s.id_solicitud = p.id_solicitud " +
            "WHERE s.id_asistente = ? AND s.estado_seleccion != 'rechazada' AND p.estado = 'aprobada' " +
            "ORDER BY s.estado_seleccion, s.puntuacion_match DESC";

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

    public List<Seleccion> getSeleccionesBySolicitud(int idSolicitud) {
        try {
            return jdbcTemplate.query(GET_SELECCIONES_BY_SOLICITUD, new SeleccionRowMapper(), idSolicitud);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron selecciones para la solicitud: " + idSolicitud);
            return new ArrayList<>();
        }
    }

    public List<Seleccion> getSeleccionesByAsistente(int idAsistente) {
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

    public List<Seleccion> getSeleccionesBySolicitudAndEstado(int idSolicitud, String estado) {
        try {
            return jdbcTemplate.query(GET_SELECCIONES_BY_SOLICITUD_ESTADO, new SeleccionRowMapper(), idSolicitud, estado);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public boolean hasPropuestasForSolicitud(int idSolicitud) {
        Integer count = jdbcTemplate.queryForObject(EXISTS_SELECCION, Integer.class, idSolicitud);
        return count != null && count > 0;
    }

    public void guardarCandidatosSugeridos(int idSolicitud, List<Seleccion> candidatos) {
        jdbcTemplate.update(DELETE_SELECCIONES_BY_SOLICITUD, idSolicitud);
        for (Seleccion s : candidatos) {
            jdbcTemplate.update(ADD_SELECCION, s.getIdSolicitud(), s.getIdAsistente(), s.getEstadoSeleccion(), s.getPuntuacionMatch());
        }
    }

public List<Seleccion> getSeleccionesByAsistenteNoRechazada(int idAsistente) {
        try {
            return jdbcTemplate.query(GET_SELECCIONES_BY_ASISTENTE_NO_RECHAZADA, new SeleccionRowMapper(), idAsistente);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public Seleccion getSeleccionAceptadaPorSolicitud(int idSolicitud) {
        try {
            return jdbcTemplate.queryForObject(GET_SELECCION_ACEPTADA, new SeleccionRowMapper(), idSolicitud);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Seleccion getSeleccionBySolicitudYAsistente(int idSolicitud, int idAsistente) {
        try {
            return jdbcTemplate.queryForObject(GET_SELECCION_BY_SOLICITUD_ASISTENTE, new SeleccionRowMapper(), idSolicitud, idAsistente);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
