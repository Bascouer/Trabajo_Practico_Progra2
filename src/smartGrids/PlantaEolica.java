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
    public double calcularCuota() { // Devuelve la capacidad mínima de cuota de la planta eólica, que es la capacidad máxima multiplicada por la eficiencia promedio
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