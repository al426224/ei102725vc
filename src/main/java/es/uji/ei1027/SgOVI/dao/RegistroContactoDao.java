package es.uji.ei1027.SgOVI.dao;

import es.uji.ei1027.SgOVI.model.RegistroContacto;
import es.uji.ei1027.SgOVI.rowMapper.RegistroContactoRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class RegistroContactoDao {

    private JdbcTemplate jdbcTemplate;
    private final Logger logger = Logger.getLogger(RegistroContactoDao.class.getName());

    private static final String TABLE_NAME = "registrocontrato";
    
    private static final String GET_REGISTRO_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE id_reg = ?";
    private static final String GET_REGISTROS_BY_SELECCION = "SELECT * FROM " + TABLE_NAME + " WHERE id_seleccion = ?";
    private static final String GET_REGISTROS_BY_RESULTADO = "SELECT * FROM " + TABLE_NAME + " WHERE resultado = ?";
    private static final String ADD_REGISTRO = "INSERT INTO " + TABLE_NAME + " (id_seleccion, tipo_contrato, observaciones, resultado, fecha_inicio, fecha_fin, ruta_pdf, pdf_data) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String DELETE_REGISTRO = "DELETE FROM " + TABLE_NAME + " WHERE id_reg = ?";
    private static final String UPDATE_REGISTRO = "UPDATE " + TABLE_NAME + " SET tipo_contrato = ?, observaciones = ?, resultado = ?, fecha_fin = ?, ruta_pdf = ?, pdf_data = ? WHERE id_reg = ?";
    private static final String GET_REGISTROS = "SELECT * FROM " + TABLE_NAME;
    private static final String GET_REGISTROS_BY_USUARIO = "SELECT rc.* FROM " + TABLE_NAME + " rc JOIN seleccion s ON rc.id_seleccion = s.id_seleccion JOIN peticionapr p ON s.id_solicitud = p.id_solicitud WHERE p.id_usuario = ? ORDER BY rc.fecha_inicio DESC";
    private static final String GET_REGISTROS_BY_ASISTENTE = "SELECT rc.* FROM " + TABLE_NAME + " rc JOIN seleccion s ON rc.id_seleccion = s.id_seleccion WHERE s.id_asistente = ? ORDER BY rc.fecha_inicio DESC";
    private static final String GET_CONTRATOS_ABIERTOS_BY_ASISTENTE = "SELECT rc.* FROM " + TABLE_NAME + " rc JOIN seleccion s ON rc.id_seleccion = s.id_seleccion WHERE s.id_asistente = ? AND (rc.resultado IS NULL OR (rc.resultado != 'finalizado' AND rc.resultado != 'cancelado')) ORDER BY rc.fecha_inicio DESC";

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

    public List<RegistroContacto> getRegistrosByResultado(String resultado) {
        try {
            return jdbcTemplate.query(GET_REGISTROS_BY_RESULTADO, new RegistroContactoRowMapper(), resultado);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron registros con resultado: " + resultado);
            return new ArrayList<>();
        }
    }

    public void addRegistro(RegistroContacto registro) {
        jdbcTemplate.update(ADD_REGISTRO, registro.getIdSeleccion(), registro.getTipoContrato(), 
                registro.getObservaciones(), registro.getResultado(), registro.getFechaInicio(), 
                registro.getFechaFin(), registro.getRutaPdf(),
                new SqlParameterValue(Types.BINARY, registro.getPdfData()));
    }

    public void updateRegistro(RegistroContacto registro) {
        jdbcTemplate.update(UPDATE_REGISTRO, registro.getTipoContrato(), 
                registro.getObservaciones(), registro.getResultado(), registro.getFechaFin(),
                registro.getRutaPdf(),
                new SqlParameterValue(Types.BINARY, registro.getPdfData()),
                registro.getIdReg());
    }

    public void deleteRegistro(int id) {
        jdbcTemplate.update(DELETE_REGISTRO, id);
    }

    public List<RegistroContacto> getRegistrosByUsuarioOVI(int idUsuario) {
        try {
            return jdbcTemplate.query(GET_REGISTROS_BY_USUARIO, new RegistroContactoRowMapper(), idUsuario);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron registros para el usuario: " + idUsuario);
            return new ArrayList<>();
        }
    }

    public List<RegistroContacto> getRegistrosByAsistente(int idAsistente) {
        try {
            return jdbcTemplate.query(GET_REGISTROS_BY_ASISTENTE, new RegistroContactoRowMapper(), idAsistente);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron registros para el asistente: " + idAsistente);
            return new ArrayList<>();
        }
    }

    public List<RegistroContacto> getContratosAbiertosByAsistente(int idAsistente) {
        try {
            return jdbcTemplate.query(GET_CONTRATOS_ABIERTOS_BY_ASISTENTE, new RegistroContactoRowMapper(), idAsistente);
        } catch (EmptyResultDataAccessException e) {
            logger.warning("No se encontraron contratos abiertos para el asistente: " + idAsistente);
            return new ArrayList<>();
        }
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
