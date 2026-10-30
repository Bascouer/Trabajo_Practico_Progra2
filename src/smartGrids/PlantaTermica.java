package smartGrids;

public  class PlantaTermica extends Planta {
    private double objetivoEmisionesCo2PorHora;
    private double capacidadAlmacenamientoCombustible;

    public PlantaTermica(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento, String tipoCombustible,
            double objetivoEmisionesCo2PorHora, double capacidadAlmacenamientoCombustible) {
    	super(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento, tipoCombustible, 0.5, false);
    		this.objetivoEmisionesCo2PorHora = objetivoEmisionesCo2PorHora;
    		this.capacidadAlmacenamientoCombustible = capacidadAlmacenamientoCombustible;
}

    @Override
    public double calcularCuota() {
        return this.getCapacidadMaxima() / 2.0;
    }

    @Override
    public boolean cumpleCuota(Monitoreo m) {
        return m.getEnergia() >= calcularCuota() && m.getExtra1() < objetivoEmisionesCo2PorHora;
    }
    public double getObjetivoEmisionesCo2PorHora() {
        return objetivoEmisionesCo2PorHora;
    }
    public double getCapacidadAlmacenamientoCombustible() {
        return capacidadAlmacenamientoCombustible;
    }
}
