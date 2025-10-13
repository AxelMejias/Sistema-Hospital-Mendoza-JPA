package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "salas")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sala {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String numero;
    
    @Column(nullable = false)
    private String descripcion;
    
    @Column(name = "capacidad_maxima")
    @Builder.Default
    private Integer capacidadMaxima = 1;
    
    @Column(name = "esta_disponible")
    @Builder.Default
    private Boolean estaDisponible = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;
    
    public void ocupar() {
        this.estaDisponible = false;
    }
    
    public void liberar() {
        this.estaDisponible = true;
    }
    
    public boolean puedeAcomodar(int personas) {
        return estaDisponible && personas <= capacidadMaxima;
    }
    
    public String getIdentificacion() {
        return numero + " - " + descripcion;
    }
}