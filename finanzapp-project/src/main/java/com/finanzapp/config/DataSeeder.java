package com.finanzapp.config;

import com.finanzapp.model.Usuario;
import com.finanzapp.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Crea un usuario administrador por defecto la primera vez que se levanta
 * la aplicación, para poder entrar al panel /admin/usuarios sin tener que
 * insertar datos manualmente en MySQL.
 *
 * Credenciales por defecto:
 *   email:    admin@finanzapp.com
 *   password: admin123
 *
 * IMPORTANTE: cámbiala o elimínala antes de usar en producción.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;

    public DataSeeder(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) {
        if (!usuarioRepository.existsByEmail("admin@finanzapp.com")) {
            Usuario admin = new Usuario();
            admin.setNombres("Administrador");
            admin.setApellidos("FinanzApp");
            admin.setCedula("0000000000");
            admin.setFechaNacimiento(LocalDate.of(1990, 1, 1));
            admin.setEmail("admin@finanzapp.com");
            admin.setPassword("admin123");
            admin.setRol("ADMIN");
            usuarioRepository.save(admin);
        }
    }
}
