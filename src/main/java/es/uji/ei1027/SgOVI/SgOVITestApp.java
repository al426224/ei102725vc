package es.uji.ei1027.SgOVI;

import es.uji.ei1027.SgOVI.dao.UsuarioOVIDao;
import es.uji.ei1027.SgOVI.model.UsuarioOVI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
@Configuration
public class SgOVITestApp {

    public static void main(String[] args) {
        SpringApplication.run(SgOVITestApp.class, args);
    }

    @Bean
    public CommandLineRunner testRunner(UsuarioOVIDao usuarioOVIDao) {
        return args -> {
            Scanner scanner = new Scanner(System.in);
            System.out.println("\n=== TESTEADOR DE DAO - UsuarioOVIDao ===");
            System.out.println("Conectado a la base de datos.\n");

            while (true) {
                System.out.println("1. Listar todos los usuarios");
                System.out.println("2. Buscar usuario por ID");
                System.out.println("3. Buscar usuario por Email");
                System.out.println("4. Buscar usuario por DNI");
                System.out.println("5. Añadir usuario");
                System.out.println("6. Actualizar usuario");
                System.out.println("7. Eliminar usuario");
                System.out.println("8. Listar usuarios por estado");
                System.out.println("0. Salir");
                System.out.print("\nOpción: ");

                int opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1 -> listarUsuarios(usuarioOVIDao);
                    case 2 -> buscarPorId(usuarioOVIDao, scanner);
                    case 3 -> buscarPorEmail(usuarioOVIDao, scanner);
                    case 4 -> buscarPorDni(usuarioOVIDao, scanner);
                    case 5 -> añadirUsuario(usuarioOVIDao, scanner);
                    case 6 -> actualizarUsuario(usuarioOVIDao, scanner);
                    case 7 -> eliminarUsuario(usuarioOVIDao, scanner);
                    case 8 -> listarPorEstado(usuarioOVIDao, scanner);
                    case 0 -> {
                        System.out.println("¡Hasta luego!");
                        return;
                    }
                    default -> System.out.println("Opción inválida");
                }
            }
        };
    }

    private void listarUsuarios(UsuarioOVIDao dao) {
        System.out.println("\n--- Listar Usuarios ---");
        List<UsuarioOVI> usuarios = dao.getUsuarios();
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios.");
        } else {
            usuarios.forEach(u -> System.out.println(u.getIdUsuario() + " | " + u.getNombre() + " | " + u.getEmail() + " | " + u.getEstado()));
        }
        System.out.println();
    }

    private void buscarPorId(UsuarioOVIDao dao, Scanner scanner) {
        System.out.print("ID de usuario: ");
        String id = scanner.nextLine();
        UsuarioOVI usuario = dao.getUsuario(id);
        System.out.println(usuario != null ? usuario : "No encontrado.\n");
    }

    private void buscarPorEmail(UsuarioOVIDao dao, Scanner scanner) {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        UsuarioOVI usuario = dao.getUsuarioByEmail(email);
        System.out.println(usuario != null ? usuario : "No encontrado.\n");
    }

    private void buscarPorDni(UsuarioOVIDao dao, Scanner scanner) {
        System.out.print("DNI: ");
        String dni = scanner.nextLine();
        UsuarioOVI usuario = dao.getUsuarioByDni(dni);
        System.out.println(usuario != null ? usuario : "No encontrado.\n");
    }

    private void añadirUsuario(UsuarioOVIDao dao, Scanner scanner) {
        System.out.println("\n--- Añadir Usuario ---");
        UsuarioOVI usuario = new UsuarioOVI();
        System.out.print("ID: "); usuario.setIdUsuario(scanner.nextLine());
        System.out.print("Nombre: "); usuario.setNombre(scanner.nextLine());
        System.out.print("Email: "); usuario.setEmail(scanner.nextLine());
        System.out.print("Teléfono: "); usuario.setTelefono(scanner.nextLine());
        System.out.print("DNI: "); usuario.setDni(scanner.nextLine());
        System.out.print("Proyecto vida: "); usuario.setProyectoVida(scanner.nextLine());
        System.out.print("Estado: "); usuario.setEstado(scanner.nextLine());
        System.out.print("Consentimiento LOPD (true/false): ");
        String consentimiento = scanner.nextLine().trim().toLowerCase();
        usuario.setConsentimientoLOPD(consentimiento.equals("true"));
        usuario.setFechaNacimiento(LocalDate.now());
        usuario.setFechaRegistro(LocalDate.now());

        dao.addUsuario(usuario);
        System.out.println("Usuario añadido.\n");
    }

    private void actualizarUsuario(UsuarioOVIDao dao, Scanner scanner) {
        System.out.print("ID del usuario a actualizar: ");
        String id = scanner.nextLine();
        UsuarioOVI usuario = dao.getUsuario(id);

        if (usuario == null) {
            System.out.println("Usuario no encontrado.\n");
            return;
        }

        System.out.println("Usuario actual: " + usuario);
        System.out.println("\n--- Actualizar Usuario (deja vacío para mantener) ---");
        System.out.print("Nombre [" + usuario.getNombre() + "]: ");
        String input = scanner.nextLine();
        if (!input.isEmpty()) usuario.setNombre(input);
        System.out.print("Email [" + usuario.getEmail() + "]: ");
        input = scanner.nextLine();
        if (!input.isEmpty()) usuario.setEmail(input);
        System.out.print("Teléfono [" + usuario.getTelefono() + "]: ");
        input = scanner.nextLine();
        if (!input.isEmpty()) usuario.setTelefono(input);
        System.out.print("Estado [" + usuario.getEstado() + "]: ");
        input = scanner.nextLine();
        if (!input.isEmpty()) usuario.setEstado(input);

        dao.updateUsuario(usuario);
        System.out.println("Usuario actualizado.\n");
    }

    private void eliminarUsuario(UsuarioOVIDao dao, Scanner scanner) {
        System.out.print("ID del usuario a eliminar: ");
        String id = scanner.nextLine();
        dao.deleteUsuario(id);
        System.out.println("Usuario eliminado.\n");
    }

    private void listarPorEstado(UsuarioOVIDao dao, Scanner scanner) {
        System.out.print("Estado (activo/inactivo/pendiente): ");
        String estado = scanner.nextLine();
        List<UsuarioOVI> usuarios = dao.getUsuariosByEstado(estado);
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios con ese estado.");
        } else {
            usuarios.forEach(u -> System.out.println(u.getIdUsuario() + " | " + u.getNombre() + " | " + u.getEmail()));
        }
        System.out.println();
    }
}
