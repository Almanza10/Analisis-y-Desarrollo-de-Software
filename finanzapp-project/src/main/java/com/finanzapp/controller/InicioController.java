package com.finanzapp.controller;

import com.finanzapp.model.Usuario;
import com.finanzapp.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class InicioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/inicio")
    public String inicio(HttpSession session, Model model) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return "redirect:/login";
        }

        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(usuarioId);
        if (usuarioOpt.isEmpty()) {
            session.invalidate();
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuarioOpt.get());
        return "inicio";
    }
}
