package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.Period;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Persona {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
    @Column(nullable = false)
    protected String nombre;
    
    @Column(nullable = false)
    protected String apellido;
    
    @Column(unique = true, nullable = false)
    protected String dni;
    
    protected void validarDni(String dni) {
        if (!dni.matches("\\d{7,8}")) {
            throw new IllegalArgumentException("DNI inválido: debe tener 7 u 8 dígitos");
        }
    }
    
    @Column(name = "fecha_nacimiento", nullable = false)
    protected LocalDate fechaNacimiento;
    
    protected String telefono;
    
    protected String direccion;
    
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
    
    public int getEdad() {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
    
    public boolean esMayorDeEdad() {
        return getEdad() >= 18;
    }
}