package smartGrids;

import java.time.LocalDateTime;

public abstract class Planta {
    private String nombre;
    private String direccion;
    private double capacidadMaxima;
    private LocalDateTime puestaEnFuncionamiento;
    private String fuentePrimaria;
    private double eficienciaPromedio;
    private boolean renovable;    //cambie lo de diagrama,hay que modificar sin descartar el viejo, queda como un modelo descartado

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
    public String getNombre() {
        return nombre;
    }
    public String getDireccion() {
        return direccion;
    }
    public double getCapacidadMaxima() {
        return capacidadMaxima;
    }
    public LocalDateTime getPuestaEnFuncionamiento() {
        return puestaEnFuncionamiento;
    }
    public String getFuentePrimaria() {
        return fuentePrimaria;
    }
    public double getEficienciaPromedio() {
        return eficienciaPromedio;
    }
    public boolean EsRenovable() {
        return renovable;
    }
}