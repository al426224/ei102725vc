package es.uji.ei1027.SgOVI.dao;

import es.uji.ei1027.SgOVI.model.RegistroContacto;
import es.uji.ei1027.SgOVI.rowMapper.RegistroContactoRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class RegistroContactoDao {

    private JdbcTemplate jdbcTemplate;
    private final Logger logger = Logger.getLogger(RegistroContactoDao.class.getName());

    public static final String GET_REGISTRO_BY_ID = "SELECT * FROM RegistroContacto WHERE id_reg = ?";
    public static final String GET_REGISTROS_BY_SELECCION = "SELECT * FROM RegistroContacto WHERE id_seleccion = ?";
    public static final String GET_REGISTROS_BY_TIPO = "SELECT * FROM RegistroContacto WHERE tipo_contacto = ?";
    public static final String GET_REGISTROS_BY_RESULTADO = "SELECT * FROM RegistroContacto WHERE resultado = ?";
    public static final String ADD_REGISTRO = "INSERT INTO RegistroContacto (id_seleccion, tipo_contacto, observaciones, resultado) VALUES (?, ?, ?, ?)";
    public static final String DELETE_REGISTRO = "DELETE FROM RegistroContacto WHERE id_reg = ?";
    public static final String UPDATE_REGISTRO = "UPDATE RegistroContacto SET id_seleccion = ?, tipo_contacto = ?, observaciones = ?, resultado = ? WHERE id_reg = ?";
    public static final String GET_REGISTROS = "SELECT * FROM RegistroContacto";

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public RegistroContacto getRegistro(int id) {
        try {
            return jdbcTemplate.queryForObject(GET_REGISTRO_BY_ID, new RegistroContactoRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontró el registro con id: " + id);
            return null;
        }
    }

    public List<RegistroContacto> getRegistrosBySeleccion(int idSeleccion) {
        try {
            return jdbcTemplate.query(GET_REGISTROS_BY_SELECCION, new RegistroContactoRowMapper(), idSeleccion);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron registros para la selección: " + idSeleccion);
            return new ArrayList<>();
        }
    }

    public List<RegistroContacto> getRegistrosByTipo(String tipoContacto) {
        try {
            return jdbcTemplate.query(GET_REGISTROS_BY_TIPO, new RegistroContactoRowMapper(), tipoContacto);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron registros de tipo: " + tipoContacto);
            return new ArrayList<>();
        }
    }

    public List<RegistroContacto> getRegistrosByResultado(String resultado) {
        try {
            return jdbcTemplate.query(GET_REGISTROS_BY_RESULTADO, new RegistroContactoRowMapper(), resultado);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron registros con resultado: " + resultado);
            return new ArrayList<>();
        }
    }

    public void addRegistro(RegistroContacto registro) {
        jdbcTemplate.update(ADD_REGISTRO, registro.getIdSeleccion(), registro.getTipoContacto(), 
                registro.getObservaciones(), registro.getResultado());
    }

    public void updateRegistro(RegistroContacto registro) {
        jdbcTemplate.update(UPDATE_REGISTRO, registro.getIdSeleccion(), registro.getTipoContacto(), 
                registro.getObservaciones(), registro.getResultado(), registro.getIdReg());
    }

    public void deleteRegistro(int id) {
        jdbcTemplate.update(DELETE_REGISTRO, id);
    }

    public List<RegistroContacto> getRegistros() {
        try {
            return jdbcTemplate.query(GET_REGISTROS, new RegistroContactoRowMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron registros.");
            return new ArrayList<>();
        }
    }
}
