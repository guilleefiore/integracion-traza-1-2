package com.biblioteca.traza1.entidades;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(exclude = {"pais", "localidades"}) // evita recursión en prints)

public class Provincia {
    private Integer id;
    private String nombre;
    private Pais pais; // 1 País -> n Provincias

    // 1 Provincia -> n Localidades
    @Builder.Default
    private Set<Localidad> localidades = new HashSet<>();
}
