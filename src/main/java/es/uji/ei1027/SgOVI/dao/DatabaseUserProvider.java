package es.uji.ei1027.SgOVI.dao;

import es.uji.ei1027.SgOVI.model.*;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class DatabaseUserProvider implements UserDao {

    private static final Logger logger = Logger.getLogger(DatabaseUserProvider.class.getName());

    @Autowired
    private UsuarioOVIDao usuarioOVIDao;

    @Autowired
    private AsistentePersonalDao asistentePersonalDao;

    @Autowired
    private FormadorDao formadorDao;

    @Autowired
    private TecnicoOVIDao tecnicoOVIDao;

    @Override
    public UserDetails loadUserByUsername(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();

        UsuarioOVI usuarioOVI = usuarioOVIDao.getUsuarioByEmail(username.trim());
        if (usuarioOVI != null && checkPasswordSafe(passwordEncryptor, password, usuarioOVI.getContrasena())) {
            UserDetails safeUser = new UserDetails();
            safeUser.setUsername(usuarioOVI.getEmail());
            return safeUser;
        }

        AsistentePersonal asistente = asistentePersonalDao.getAsistenteByEmail(username.trim());
        if (asistente != null && checkPasswordSafe(passwordEncryptor, password, asistente.getContrasena())) {
            UserDetails safeUser = new UserDetails();
            safeUser.setUsername(asistente.getEmail());
            return safeUser;
        }

        Formador formador = formadorDao.getFormadorByEmail(username.trim());
        if (formador != null && checkPasswordSafe(passwordEncryptor, password, formador.getContrasena())) {
            UserDetails safeUser = new UserDetails();
            safeUser.setUsername(formador.getEmail());
            return safeUser;
        }

        TecnicoOVI tecnico = tecnicoOVIDao.getTecnicoByEmail(username.trim());
        if (tecnico != null && password.equals(tecnico.getContrasena())) {
            UserDetails safeUser = new UserDetails();
            safeUser.setUsername(tecnico.getEmail());
            return safeUser;
        }

        return null;
    }

    private boolean checkPasswordSafe(BasicPasswordEncryptor encryptor, String rawPassword, String storedPassword) {
        try {
            return encryptor.checkPassword(rawPassword, storedPassword);
        } catch (EncryptionOperationNotPossibleException e) {
            logger.warning("Password hash format not recognized for a user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Collection<UserDetails> listAllUsers() {
        List<UserDetails> users = new ArrayList<>();

        for (UsuarioOVI usuarioOVI : usuarioOVIDao.getUsuarios()) {
            UserDetails user = new UserDetails();
            user.setUsername(usuarioOVI.getEmail());
            users.add(user);
        }

        for (AsistentePersonal asistente : asistentePersonalDao.getAsistentes()) {
            UserDetails user = new UserDetails();
            user.setUsername(asistente.getEmail());
            users.add(user);
        }

        for (Formador formador : formadorDao.getFormadores()) {
            UserDetails user = new UserDetails();
            user.setUsername(formador.getEmail());
            users.add(user);
        }

        return users;
    }
}
