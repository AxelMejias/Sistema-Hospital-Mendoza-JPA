package org.example.entidades;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Matricula {
    
    private String numero;
    private LocalDate fechaVencimiento;
    private String consejoProfesional;
    
    public boolean isVigente() {
        return fechaVencimiento.isAfter(LocalDate.now());
    }
    
    public long diasParaVencimiento() {
        return LocalDate.now().until(fechaVencimiento).getDays();
    }
}