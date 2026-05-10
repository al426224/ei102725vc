package es.uji.ei1027.SgOVI.dao;

import es.uji.ei1027.SgOVI.model.PeticionAPR;
import es.uji.ei1027.SgOVI.rowMapper.PeticionAPRRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class PeticionAPRDao {

    private JdbcTemplate jdbcTemplate;
    private final Logger logger = Logger.getLogger(PeticionAPRDao.class.getName());

    public static final String TABLE_NAME = "peticionapr";

    public static final String GET_PETICION_BY_ID = "SELECT p.*, u.nombre AS nombre_usuario FROM " + TABLE_NAME + " p LEFT JOIN usuariovi u ON p.id_usuario = u.id_usuario WHERE p.id_solicitud = ?";
    public static final String GET_PETICIONES_BY_USUARIO = "SELECT p.*, u.nombre AS nombre_usuario FROM " + TABLE_NAME + " p LEFT JOIN usuariovi u ON p.id_usuario = u.id_usuario WHERE p.id_usuario = ? ORDER BY p.id_solicitud DESC";
    public static final String GET_PETICIONES_BY_USUARIO_FILTRADO = "SELECT p.*, u.nombre AS nombre_usuario FROM " + TABLE_NAME + " p LEFT JOIN usuariovi u ON p.id_usuario = u.id_usuario WHERE p.id_usuario = ?";
    public static final String GET_PETICIONES_BY_ESTADO = "SELECT p.*, u.nombre AS nombre_usuario FROM " + TABLE_NAME + " p LEFT JOIN usuariovi u ON p.id_usuario = u.id_usuario WHERE p.estado = ?";
    public static final String GET_PETICIONES_BY_TIPO = "SELECT p.*, u.nombre AS nombre_usuario FROM " + TABLE_NAME + " p LEFT JOIN usuariovi u ON p.id_usuario = u.id_usuario WHERE p.tipo_asistencia = ?";
    public static final String ADD_PETICION = "INSERT INTO " + TABLE_NAME + " (id_usuario, tipo_asistencia, descripcion, horas_semanales, estado, tiempo_preferido, tipo_tareas, municipio, fecha_inicio_prevista, preferencia_genero, preferencias, idiomas_requeridos) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String DELETE_PETICION = "DELETE FROM " + TABLE_NAME + " WHERE id_solicitud = ?";
    public static final String UPDATE_PETICION = "UPDATE " + TABLE_NAME + " SET id_usuario = ?, tipo_asistencia = ?, descripcion = ?, horas_semanales = ?, estado = ?, tiempo_preferido = ?, tipo_tareas = ?, municipio = ?, fecha_inicio_prevista = ?, preferencia_genero = ?, preferencias = ?, idiomas_requeridos = ?, observaciones_tecnico = ?, motivo_rechazo = ? WHERE id_solicitud = ?";
    public static final String GET_PETICIONES = "SELECT p.*, u.nombre AS nombre_usuario FROM " + TABLE_NAME + " p LEFT JOIN usuariovi u ON p.id_usuario = u.id_usuario";
    public static final String GET_PETICIONES_BY_USUARIO_AND_ESTADO = "SELECT p.*, u.nombre AS nombre_usuario FROM " + TABLE_NAME + " p LEFT JOIN usuariovi u ON p.id_usuario = u.id_usuario WHERE p.id_usuario = ? AND p.estado = ?";
    public static final String GET_PETICION_WITH_USER = "SELECT p.*, u.nombre AS nombre_usuario FROM " + TABLE_NAME + " p LEFT JOIN usuariovi u ON p.id_usuario = u.id_usuario WHERE p.id_solicitud = ?";

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public PeticionAPR getPeticion(int id) {
        try {
            return jdbcTemplate.queryForObject(GET_PETICION_BY_ID, new PeticionAPRRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontro la peticion con id: " + id);
            return null;
        }
    }

    public List<PeticionAPR> getPeticionesByUsuario(int idUsuario) {
        try {
            return jdbcTemplate.query(GET_PETICIONES_BY_USUARIO, new PeticionAPRRowMapper(), idUsuario);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron peticiones para el usuario: " + idUsuario);
            return new ArrayList<>();
        }
    }

    public List<PeticionAPR> getPeticionesByUsuarioFiltrado(int idUsuario, String estado, String ordenar) {
        String sql = GET_PETICIONES_BY_USUARIO_FILTRADO;
        List<Object> params = new ArrayList<>();
        params.add(idUsuario);

        if (estado != null && !estado.isEmpty()) {
            sql += " AND estado = ?";
            params.add(estado);
        }

        if ("fechaInicio".equals(ordenar)) {
            sql += " ORDER BY p.fecha_inicio_prevista ASC NULLS LAST";
        } else {
            sql += " ORDER BY p.id_solicitud DESC";
        }

        try {
            return jdbcTemplate.query(sql, new PeticionAPRRowMapper(), params.toArray());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public List<PeticionAPR> getPeticionesByEstado(String estado) {
        try {
            return jdbcTemplate.query(GET_PETICIONES_BY_ESTADO, new PeticionAPRRowMapper(), estado);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron peticiones con estado: " + estado);
            return new ArrayList<>();
        }
    }

    public List<PeticionAPR> getPeticionesByTipo(String tipoAsistencia) {
        try {
            return jdbcTemplate.query(GET_PETICIONES_BY_TIPO, new PeticionAPRRowMapper(), tipoAsistencia);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron peticiones de tipo: " + tipoAsistencia);
            return new ArrayList<>();
        }
    }

    public void addPeticion(PeticionAPR peticion) {
        jdbcTemplate.update(ADD_PETICION,
                peticion.getIdUsuario(),
                peticion.getTipoAsistencia(),
                peticion.getDescripcion(),
                peticion.getHorasSemanales(),
                peticion.getEstado(),
                peticion.getTiempoPreferido(),
                peticion.getTipoTareas(),
                peticion.getMunicipio(),
                peticion.getFechaInicioPrevista(),
                peticion.getPreferenciaGenero(),
                peticion.getPreferencias(),
                peticion.getIdiomasRequeridos());
    }

    public void updatePeticion(PeticionAPR peticion) {
        jdbcTemplate.update(UPDATE_PETICION,
                peticion.getIdUsuario(),
                peticion.getTipoAsistencia(),
                peticion.getDescripcion(),
                peticion.getHorasSemanales(),
                peticion.getEstado(),
                peticion.getTiempoPreferido(),
                peticion.getTipoTareas(),
                peticion.getMunicipio(),
                peticion.getFechaInicioPrevista(),
                peticion.getPreferenciaGenero(),
                peticion.getPreferencias(),
                peticion.getIdiomasRequeridos(),
                peticion.getObservacionesTecnico(),
                peticion.getMotivoRechazo(),
                peticion.getIdSolicitud());
    }

    public PeticionAPR getPeticionWithUser(int id) {
        try {
            return jdbcTemplate.queryForObject(GET_PETICION_WITH_USER, new PeticionAPRRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontro la peticion con id: " + id);
            return null;
        }
    }

    public void deletePeticion(int id) {
        jdbcTemplate.update(DELETE_PETICION, id);
    }

    public List<PeticionAPR> getPeticiones() {
        try {
            return jdbcTemplate.query(GET_PETICIONES, new PeticionAPRRowMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron peticiones.");
            return new ArrayList<>();
        }
    }

    public List<PeticionAPR> getPeticionesByUsuarioAndEstado(int idUsuario, String estado) {
        try {
            return jdbcTemplate.query(GET_PETICIONES_BY_USUARIO_AND_ESTADO, new PeticionAPRRowMapper(), idUsuario, estado);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron peticiones para el usuario con estado: " + estado);
            return new ArrayList<>();
        }
    }

    public List<PeticionAPR> getPeticionesByEstadoFiltrado(String estado) {
        if (estado == null || estado.isEmpty()) {
            return getPeticiones();
        }
        try {
            return jdbcTemplate.query(GET_PETICIONES_BY_ESTADO, new PeticionAPRRowMapper(), estado);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron peticiones con estado: " + estado);
            return new ArrayList<>();
        }
    }
}