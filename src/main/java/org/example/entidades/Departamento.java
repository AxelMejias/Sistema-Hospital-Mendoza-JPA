package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departamentos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Departamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EspecialidadMedica especialidad;
    
    @Column
    private String descripcion;
    
    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Medico> medicos = new ArrayList<>();
    
    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Sala> salas = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;
    
    public void agregarMedico(Medico medico) {
        this.medicos.add(medico);
        medico.setDepartamento(this);
    }
    
    public void removerMedico(Medico medico) {
        this.medicos.remove(medico);
        medico.setDepartamento(null);
    }
    
    public Sala crearSala(String numero, String descripcion) {
        Sala sala = Sala.builder()
            .numero(numero)
            .descripcion(descripcion)
            .departamento(this)
            .build();
        this.salas.add(sala);
        return sala;
    }
    
    public void agregarSala(Sala sala) {
        this.salas.add(sala);
        sala.setDepartamento(this);
    }
    
    public void removerSala(Sala sala) {
        this.salas.remove(sala);
        sala.setDepartamento(null);
    }
    
    public List<Sala> getSalasDisponibles() {
        return salas.stream()
                .filter(Sala::getEstaDisponible)
                .toList();
    }
    
    public List<Medico> getMedicosPorEspecialidad(EspecialidadMedica especialidadBuscada) {
        return medicos.stream()
                .filter(medico -> medico.getEspecialidad() == especialidadBuscada)
                .toList();
    }
    
    public int getTotalMedicos() {
        return medicos.size();
    }
    
    public int getTotalSalas() {
        return salas.size();
    }
}