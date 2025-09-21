package com.biblioteca.traza1.entidades;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

public class Sucursal {
    private Integer id; // para buscar/actualizar por ID
    private String nombre;
    private LocalTime horarioApertura;
    private LocalTime horarioCierre;
    private boolean esCasaMatriz;
    private Domicilio domicilio; // relación 1:1 Sucursal -> Domicilio
}
