package smartGrids;

public class PlantaEolica extends Planta {
    private int cantidadAerogeneradores;
    private double alturaTorres;
    private double velocidadMinimaViento;
	

    public PlantaEolica(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria,
            double eficienciaPromedio, int cantidadAerogeneradores,double alturaTorres, double velocidadMinimaViento) {
    	super(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento, fuentePrimaria, eficienciaPromedio, true);
    		this.cantidadAerogeneradores = cantidadAerogeneradores;
    		this.alturaTorres = alturaTorres;
    		this.velocidadMinimaViento = velocidadMinimaViento;

}
    @Override
    public double calcularCuota() {
        return getCapacidadMaxima() * getEficienciaPromedio();
    }

    @Override
    public boolean cumpleCuota(Monitoreo m) {
        return m.getEnergia() >= calcularCuota();
    }
    public int getCantidadAerogeneradores() {
        return cantidadAerogeneradores;
    }
    public double getAlturaTorres() {
        return alturaTorres;
    }
    public double getVelocidadMinimaViento() {
        return velocidadMinimaViento;
    }
}