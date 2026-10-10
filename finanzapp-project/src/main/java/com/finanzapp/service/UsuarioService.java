package com.finanzapp.service;

import com.finanzapp.model.Usuario;
import com.finanzapp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Registra un nuevo usuario. Lanza IllegalArgumentException con un mensaje
     * de error legible si el email o la cédula ya existen.
     */
    public Usuario registrar(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("email");
        }
        if (usuarioRepository.existsByCedula(usuario.getCedula())) {
            throw new IllegalArgumentException("cedula");
        }
        usuario.setRol("USER");
        return usuarioRepository.save(usuario);
    }

    /**
     * Valida credenciales de login. Devuelve el Usuario si son correctas,
     * Optional.empty() si no.
     */
    public Optional<Usuario> validarLogin(String email, String password) {
        return usuarioRepository.findByEmail(email)
                .filter(u -> u.getPassword().equals(password));
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario actualizar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }
}
