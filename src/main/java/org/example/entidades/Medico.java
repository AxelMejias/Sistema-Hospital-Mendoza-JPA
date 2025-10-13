package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medicos")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Medico extends Persona {
    
    @Column(name = "numero_matricula", unique = true, nullable = false)
    private String numeroMatricula;
    
    private void validarMatricula(String numeroMatricula) {
        if (!numeroMatricula.matches("MP-\\d{4,6}")) {
            throw new IllegalArgumentException("Formato de matrícula inválido: debe ser MP-XXXX");
        }
    }
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EspecialidadMedica especialidad;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "numero", column = @Column(name = "matricula_numero")),
        @AttributeOverride(name = "fechaVencimiento", column = @Column(name = "matricula_vencimiento")),
        @AttributeOverride(name = "consejoProfesional", column = @Column(name = "matricula_consejo"))
    })
    private Matricula matricula;
    
    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Cita> citas = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;
    
    public void asignarCita(Cita cita) {
        this.citas.add(cita);
        cita.setMedico(this);
    }
    
    public void removerCita(Cita cita) {
        this.citas.remove(cita);
        cita.setMedico(null);
    }
    
    public int getTotalCitas() {
        return citas.size();
    }
    
    public List<Cita> getCitasPorEstado(EstadoCita estado) {
        return citas.stream()
                .filter(cita -> cita.getEstado() == estado)
                .toList();
    }
    
    public boolean tieneMatriculaVigente() {
        return matricula != null && matricula.isVigente();
    }
}