package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "citas")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cita {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EstadoCita estado = EstadoCita.PROGRAMADA;
    
    @Column(columnDefinition = "TEXT")
    private String observaciones;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal costo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_id", nullable = false)
    private Sala sala;
    
    public void iniciar() {
        if (this.estado == EstadoCita.PROGRAMADA) {
            this.estado = EstadoCita.EN_CURSO;
        }
    }
    
    public void completar(String observacionesFinal) {
        if (this.estado == EstadoCita.EN_CURSO) {
            this.estado = EstadoCita.COMPLETADA;
            this.observaciones = observacionesFinal;
        }
    }
    
    public void cancelar(String motivo) {
        if (this.estado == EstadoCita.PROGRAMADA || this.estado == EstadoCita.EN_CURSO) {
            this.estado = EstadoCita.CANCELADA;
            this.observaciones = "CANCELADA: " + motivo;
        }
    }
    
    public void reprogramar(LocalDateTime nuevaFecha) {
        if (this.estado == EstadoCita.PROGRAMADA) {
            this.fechaHora = nuevaFecha;
            this.estado = EstadoCita.REPROGRAMADA;
        }
    }
    
    public boolean estaEnCurso() {
        return this.estado == EstadoCita.EN_CURSO;
    }
    
    public boolean estaCompletada() {
        return this.estado == EstadoCita.COMPLETADA;
    }
    
    public boolean estaCancelada() {
        return this.estado == EstadoCita.CANCELADA;
    }
    
    public String getResumenCita() {
        return String.format("Cita %d - %s con Dr. %s el %s - Estado: %s",
            id,
            paciente.getNombreCompleto(),
            medico.getNombreCompleto(),
            fechaHora.toString(),
            estado.getDescripcion()
        );
    }
}