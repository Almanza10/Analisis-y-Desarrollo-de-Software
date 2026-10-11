package com.finanzapp.empleados.service;

import com.finanzapp.empleados.exception.DuplicadoException;
import com.finanzapp.empleados.exception.RecursoNoEncontradoException;
import com.finanzapp.empleados.model.Empleado;
import com.finanzapp.empleados.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmpleadoService {

    private final EmpleadoRepository repo;

    public EmpleadoService(EmpleadoRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Empleado> listar() {
        return repo.findAllByOrderByIdDesc();
    }

    @Transactional(readOnly = true)
    public Empleado obtener(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("No existe un empleado con id " + id));
    }

    public Empleado crear(Empleado datos) {
        normalizar(datos);
        validarDuplicados(datos, null);
        datos.setId(null);
        return repo.save(datos);
    }

    public Empleado actualizar(Long id, Empleado datos) {
        Empleado actual = obtener(id);
        normalizar(datos);
        validarDuplicados(datos, id);
        actual.setNombres(datos.getNombres());
        actual.setApellidos(datos.getApellidos());
        actual.setNumeroIdentificacion(datos.getNumeroIdentificacion());
        actual.setNumeroEmpleado(datos.getNumeroEmpleado());
        actual.setCargo(datos.getCargo());
        actual.setDepartamento(datos.getDepartamento());
        actual.setCorreo(datos.getCorreo());
        actual.setTelefono(datos.getTelefono());
        actual.setFechaIngreso(datos.getFechaIngreso());
        actual.setEstatus(datos.getEstatus());
        return repo.save(actual);
    }

    public void eliminar(Long id) {
        repo.delete(obtener(id));
    }

    // ---- utilidades ----

    private void validarDuplicados(Empleado e, Long idActual) {
        repo.findByNumeroIdentificacion(e.getNumeroIdentificacion())
                .filter(otro -> !otro.getId().equals(idActual))
                .ifPresent(otro -> {
                    throw new DuplicadoException("Ya existe un empleado con ese número de identificación");
                });
        repo.findByNumeroEmpleado(e.getNumeroEmpleado())
                .filter(otro -> !otro.getId().equals(idActual))
                .ifPresent(otro -> {
                    throw new DuplicadoException("Ya existe un empleado con ese número de empleado");
                });
    }

    private void normalizar(Empleado e) {
        e.setNombres(limpiar(e.getNombres()));
        e.setApellidos(limpiar(e.getApellidos()));
        e.setNumeroIdentificacion(limpiar(e.getNumeroIdentificacion()));
        String numEmp = limpiar(e.getNumeroEmpleado());
        e.setNumeroEmpleado(numEmp == null ? null : numEmp.toUpperCase());
        e.setCargo(limpiar(e.getCargo()));
        e.setDepartamento(limpiar(e.getDepartamento()));
        e.setCorreo(limpiar(e.getCorreo()));
        e.setTelefono(limpiar(e.getTelefono()));
        String estatus = limpiar(e.getEstatus());
        e.setEstatus(estatus == null ? "Activo" : estatus);
    }

    private String limpiar(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}
