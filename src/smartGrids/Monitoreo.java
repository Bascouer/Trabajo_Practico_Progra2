package smartGrids;

import java.time.LocalDateTime;

public  class Monitoreo {
    LocalDateTime fechaHora;
    double energia;
    int incidencias;
    double extra1; // Velocidad del viento o CO2 emitido
    double extra2; // Combustible utilizado (Térmica)

    public Monitoreo(LocalDateTime fechaHora, double energia, int incidencias, double extra1, double extra2) {
        this.fechaHora = fechaHora;
        this.energia = energia;
        this.incidencias = incidencias;
        this.extra1 = extra1;
        this.extra2 = extra2;
    }
}



