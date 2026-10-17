package ar.edu.ungs.prog2.smartgrids;

public class PlantaEolica extends Planta {
    int cantidadAerogeneradores;
    double alturaTorres;
    double velocidadMinimaViento;
	

    public PlantaEolica(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria,
            double eficienciaPromedio, int cantidadAerogeneradores,double alturaTorres, double velocidadMinimaViento) {
    	super(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento, fuentePrimaria, eficienciaPromedio, true);
    		this.cantidadAerogeneradores = cantidadAerogeneradores;
    		this.alturaTorres = alturaTorres;
    		this.velocidadMinimaViento = velocidadMinimaViento;

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