package smartGrids;

public class PlantaSolar extends Planta {
    private double superficieCubierta;
    private int cantidadPaneles;
    private double horasRadiacionDiaria;

    public PlantaSolar(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento, 
                       String fuentePrimaria, double eficienciaPromedio, double superficieCubierta, 
                       int cantidadPaneles, double horasRadiacionDiaria) {
        super(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento, fuentePrimaria, eficienciaPromedio, true);
        this.superficieCubierta = superficieCubierta;
        this.cantidadPaneles = cantidadPaneles;
        this.horasRadiacionDiaria = horasRadiacionDiaria;
    }



	@Override
    public double calcularCuota() {
       
		return this.getCapacidadMaxima() * this.getEficienciaPromedio() * this.getHorasRadiacionDiaria() / 24.0;
    }

    @Override
    public boolean cumpleCuota(Monitoreo m) {
        return m.getEnergia() >= calcularCuota();
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
}