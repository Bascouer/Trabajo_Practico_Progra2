package smartGrids;

public class PlantaEolica extends Planta {
    private int cantidadAerogeneradores;
    private double alturaTorres;
    private double velocidadMinimaViento;
	

    public PlantaEolica(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria,
            double eficienciaPromedio, int cantidadAerogeneradores,double alturaTorres, double velocidadMinimaViento) {
    	super(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento, fuentePrimaria, eficienciaPromedio, true);
    	if (cantidadAerogeneradores < 0 || alturaTorres < 0 || velocidadMinimaViento < 0) {
            throw new RuntimeException("Los atributos propios de la planta eólica no pueden ser negativos");
        }
    		this.cantidadAerogeneradores = cantidadAerogeneradores;
    		this.alturaTorres = alturaTorres;
    		this.velocidadMinimaViento = velocidadMinimaViento;

}
    @Override
    public double calcularCuota() { 
        return getCapacidadMaxima() * getEficienciaPromedio();
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
    @Override
    public String toString() {
    return super.toString() + " | tipo: Eolica"
            + " | aerogeneradores: " + cantidadAerogeneradores
            + " | altura de torres: " + alturaTorres
            + " | velocidad minima de viento: " + velocidadMinimaViento;
}
}