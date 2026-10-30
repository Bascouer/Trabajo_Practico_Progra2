package smartGrids;

import java.time.LocalDateTime;

public  class Monitoreo {
    private LocalDateTime fechaHora;
    private double energia;
    private int incidencias;
    private double extra1; // Velocidad del viento o CO2 emitido
    private double extra2; // Combustible utilizado (Térmica)

    public Monitoreo(LocalDateTime fechaHora, double energia, int incidencias, double extra1, double extra2) {
        this.fechaHora = fechaHora;
        this.energia = energia;
        this.incidencias = incidencias;
        this.extra1 = extra1;
        this.extra2 = extra2;
    }


public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public double getEnergia() {
        return energia;
    }

    public int getIncidencias() {
        return incidencias;
    }

    public double getExtra1() {
        return extra1;
    }

    public double getExtra2() {
        return extra2;
    }
}   

