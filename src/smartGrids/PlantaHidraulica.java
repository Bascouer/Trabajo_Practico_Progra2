package smartGrids;

public class PlantaHidraulica extends Planta {
    int cantidadTurbinas;
    double capacidadEmbalse;

    public PlantaHidraulica(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento,
                            String fuentePrimaria, double eficienciaPromedio, int cantidadTurbinas, double capacidadEmbalse) {
        super(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento, fuentePrimaria, eficienciaPromedio, true);
        this.cantidadTurbinas = cantidadTurbinas;
        this.capacidadEmbalse = capacidadEmbalse;
    }

    @Override
    public double calcularCuota() {
        return capacidadMaxima * eficienciaPromedio;
    }

    @Override
    public boolean cumpleCuota(Monitoreo m) {
        return m.energia >= calcularCuota();
    }
}