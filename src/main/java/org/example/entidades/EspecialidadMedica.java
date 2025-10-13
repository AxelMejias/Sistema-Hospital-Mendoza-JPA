package org.example.entidades;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EspecialidadMedica {
    CARDIOLOGIA("Cardiologia"),
    PEDIATRIA("Pediatria"),
    TRAUMATOLOGIA("Traumatologia"),
    NEUROLOGIA("Neurologia"),
    DERMATOLOGIA("Dermatologia"),
    ONCOLOGIA("Oncologia"),
    PSIQUIATRIA("Psiquiatria"),
    GINECOLOGIA("Ginecologia"),
    UROLOGIA("Urologia"),
    OFTALMOLOGIA("Oftalmologia");

    private final String descripcion;
}