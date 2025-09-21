package com.biblioteca.app;

import com.biblioteca.repositorio.InMemoryRepository;
import com.biblioteca.traza1.entidades.*;

public class MainTraza1 {
    public static void main(String[] args) {

        // =====================================================
        // PUNTO 4 — Secuencia de creación y relaciones (con builders)
        // =====================================================

        // 1) País: Argentina
        Pais argentina = Pais.builder()
                .nombre("Argentina")
                .build();

        // 2) Provincia: Buenos Aires (relación con País)
        Provincia buenosAires = Provincia.builder()
                .nombre("Buenos Aires")
                .pais(argentina)
                .build();
        argentina.getProvincias().add(buenosAires);

        // 3) Localidad: CABA (pertenece a Buenos Aires)
        Localidad caba = Localidad.builder()
                .nombre("CABA")
                .provincia(buenosAires)
                .build();
        buenosAires.getLocalidades().add(caba);

        // 4) Domicilio para CABA
        Domicilio domCaba = Domicilio.builder()
                .calle("Cerrito")
                .numero(628)
                .cp("C1010")
                .localidad(caba)
                .build();

        // 5) Otra localidad de Buenos Aires: La Plata
        Localidad laPlata = Localidad.builder()
                .nombre("La Plata")
                .provincia(buenosAires)
                .build();
        buenosAires.getLocalidades().add(laPlata);

        // 6) Domicilio para La Plata
        Domicilio domLaPlata = Domicilio.builder()
                .calle("Paseo del Bosque")
                .numero(null)
                .cp("B1900")
                .localidad(laPlata)
                .build();

        // 7) Otra provincia: Córdoba (relación con País)
        Provincia cordoba = Provincia.builder()
                .nombre("Córdoba")
                .pais(argentina)
                .build();
        argentina.getProvincias().add(cordoba);

        // 8) Localidad de Córdoba: Córdoba Capital
        Localidad cordobaCapital = Localidad.builder()
                .nombre("Córdoba Capital")
                .provincia(cordoba)
                .build();
        cordoba.getLocalidades().add(cordobaCapital);

        // 9) Domicilio para Córdoba Capital
        Domicilio domCbaCapital = Domicilio.builder()
                .calle("Jose Antonio Goyenechea")
                .numero(2851)
                .cp("X5009")
                .localidad(cordobaCapital)
                .build();

        // 10) Otra localidad de Córdoba: Villa Carlos Paz
        Localidad villaCarlosPaz = Localidad.builder()
                .nombre("Villa Carlos Paz")
                .provincia(cordoba)
                .build();
        cordoba.getLocalidades().add(villaCarlosPaz);

        // 11) Domicilio para Villa Carlos Paz
        Domicilio domVcp = Domicilio.builder()
                .calle("Av. Illia")
                .numero(899)
                .cp("X5152")
                .localidad(villaCarlosPaz)
                .build();

        // 12) Sucursal 1 ↔ domicilio de CABA
        Sucursal sucursal1 = Sucursal.builder()
                .nombre("Sucursal 1 - CABA")
                .horarioApertura(java.time.LocalTime.of(9, 0))
                .horarioCierre(java.time.LocalTime.of(18, 0))
                .esCasaMatriz(true)
                .domicilio(domCaba)
                .build();

        // 13) Sucursal 2 ↔ domicilio de La Plata
        Sucursal sucursal2 = Sucursal.builder()
                .nombre("Sucursal 2 - La Plata")
                .horarioApertura(java.time.LocalTime.of(8, 0))
                .horarioCierre(java.time.LocalTime.of(16, 30))
                .esCasaMatriz(false)
                .domicilio(domLaPlata)
                .build();

        // 14) Sucursal 3 ↔ domicilio de Córdoba Capital
        Sucursal sucursal3 = Sucursal.builder()
                .nombre("Sucursal 3 - Córdoba Capital")
                .horarioApertura(java.time.LocalTime.of(8, 30))
                .horarioCierre(java.time.LocalTime.of(13, 30))
                .esCasaMatriz(false)
                .domicilio(domCbaCapital)
                .build();

        // 15) (La consigna repite “Sucursal2”; interpretamos Sucursal 4) ↔ domicilio de Villa Carlos Paz
        Sucursal sucursal4 = Sucursal.builder()
                .nombre("Sucursal 4 - Villa Carlos Paz")
                .horarioApertura(java.time.LocalTime.of(9, 0))
                .horarioCierre(java.time.LocalTime.of(20, 0))
                .esCasaMatriz(false)
                .domicilio(domVcp)
                .build();

        // 16) Empresa 1 con sucursales 1 y 2
        Empresa empresa1 = Empresa.builder()
                .nombre("Empresa 1")
                .razonSocial("Empresa Uno S.A.")
                .cuit(20300123)     // Integer según tu UML
                .logo("logo1.png")
                .sucursales(new java.util.HashSet<>(java.util.Set.of(sucursal1, sucursal2)))
                .build();

        // 17) Empresa 2 con sucursales 3 y 4
        Empresa empresa2 = Empresa.builder()
                .nombre("Empresa 2")
                .razonSocial("Empresa Dos S.A.")
                .cuit(20300222)
                .logo("logo2.png")
                .sucursales(new java.util.HashSet<>(java.util.Set.of(sucursal3, sucursal4)))
                .build();

        // =====================================================
        // PUNTO 5 — CRUD de Empresa con mi InMemoryRepository
        // =====================================================

        InMemoryRepository<Empresa> repo = new InMemoryRepository<>();

        // Guardar las empresas creadas en el Punto 4
        repo.save(empresa1);
        repo.save(empresa2);

        // a) Mostrar TODAS las empresas
        System.out.println("\n[a] Todas las empresas:");
        repo.findAll().forEach(System.out::println);

        // b) Buscar una empresa por su ID
        long idEmp1 = empresa1.getId().longValue();   // convertir Integer -> long
        System.out.println("\n[b] Buscar empresa por ID = " + idEmp1 + ":");
        System.out.println(repo.findById(idEmp1).orElse(null));

        // c) Buscar empresas por NOMBRE (igualdad exacta)
        System.out.println("\n[c] Buscar por nombre = 'Empresa 1':");
        repo.genericFindByField("nombre", "Empresa 1")
                .forEach(System.out::println);

        // d) Actualizar datos por ID (ej.: CUIT)
        System.out.println("\n[d] Actualizar CUIT de la empresa con ID " + idEmp1 + " a 20999999:");
        repo.genericUpdate(idEmp1, "cuit", 20999999);
        System.out.println(repo.findById(idEmp1).orElse(null));

        // e) Eliminar una empresa por ID (la segunda)
        long idEmp2 = empresa2.getId().longValue();
        System.out.println("\n[e] Eliminar empresa con ID = " + idEmp2 + ":");
        boolean borrada = repo.genericDelete(idEmp2);
        System.out.println("Eliminada: " + borrada);

        // Estado final
        System.out.println("\n== Estado final de empresas ==");
        repo.findAll().forEach(System.out::println);
    }
}
