package ar.edu.ungs.prog2.smartgrids;

public  class PlantaTermica extends Planta {
    double objetivoEmisionesCo2PorHora;
    double capacidadAlmacenamientoCombustible;

    public PlantaTermica(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento, String tipoCombustible,
            double objetivoEmisionesCo2PorHora, double capacidadAlmacenamientoCombustible) {
    	super(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento, tipoCombustible, 0.5, false);
    		this.objetivoEmisionesCo2PorHora = objetivoEmisionesCo2PorHora;
    		this.capacidadAlmacenamientoCombustible = capacidadAlmacenamientoCombustible;
}

    @Override
    public double calcularCuota() {
        return capacidadMaxima / 2.0;
    }

    @Override
    public boolean cumpleCuota(Monitoreo m) {
        return m.energia >= calcularCuota() && m.extra1 < objetivoEmisionesCo2PorHora;
    }
}
