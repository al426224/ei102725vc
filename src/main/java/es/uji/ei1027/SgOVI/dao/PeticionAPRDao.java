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

    public static final String GET_PETICION_BY_ID = "SELECT * FROM PeticionAPR WHERE id_solicitud = ?";
    public static final String GET_PETICIONES_BY_USUARIO = "SELECT * FROM PeticionAPR WHERE id_usuario = ?";
    public static final String GET_PETICIONES_BY_ESTADO = "SELECT * FROM PeticionAPR WHERE estado = ?";
    public static final String GET_PETICIONES_BY_TIPO = "SELECT * FROM PeticionAPR WHERE tipo_asistencia = ?";
    public static final String ADD_PETICION = "INSERT INTO PeticionAPR (id_solicitud, id_usuario, tipo_asistencia, descripcion, horas_semanales, estado) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String DELETE_PETICION = "DELETE FROM PeticionAPR WHERE id_solicitud = ?";
    public static final String UPDATE_PETICION = "UPDATE PeticionAPR SET id_usuario = ?, tipo_asistencia = ?, descripcion = ?, horas_semanales = ?, estado = ? WHERE id_solicitud = ?";
    public static final String GET_PETICIONES = "SELECT * FROM PeticionAPR";

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public PeticionAPR getPeticion(String id) {
        try {
            return jdbcTemplate.queryForObject(GET_PETICION_BY_ID, new PeticionAPRRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontró la petición con id: " + id);
            return null;
        }
    }

    public List<PeticionAPR> getPeticionesByUsuario(String idUsuario) {
        try {
            return jdbcTemplate.query(GET_PETICIONES_BY_USUARIO, new PeticionAPRRowMapper(), idUsuario);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron peticiones para el usuario: " + idUsuario);
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
        jdbcTemplate.update(ADD_PETICION, peticion.getIdSolicitud(), peticion.getIdUsuario(), 
                peticion.getTipoAsistencia(), peticion.getDescripcion(), peticion.getHorasSemanales(), 
                peticion.getEstado());
    }

    public void updatePeticion(PeticionAPR peticion) {
        jdbcTemplate.update(UPDATE_PETICION, peticion.getIdUsuario(), peticion.getTipoAsistencia(), 
                peticion.getDescripcion(), peticion.getHorasSemanales(), peticion.getEstado(), 
                peticion.getIdSolicitud());
    }

    public void deletePeticion(String id) {
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
}
