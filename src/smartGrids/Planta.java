package smartGrids;

import java.time.LocalDateTime;

public abstract class Planta {
    protected String nombre;
    protected String direccion;
    protected double capacidadMaxima;
    protected LocalDateTime puestaEnFuncionamiento;
    protected String fuentePrimaria;
    protected double eficienciaPromedio;
    protected boolean renovable;    //cambie lo de diagrama,hay que modificar sin descartar el viejo, queda como un modelo descartado

    public Planta(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento,
                  String fuentePrimaria, double eficienciaPromedio, boolean renovable) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.capacidadMaxima = capacidadMaxima;
        try {
            this.puestaEnFuncionamiento = LocalDateTime.parse(fechaHoraPuestaEnFuncionamiento);
        } catch (Exception e) {
            throw new RuntimeException("Formato de fechaHora inválido");
        }
        this.fuentePrimaria = fuentePrimaria;
        this.eficienciaPromedio = eficienciaPromedio;
        this.renovable = renovable;
    }

    public abstract double calcularCuota();
    public abstract boolean cumpleCuota(Monitoreo m);
}