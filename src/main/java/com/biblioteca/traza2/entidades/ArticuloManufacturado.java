package com.biblioteca.traza2.entidades;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @SuperBuilder @ToString(callSuper = true) // imprime lo de Articulo + sus propios campos

public class ArticuloManufacturado extends Articulo{
    private String descripcion;
    private Integer tiempoEstimadoMinutos;
    private String preparacion;

    // relacion 1 -> n (excluyo para que el toString no sea grande)
    @Builder.Default @ToString.Exclude
    private Set<ArticuloManufacturadoDetalle> detalles = new HashSet<>();

    public void addDetalle(ArticuloInsumo insumo, int cantidad) {
        detalles.add(ArticuloManufacturadoDetalle.builder()
                .cantidad(cantidad)
                .articuloInsumo(insumo)
                .build());
    }
}
