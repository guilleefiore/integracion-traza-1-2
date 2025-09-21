package com.biblioteca.traza1.entidades;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(exclude = "provincias")

public class Pais {
    private Integer id;
    private String nombre;

    // 1 País -> n Provincias
    @Builder.Default
    private Set<Provincia> provincias = new HashSet<>();
}
