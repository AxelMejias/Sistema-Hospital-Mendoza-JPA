package org.example.servicio;

import org.example.entidades.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CitaManager {
    
    private List<Cita> citas = new ArrayList<>();
    
    public Cita programarCita(Paciente paciente, Medico medico, Sala sala, 
                             LocalDateTime fechaHora, BigDecimal costo) throws CitaException {
        
        validarDisponibilidad(medico, sala, fechaHora);
        
        Cita cita = Cita.builder()
            .paciente(paciente)
            .medico(medico)
            .sala(sala)
            .fechaHora(fechaHora)
            .costo(costo)
            .estado(EstadoCita.PROGRAMADA)
            .build();
        
        citas.add(cita);
        paciente.programarCita(cita);
        medico.asignarCita(cita);
        
        return cita;
    }
    
    public void cancelarCita(Long citaId, String motivo) throws CitaException {
        Cita cita = buscarCitaPorId(citaId);
        if (cita == null) {
            throw new CitaException("Cita no encontrada con ID: " + citaId);
        }
        
        cita.cancelar(motivo);
    }
    
    public void completarCita(Long citaId, String observaciones) throws CitaException {
        Cita cita = buscarCitaPorId(citaId);
        if (cita == null) {
            throw new CitaException("Cita no encontrada con ID: " + citaId);
        }
        
        if (cita.getEstado() != EstadoCita.EN_CURSO) {
            throw new CitaException("Solo se pueden completar citas en curso");
        }
        
        cita.completar(observaciones);
    }
    
    public void iniciarCita(Long citaId) throws CitaException {
        Cita cita = buscarCitaPorId(citaId);
        if (cita == null) {
            throw new CitaException("Cita no encontrada con ID: " + citaId);
        }
        
        if (cita.getEstado() != EstadoCita.PROGRAMADA) {
            throw new CitaException("Solo se pueden iniciar citas programadas");
        }
        
        cita.iniciar();
    }
    
    public List<Cita> getCitasPorMedico(Medico medico) {
        return citas.stream()
            .filter(cita -> cita.getMedico().equals(medico))
            .collect(Collectors.toList());
    }
    
    public List<Cita> getCitasPorPaciente(Paciente paciente) {
        return citas.stream()
            .filter(cita -> cita.getPaciente().equals(paciente))
            .collect(Collectors.toList());
    }
    
    public List<Cita> getCitasPorEstado(EstadoCita estado) {
        return citas.stream()
            .filter(cita -> cita.getEstado() == estado)
            .collect(Collectors.toList());
    }
    
    public List<Cita> getTodasLasCitas() {
        return new ArrayList<>(citas);
    }
    
    private void validarDisponibilidad(Medico medico, Sala sala, LocalDateTime fechaHora) 
            throws CitaException {
        
        if (!sala.getEstaDisponible()) {
            throw new CitaException("La sala no está disponible");
        }
        
        boolean medicoOcupado = citas.stream()
            .filter(c -> c.getEstado() == EstadoCita.PROGRAMADA || c.getEstado() == EstadoCita.EN_CURSO)
            .anyMatch(c -> c.getMedico().equals(medico) && 
                          c.getFechaHora().isEqual(fechaHora));
        
        if (medicoOcupado) {
            throw new CitaException("El médico ya tiene una cita programada en ese horario");
        }
        
        boolean salaOcupada = citas.stream()
            .filter(c -> c.getEstado() == EstadoCita.PROGRAMADA || c.getEstado() == EstadoCita.EN_CURSO)
            .anyMatch(c -> c.getSala().equals(sala) && 
                          c.getFechaHora().isEqual(fechaHora));
        
        if (salaOcupada) {
            throw new CitaException("La sala ya está ocupada en ese horario");
        }
    }
    
    private Cita buscarCitaPorId(Long citaId) {
        return citas.stream()
            .filter(cita -> cita.getId().equals(citaId))
            .findFirst()
            .orElse(null);
    }
}