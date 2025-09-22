package com.biblioteca.traza2.entidades;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @ToString
public class ArticuloManufacturadoDetalle {
    private Long id;
    @NonNull private Integer cantidad;

    // relación n -> 1 a Insumo
    @NonNull private ArticuloInsumo articuloInsumo;
}
