package com.finanzapp.empleados.repository;

import com.finanzapp.empleados.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    Optional<Empleado> findByNumeroIdentificacion(String numeroIdentificacion);
    Optional<Empleado> findByNumeroEmpleado(String numeroEmpleado);
    List<Empleado> findAllByOrderByIdDesc();
}
