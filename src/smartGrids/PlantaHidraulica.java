package smartGrids;

public class PlantaHidraulica extends Planta {
    private int cantidadTurbinas;
    private double capacidadEmbalse;

    public PlantaHidraulica(String nombre, String direccion, double capacidadMaxima,
            String fechaHoraPuestaEnFuncionamiento,
            String fuentePrimaria, double eficienciaPromedio, int cantidadTurbinas, double capacidadEmbalse) {
        super(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento, fuentePrimaria, eficienciaPromedio,
                true);
        this.cantidadTurbinas = cantidadTurbinas;
        this.capacidadEmbalse = capacidadEmbalse;
    }

    @Override
    public double calcularCuota() { // Devuelve la capacidad mínima de cuota de la planta hidráulica, que es la
                                    // capacidad máxima multiplicada por la eficiencia promedio
        return this.getCapacidadMaxima() * this.getEficienciaPromedio();
    }

    public int getCantidadTurbinas() {
        return cantidadTurbinas;
    }

    public double getCapacidadEmbalse() {
        return capacidadEmbalse;
    }

    @Override
    public String toString() {
        return super.toString() + " | tipo: Hidraulica"
                + " | cantidad de turbinas: " + cantidadTurbinas
                + " | capacidad del embalse: " + capacidadEmbalse;
    }
}