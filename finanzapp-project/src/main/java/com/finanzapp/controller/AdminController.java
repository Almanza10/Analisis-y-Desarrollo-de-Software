package com.finanzapp.controller;

import com.finanzapp.model.Usuario;
import com.finanzapp.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    /** Verifica que haya una sesión activa con rol ADMIN. */
    private boolean noEsAdmin(HttpSession session) {
        Object rol = session.getAttribute("usuarioRol");
        return rol == null || !"ADMIN".equals(rol);
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(HttpSession session, Model model) {
        if (noEsAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("adminNombre", session.getAttribute("usuarioNombre"));
        return "admin";
    }

    @GetMapping("/usuarios/{id}/editar")
    public String mostrarEdicion(@PathVariable Long id, HttpSession session, Model model) {
        if (noEsAdmin(session)) {
            return "redirect:/login";
        }
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(id);
        if (usuarioOpt.isEmpty()) {
            return "redirect:/admin/usuarios";
        }
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("adminNombre", session.getAttribute("usuarioNombre"));
        model.addAttribute("editando", usuarioOpt.get());
        return "admin";
    }

    @PostMapping("/usuarios/{id}/editar")
    public String procesarEdicion(@PathVariable Long id,
                                   @RequestParam String nombres,
                                   @RequestParam String apellidos,
                                   @RequestParam String cedula,
                                   @RequestParam String email,
                                   @RequestParam String rol,
                                   @RequestParam(required = false) String fechaNacimiento,
                                   HttpSession session) {
        if (noEsAdmin(session)) {
            return "redirect:/login";
        }
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setNombres(nombres);
            usuario.setApellidos(apellidos);
            usuario.setCedula(cedula);
            usuario.setEmail(email);
            usuario.setRol(rol);
            if (fechaNacimiento != null && !fechaNacimiento.isBlank()) {
                usuario.setFechaNacimiento(LocalDate.parse(fechaNacimiento));
            }
            usuarioService.actualizar(usuario);
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/usuarios/{id}/eliminar")
    public String eliminarUsuario(@PathVariable Long id, HttpSession session) {
        if (noEsAdmin(session)) {
            return "redirect:/login";
        }
        usuarioService.eliminar(id);
        return "redirect:/admin/usuarios";
    }
}
