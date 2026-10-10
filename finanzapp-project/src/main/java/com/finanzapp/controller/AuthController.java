package com.finanzapp.controller;

import com.finanzapp.model.Usuario;
import com.finanzapp.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    // ---------- LOGIN ----------

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String email,
                                 @RequestParam String password,
                                 HttpSession session,
                                 Model model) {

        Optional<Usuario> usuarioOpt = usuarioService.validarLogin(email, password);

        if (usuarioOpt.isEmpty()) {
            model.addAttribute("error", "Usuario o contraseña incorrectos.");
            model.addAttribute("email", email);
            return "login";
        }

        Usuario usuario = usuarioOpt.get();
        session.setAttribute("usuarioId", usuario.getId());
        session.setAttribute("usuarioNombre", usuario.getNombreCompleto());
        session.setAttribute("usuarioRol", usuario.getRol());

        if ("ADMIN".equals(usuario.getRol())) {
            return "redirect:/admin/usuarios";
        }
        return "redirect:/inicio";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // ---------- REGISTRO ----------

    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@RequestParam String nombres,
                                    @RequestParam String apellidos,
                                    @RequestParam String cedula,
                                    @RequestParam String fechaNacimiento,
                                    @RequestParam String email,
                                    @RequestParam String password,
                                    Model model) {

        // Reincorporar valores al formulario en caso de error
        model.addAttribute("nombres", nombres);
        model.addAttribute("apellidos", apellidos);
        model.addAttribute("cedula", cedula);
        model.addAttribute("fechaNacimiento", fechaNacimiento);
        model.addAttribute("email", email);

        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaNacimiento); // formato yyyy-MM-dd (input type=date)
        } catch (DateTimeParseException ex) {
            model.addAttribute("errorFecha", "*FECHA ERRÓNEA");
            return "error";
        }

        if (fecha.isAfter(LocalDate.now().minusYears(18))) {
            model.addAttribute("errorFecha", "*DEBE SER MAYOR DE EDAD");
            return "error";
        }

        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            model.addAttribute("errorEmail", "*CORREO INVALIDO");
            return "error";
        }

        Usuario usuario = new Usuario();
        usuario.setNombres(nombres);
        usuario.setApellidos(apellidos);
        usuario.setCedula(cedula);
        usuario.setFechaNacimiento(fecha);
        usuario.setEmail(email);
        usuario.setPassword(password);

        try {
            usuarioService.registrar(usuario);
        } catch (IllegalArgumentException ex) {
            if ("email".equals(ex.getMessage())) {
                model.addAttribute("errorEmail", "*CORREO YA REGISTRADO");
            } else {
                model.addAttribute("errorFecha", "*CÉDULA YA REGISTRADA");
            }
            return "error";
        }

        return "redirect:/login?registrado=true";
    }
}
