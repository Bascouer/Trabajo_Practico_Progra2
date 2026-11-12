package smartGrids;

public class PlantaSolar extends Planta {
    private double superficieCubierta;
    private int cantidadPaneles;
    private double horasRadiacionDiaria;

    public PlantaSolar(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento, 
                       String fuentePrimaria, double eficienciaPromedio, double superficieCubierta, 
                       int cantidadPaneles, double horasRadiacionDiaria) {
        super(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento, fuentePrimaria, eficienciaPromedio, true);
        if (superficieCubierta < 0 || cantidadPaneles < 0 || horasRadiacionDiaria < 0) {
            throw new RuntimeException("Los atributos propios de la planta solar no pueden ser negativos");
        }
        this.superficieCubierta = superficieCubierta;
        this.cantidadPaneles = cantidadPaneles;
        this.horasRadiacionDiaria = horasRadiacionDiaria;
    }



	@Override
    public double calcularCuota() { // Devuelve la capacidad mínima de cuota de la planta solar, que es la capacidad máxima multiplicada por la eficiencia promedio y por las horas de radiación diaria dividido por 24
       
		return this.getCapacidadMaxima() * this.getEficienciaPromedio() * this.getHorasRadiacionDiaria() / 24.0;
    }
    
    public double getSuperficieCubierta() {
        return superficieCubierta;
    }
    public int getCantidadPaneles() {
        return cantidadPaneles;
    }
    public double getHorasRadiacionDiaria() {
        return horasRadiacionDiaria;
    }

    @Override
    public String toString() {
        return super.toString() + " | tipo: Solar"
                + " | superficie cubierta: " + superficieCubierta
                + " | cantidad de paneles: " + cantidadPaneles
                + " | horas de radiacion diaria: " + horasRadiacionDiaria;
    }
}