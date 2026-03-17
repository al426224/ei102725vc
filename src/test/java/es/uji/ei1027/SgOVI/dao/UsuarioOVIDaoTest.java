package es.uji.ei1027.SgOVI.dao;


import es.uji.ei1027.SgOVI.model.UsuarioOVI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UsuarioOVIDaoTest {

    @Autowired
    private UsuarioOVIDao usuarioOVIDao;

    @Test
    public void testGetUsuarios() {
        List<UsuarioOVI> usuarios = usuarioOVIDao.getUsuarios();
        assertNotNull(usuarios);
        System.out.println("Usuarios encontrados: " + usuarios.size());
    }

    @Test
    public void testGetUsuarioById() {
        UsuarioOVI usuario = usuarioOVIDao.getUsuario("USR001");
        if (usuario != null) {
            assertNotNull(usuario.getNombre());
            System.out.println("Usuario encontrado: " + usuario.getNombre());
        } else {
            System.out.println("No se encontró usuario con id USR001");
        }
    }

    @Test
    public void testGetUsuarioByEmail() {
        UsuarioOVI usuario = usuarioOVIDao.getUsuarioByEmail("test@test.com");
        if (usuario != null) {
            assertEquals("test@test.com", usuario.getEmail());
            System.out.println("Usuario encontrado por email: " + usuario.getNombre());
        } else {
            System.out.println("No se encontró usuario con email test@test.com");
        }
    }

    @Test
    public void testAddAndDeleteUsuario() {
        String testEmail = "test" + System.currentTimeMillis() + "@test.com";
        
        UsuarioOVI usuario = new UsuarioOVI();
        usuario.setIdUsuario("TEST" + System.currentTimeMillis());
        usuario.setNombre("Usuario Test");
        usuario.setEmail(testEmail);
        usuario.setTelefono("123456789");
        usuario.setConsentimientoLOPD(true);
        usuario.setDni("12345678A");
        usuario.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        usuario.setProyectoVida("Proyecto test");
        usuario.setEstado("pendiente");

        usuarioOVIDao.addUsuario(usuario);
        System.out.println("Usuario añadido: " + usuario.getIdUsuario());

        UsuarioOVI usuarioRecuperado = usuarioOVIDao.getUsuarioByEmail(testEmail);
        assertNotNull(usuarioRecuperado);
        assertEquals("Usuario Test", usuarioRecuperado.getNombre());
        System.out.println("Usuario recuperado: " + usuarioRecuperado.getNombre());

        usuarioOVIDao.deleteUsuario(usuarioRecuperado.getIdUsuario());
        System.out.println("Usuario eliminado");

        UsuarioOVI usuarioEliminado = usuarioOVIDao.getUsuario(usuarioRecuperado.getIdUsuario());
        assertNull(usuarioEliminado);
    }

    @Test
    public void testGetUsuariosByEstado() {
        List<UsuarioOVI> usuarios = usuarioOVIDao.getUsuariosByEstado("pendiente");
        assertNotNull(usuarios);
        System.out.println("Usuarios pendientes: " + usuarios.size());
    }
}
