package com.biblioteca.traza1.entidades;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(exclude = "provincia")

public class Localidad {
    private Integer id;
    private String nombre;
    private Provincia provincia; // 1 Provincia -> n Localidades
}
