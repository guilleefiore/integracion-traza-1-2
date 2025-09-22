package com.biblioteca.app;

import com.biblioteca.repositorio.InMemoryRepository;
import com.biblioteca.traza2.entidades.*;

public class MainTraza2 {
    public static void main(String[] args) {
        // Repos genéricos
        var repoCategoria = new InMemoryRepository<Categoria>();
        var repoUnidad    = new InMemoryRepository<UnidadMedida>();
        var repoInsumo    = new InMemoryRepository<ArticuloInsumo>();
        var repoManu      = new InMemoryRepository<ArticuloManufacturado>();

        // =====================================================
        // Categorías
        // =====================================================
        var catPizzas   = repoCategoria.save(Categoria.builder().denominacion("Pizzas").build());
        var catSandwich = repoCategoria.save(Categoria.builder().denominacion("Sandwich").build());
        var catLomos    = repoCategoria.save(Categoria.builder().denominacion("Lomos").build());
        var catInsumos  = repoCategoria.save(Categoria.builder().denominacion("Insumos").build());

        // =====================================================
        // Unidades de medida
        // =====================================================
        var kg    = repoUnidad.save(UnidadMedida.builder().denominacion("Kilogramos").build());
        var litro = repoUnidad.save(UnidadMedida.builder().denominacion("Litros").build());
        var gr    = repoUnidad.save(UnidadMedida.builder().denominacion("Gramos").build());

        // =====================================================
        // Artículos Insumo
        // =====================================================
        var sal = repoInsumo.save(ArticuloInsumo.builder()
                .denominacion("Sal").precioVenta(100.0)
                .unidadMedida(gr).categoria(catInsumos)
                .precioCompra(60.0).stockActual(100).stockMinimo(10).stockMaximo(500)
                .esParaElaborar(true).build());

        var aceite = repoInsumo.save(ArticuloInsumo.builder()
                .denominacion("Aceite").precioVenta(1200.0)
                .unidadMedida(litro).categoria(catInsumos)
                .precioCompra(900.0).stockActual(30).stockMinimo(3).stockMaximo(60)
                .esParaElaborar(true).build());

        var carne = repoInsumo.save(ArticuloInsumo.builder()
                .denominacion("Carne").precioVenta(6000.0)
                .unidadMedida(kg).categoria(catInsumos)
                .precioCompra(4800.0).stockActual(50).stockMinimo(5).stockMaximo(80)
                .esParaElaborar(true).build());

        var harina = repoInsumo.save(ArticuloInsumo.builder()
                .denominacion("Harina").precioVenta(1500.0)
                .unidadMedida(kg).categoria(catInsumos)
                .precioCompra(1100.0).stockActual(200).stockMinimo(20).stockMaximo(400)
                .esParaElaborar(true).build());

        // =====================================================
        // Imágenes sin repo - No se guardan hasta agregarlas
        // =====================================================
        var img1 = ImagenArticulo.builder().denominacion("hawaina1").build();
        var img2 = ImagenArticulo.builder().denominacion("hawaina2").build();
        var img3 = ImagenArticulo.builder().denominacion("hawaina3").build();
        var img4 = ImagenArticulo.builder().denominacion("lomo1").build();
        var img5 = ImagenArticulo.builder().denominacion("lomo2").build();
        var img6 = ImagenArticulo.builder().denominacion("lomo3").build();

        // =====================================================
        // Artículos Manufacturados
        // =====================================================
        var pizzaHawaiana = repoManu.save(ArticuloManufacturado.builder()
                .denominacion("Pizza Hawaiana").precioVenta(9000.0)
                .descripcion("Pizza con ananá")
                .tiempoEstimadoMinutos(20).preparacion("Estirar masa, agregar ingredientes y hornear")
                .unidadMedida(kg).categoria(catPizzas).build());
        pizzaHawaiana.addImagen(img1);
        pizzaHawaiana.addImagen(img2);
        pizzaHawaiana.addImagen(img3);
        pizzaHawaiana.addDetalle(harina, 2);
        pizzaHawaiana.addDetalle(sal, 1);
        pizzaHawaiana.addDetalle(aceite, 1);

        var lomoCompleto = repoManu.save(ArticuloManufacturado.builder()
                .denominacion("Lomo Completo").precioVenta(7500.0)
                .descripcion("Lomo con todo")
                .tiempoEstimadoMinutos(15).preparacion("Plancha y armado en pan")
                .unidadMedida(kg).categoria(catLomos).build());
        lomoCompleto.addImagen(img4);
        lomoCompleto.addImagen(img5);
        lomoCompleto.addImagen(img6);
        lomoCompleto.addDetalle(sal, 1);
        lomoCompleto.addDetalle(aceite, 1);
        lomoCompleto.addDetalle(carne, 1);

        // =====================================================
        // Salidas
        // =====================================================
        System.out.println("\n[a] Categorías:");
        repoCategoria.findAll().forEach(System.out::println);

        System.out.println("\n[b] Artículos INSUMO:");
        repoInsumo.findAll().forEach(System.out::println);

        System.out.println("\n[c] Artículos MANUFACTURADOS:");
        repoManu.findAll().forEach(System.out::println);

        System.out.println("\n[d] Buscar manufacturado por ID (Pizza):");
        var idPizza = pizzaHawaiana.getId();
        System.out.println("Encontrado: " + repoManu.findById(idPizza).orElse(null));

        System.out.println("\n[e] Actualizar manufacturado por ID (cambio de descripción Pizza):");
        repoManu.findById(idPizza).ifPresent(m -> m.setDescripcion("Pizza con ananá y extra queso"));
        System.out.println(repoManu.findById(idPizza).orElse(null));

        System.out.println("\n[f] Eliminar manufacturado por ID (Lomo):");
        var idLomo = lomoCompleto.getId();
        System.out.println("Eliminado: " + repoManu.genericDelete(idLomo));

        System.out.println("\n== Estado FINAL de manufacturados ==");
        repoManu.findAll().forEach(System.out::println);
    }
}
