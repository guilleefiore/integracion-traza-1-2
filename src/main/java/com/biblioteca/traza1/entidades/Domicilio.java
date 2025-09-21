package com.biblioteca.traza1.entidades;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

public class Domicilio {
    private Integer id;
    private String calle;
    private Integer numero;
    private String cp;
    private Localidad localidad; // relación: Domicilio -> Localidad
}
