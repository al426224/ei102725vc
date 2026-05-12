package es.uji.ei1027.SgOVI.dao;

import es.uji.ei1027.SgOVI.model.*;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class DatabaseUserProvider implements UserDao {

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
        if (usuarioOVI != null) {
            if (passwordEncryptor.checkPassword(password, usuarioOVI.getContrasena())) {
                UserDetails safeUser = new UserDetails();
                safeUser.setUsername(usuarioOVI.getEmail());
                return safeUser;
            }
        }

        AsistentePersonal asistente = asistentePersonalDao.getAsistenteByEmail(username.trim());
        if (asistente != null) {
            if (passwordEncryptor.checkPassword(password, asistente.getContrasena())) {
                UserDetails safeUser = new UserDetails();
                safeUser.setUsername(asistente.getEmail());
                return safeUser;
            }
        }

        Formador formador = formadorDao.getFormadorByEmail(username.trim());
        if (formador != null) {
            if (passwordEncryptor.checkPassword(password, formador.getContrasena())) {
                UserDetails safeUser = new UserDetails();
                safeUser.setUsername(formador.getEmail());
                return safeUser;
            }
        }

        TecnicoOVI tecnico = tecnicoOVIDao.getTecnicoByEmail(username.trim());
        if (tecnico != null) {
            if (passwordEncryptor.checkPassword(password, tecnico.getContrasena())) {
                UserDetails safeUser = new UserDetails();
                safeUser.setUsername(tecnico.getEmail());
                return safeUser;
            }
        }

        return null;
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
