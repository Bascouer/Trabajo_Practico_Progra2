package smartGrids;

public class PlantaHidraulica extends Planta {
    private int cantidadTurbinas;
    private double capacidadEmbalse;

    public PlantaHidraulica(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento,
                            String fuentePrimaria, double eficienciaPromedio, int cantidadTurbinas, double capacidadEmbalse) {
        super(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento, fuentePrimaria, eficienciaPromedio, true);
        this.cantidadTurbinas = cantidadTurbinas;
        this.capacidadEmbalse = capacidadEmbalse;
    }

    @Override
    public double calcularCuota() {
        return this.getCapacidadMaxima() * this.getEficienciaPromedio();
    }

    @Override
    public boolean cumpleCuota(Monitoreo m) {
        return m.getEnergia() >= calcularCuota();
    }
    public int getCantidadTurbinas() {
        return cantidadTurbinas;
    }
    public double getCapacidadEmbalse() {
        return capacidadEmbalse;
    }
}