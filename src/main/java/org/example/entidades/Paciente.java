package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pacientes")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Paciente extends Persona {
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_sangre", nullable = false)
    private TipoSangre tipoSangre;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "historia_clinica_id")
    @Builder.Default
    private HistoriaClinica historiaClinica = new HistoriaClinica();
    
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Cita> citas = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;
    
    @PostLoad
    @PostPersist
    private void initializeHistoriaClinica() {
        if (this.historiaClinica == null) {
            this.historiaClinica = HistoriaClinica.builder()
                .numeroHistoria("HC-" + this.id)
                .fechaCreacion(java.time.LocalDate.now())
                .build();
        }
    }
    
    public void programarCita(Cita cita) {
        this.citas.add(cita);
        cita.setPaciente(this);
    }
    
    public void cancelarCita(Cita cita) {
        this.citas.remove(cita);
        cita.setPaciente(null);
    }
    
    public int getTotalCitas() {
        return citas.size();
    }
    
    public List<Cita> getCitasPorEstado(EstadoCita estado) {
        return citas.stream()
                .filter(cita -> cita.getEstado() == estado)
                .toList();
    }
    
    public boolean tieneAlergias() {
        return historiaClinica != null && !historiaClinica.getAlergias().isEmpty();
    }
    
    public boolean esCompatibleConSangre(TipoSangre otroTipo) {
        return tipoSangre == otroTipo || 
               tipoSangre == TipoSangre.O_NEGATIVO ||
               (tipoSangre == TipoSangre.AB_POSITIVO);
    }
}