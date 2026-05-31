package es.uji.ei1027.SgOVI.services;

import es.uji.ei1027.SgOVI.dao.PeticionAPRDao;
import es.uji.ei1027.SgOVI.dao.RegistroContactoDao;
import es.uji.ei1027.SgOVI.dao.SeleccionDao;
import es.uji.ei1027.SgOVI.model.PeticionAPR;
import es.uji.ei1027.SgOVI.model.RegistroContacto;
import es.uji.ei1027.SgOVI.model.Seleccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Logger;

@Service
public class ContratoFinalizacionService {

    private final RegistroContactoDao registroContactoDao;
    private final SeleccionDao seleccionDao;
    private final PeticionAPRDao peticionAPRDao;
    private final Logger logger = Logger.getLogger(ContratoFinalizacionService.class.getName());

    @Autowired
    public ContratoFinalizacionService(RegistroContactoDao registroContactoDao,
                                        SeleccionDao seleccionDao,
                                        PeticionAPRDao peticionAPRDao) {
        this.registroContactoDao = registroContactoDao;
        this.seleccionDao = seleccionDao;
        this.peticionAPRDao = peticionAPRDao;
    }

    @Transactional
    @Scheduled(cron = "0 0 6 * * *",  zone = "Europe/Madrid")
    public void finalizarContratosVencidos() {
        List<RegistroContacto> vencidos = registroContactoDao.getContratosVencidos();

        logger.info("Contratos vencidos encontrados: " + vencidos.size());

        for (RegistroContacto rc : vencidos) {
            rc.setResultado("Finalizado");
            registroContactoDao.updateRegistro(rc);

            try {
                Seleccion sel = seleccionDao.getSeleccion(rc.getIdSeleccion());
                if (sel != null) {
                    PeticionAPR peticion = peticionAPRDao.getPeticion(sel.getIdSolicitud());
                    if (peticion != null && "cerrada_contrato".equals(peticion.getEstado())) {
                        peticion.setEstado("cerrada_contrato_finalizado");
                        peticionAPRDao.updatePeticion(peticion);
                    }
                }
            } catch (Exception e) {
                logger.warning("Error al finalizar peticion para contrato " + rc.getIdReg() + ": " + e.getMessage());
            }
        }
    }

    public int contarVencidos() {
        return registroContactoDao.getContratosVencidos().size();
    }
}
