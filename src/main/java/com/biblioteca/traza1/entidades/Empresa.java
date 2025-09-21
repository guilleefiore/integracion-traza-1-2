package com.biblioteca.traza1.entidades;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "sucursales") // excluye sucursales para evitar recursión infinita

public class Empresa {
    // Para buscar/actualizar por ID (punto 5 del TP)
    private Integer id;

    // Atributos del UML
    private String nombre;
    private String razonSocial;
    private Integer cuit;
    private String logo;

    // Relación: Empresa -> * Sucursal
    private Set<Sucursal> sucursales = new HashSet<>();
}
