package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "hospitales")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hospital {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String direccion;
    
    @Column
    private String telefono;
    
    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Departamento> departamentos = new ArrayList<>();
    
    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Paciente> pacientes = new ArrayList<>();
    
    public void agregarDepartamento(Departamento departamento) {
        this.departamentos.add(departamento);
        departamento.setHospital(this);
    }
    
    public void removerDepartamento(Departamento departamento) {
        this.departamentos.remove(departamento);
        departamento.setHospital(null);
    }
    
    public void agregarPaciente(Paciente paciente) {
        this.pacientes.add(paciente);
        paciente.setHospital(this);
    }
    
    public void removerPaciente(Paciente paciente) {
        this.pacientes.remove(paciente);
        paciente.setHospital(null);
    }
    
    public List<Departamento> getDepartamentosPorEspecialidad(EspecialidadMedica especialidad) {
        return departamentos.stream()
                .filter(dept -> dept.getEspecialidad() == especialidad)
                .collect(Collectors.toList());
    }
    
    public List<Medico> getTodosLosMedicos() {
        return departamentos.stream()
                .flatMap(dept -> dept.getMedicos().stream())
                .collect(Collectors.toList());
    }
    
    public List<Sala> getTodasLasSalas() {
        return departamentos.stream()
                .flatMap(dept -> dept.getSalas().stream())
                .collect(Collectors.toList());
    }
    
    public List<Medico> getMedicosPorEspecialidad(EspecialidadMedica especialidad) {
        return getTodosLosMedicos().stream()
                .filter(medico -> medico.getEspecialidad() == especialidad)
                .collect(Collectors.toList());
    }
    
    public List<Paciente> getPacientesPorTipoSangre(TipoSangre tipoSangre) {
        return pacientes.stream()
                .filter(paciente -> paciente.getTipoSangre() == tipoSangre)
                .collect(Collectors.toList());
    }
    
    public int getTotalMedicos() {
        return getTodosLosMedicos().size();
    }
    
    public int getTotalPacientes() {
        return pacientes.size();
    }
    
    public int getTotalSalas() {
        return getTodasLasSalas().size();
    }
    
    public int getTotalDepartamentos() {
        return departamentos.size();
    }
    
    public String getEstadisticasGenerales() {
        return String.format("Hospital %s - Departamentos: %d, Médicos: %d, Pacientes: %d, Salas: %d",
            nombre, getTotalDepartamentos(), getTotalMedicos(), getTotalPacientes(), getTotalSalas());
    }
}