package smartGrids;

import java.time.LocalDateTime;

public abstract class Planta {
    private String nombre;
    private String direccion;
    private double capacidadMaxima;
    private LocalDateTime puestaEnFuncionamiento;
    private String fuentePrimaria;
    private double eficienciaPromedio;
    private boolean renovable;    

    public Planta(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento,
                  String fuentePrimaria, double eficienciaPromedio, boolean renovable) {
    	if (nombre == null || nombre.trim().isEmpty()) {
            throw new RuntimeException("El nombre de la planta no puede ser nulo ni vacío");
        }
        if (direccion == null) {
            throw new RuntimeException("La dirección no puede ser nula");
        }
        if (capacidadMaxima <= 0) {
            throw new RuntimeException("La capacidad máxima debe ser mayor a 0");
        }
        if (fuentePrimaria == null || fuentePrimaria.trim().isEmpty()) {
            throw new RuntimeException("La fuente primaria no puede ser nula ni vacía");
        }
        if (eficienciaPromedio < 0 || eficienciaPromedio > 1) {
            throw new RuntimeException("La eficiencia promedio debe estar entre 0 y 1");
        }
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

    public boolean cumpleCuota(Monitoreo m) {
        return m.getEnergia() >= calcularCuota();
    }

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
    public boolean esRenovable() {
        return renovable;
    }
    @Override
    public String toString() {
    return "Planta " + nombre + " | direccion: " + direccion + " | capacidad maxima: " + capacidadMaxima + 
    " | puesta en funcionamiento: " + puestaEnFuncionamiento + " | fuente primaria: " + fuentePrimaria + 
    " | eficiencia: " + eficienciaPromedio  + " | renovable: " + (renovable ? "si" : "no");
    }
}