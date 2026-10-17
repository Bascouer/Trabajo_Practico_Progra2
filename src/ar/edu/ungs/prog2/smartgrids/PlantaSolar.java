package ar.edu.ungs.prog2.smartgrids;

public class PlantaSolar extends Planta {
    double superficieCubierta;
    int cantidadPaneles;
    double horasRadiacionDiaria;

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
       
		return capacidadMaxima * eficienciaPromedio * horasRadiacionDiaria / 24.0;
    }

    @Override
    public boolean cumpleCuota(Monitoreo m) {
        return m.energia >= calcularCuota();
    }
}