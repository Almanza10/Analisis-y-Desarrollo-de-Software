package com.finanzapp.empleados.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "empleados")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 80, message = "Máximo 80 caracteres")
    @Column(nullable = false, length = 80)
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 80, message = "Máximo 80 caracteres")
    @Column(nullable = false, length = 80)
    private String apellidos;

    @NotBlank(message = "El número de identificación es obligatorio")
    @Pattern(regexp = "\\d{5,15}", message = "Debe tener solo números (entre 5 y 15 dígitos)")
    @Column(nullable = false, unique = true, length = 15)
    private String numeroIdentificacion;

    @NotBlank(message = "El número de empleado es obligatorio")
    @Pattern(regexp = "[A-Za-z0-9-]{3,20}", message = "Use de 3 a 20 letras, números o guiones")
    @Column(nullable = false, unique = true, length = 20)
    private String numeroEmpleado;

    @Size(max = 80)
    private String cargo;

    @Size(max = 80)
    private String departamento;

    @Email(message = "Correo no válido")
    @Size(max = 120)
    private String correo;

    @Size(max = 30)
    private String telefono;

    private LocalDate fechaIngreso;

    @Size(max = 20)
    @Column(nullable = false, length = 20)
    private String estatus = "Activo";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getNumeroIdentificacion() { return numeroIdentificacion; }
    public void setNumeroIdentificacion(String numeroIdentificacion) { this.numeroIdentificacion = numeroIdentificacion; }
    public String getNumeroEmpleado() { return numeroEmpleado; }
    public void setNumeroEmpleado(String numeroEmpleado) { this.numeroEmpleado = numeroEmpleado; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }
}
