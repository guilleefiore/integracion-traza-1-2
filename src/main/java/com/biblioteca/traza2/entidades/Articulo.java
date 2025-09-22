package com.biblioteca.traza2.entidades;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder @ToString(exclude = "imagenes")

public abstract class Articulo {
    private Long id;
    private String denominacion;
    private Double precioVenta;

    // relaciones n -> 1
    private UnidadMedida unidadMedida;
    private Categoria categoria;

    // relaciones 1 -> n (excluí para no imprimir listados grandes)
    @Builder.Default
    private Set<ImagenArticulo> imagenes = new HashSet<>();

    public void addImagen(ImagenArticulo img) {
        this.imagenes.add(img);
    }
}
