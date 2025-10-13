package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "historias_clinicas")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoriaClinica {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "numero_historia")
    private String numeroHistoria;
    
    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;
    
    @ElementCollection
    @CollectionTable(name = "diagnosticos", joinColumns = @JoinColumn(name = "historia_id"))
    @Column(name = "diagnostico")
    @Builder.Default
    private List<String> diagnosticos = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(name = "tratamientos", joinColumns = @JoinColumn(name = "historia_id"))
    @Column(name = "tratamiento")
    @Builder.Default
    private List<String> tratamientos = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(name = "alergias", joinColumns = @JoinColumn(name = "historia_id"))
    @Column(name = "alergia")
    @Builder.Default
    private List<String> alergias = new ArrayList<>();
    
    @OneToOne(mappedBy = "historiaClinica")
    private Paciente paciente;
    
    public void agregarDiagnostico(String diagnostico) {
        if (diagnostico != null && !diagnostico.trim().isEmpty()) {
            this.diagnosticos.add(diagnostico.trim());
        }
    }
    
    public void agregarTratamiento(String tratamiento) {
        if (tratamiento != null && !tratamiento.trim().isEmpty()) {
            this.tratamientos.add(tratamiento.trim());
        }
    }
    
    public void agregarAlergia(String alergia) {
        if (alergia != null && !alergia.trim().isEmpty()) {
            this.alergias.add(alergia.trim());
        }
    }
    
    public boolean tieneAlergia(String alergia) {
        return alergias.contains(alergia);
    }
    
    public int getTotalDiagnosticos() {
        return diagnosticos.size();
    }
}