package smartGrids;

public  class PlantaTermica extends Planta {
    private double objetivoEmisionesCo2PorHora;
    private double capacidadAlmacenamientoCombustible;

    public PlantaTermica(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento, String tipoCombustible,
            double objetivoEmisionesCo2PorHora, double capacidadAlmacenamientoCombustible) {
    	super(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento, tipoCombustible, 0.5, false);
    	if (objetivoEmisionesCo2PorHora < 0 || capacidadAlmacenamientoCombustible < 0) {
            throw new RuntimeException("Los atributos propios de la planta térmica no pueden ser negativos");
        }
    		this.objetivoEmisionesCo2PorHora = objetivoEmisionesCo2PorHora;
    		this.capacidadAlmacenamientoCombustible = capacidadAlmacenamientoCombustible;
}

    @Override
    public double calcularCuota() { // Devuelve la capacidad mínima de cuota de la planta térmica, que es la mitad de la capacidad máxima
        return this.getCapacidadMaxima() / 2.0;
    }

    @Override
    public boolean cumpleCuota(Monitoreo m) { // Devuelve true si la energía generada es mayor o igual a la cuota y las emisiones de CO2 por hora son menores al objetivo
        return m.getEnergia() >= calcularCuota() && m.getExtra1() < objetivoEmisionesCo2PorHora;
    }
    public double getObjetivoEmisionesCo2PorHora() {
        return objetivoEmisionesCo2PorHora;
    }
    public double getCapacidadAlmacenamientoCombustible() {
        return capacidadAlmacenamientoCombustible;
    }
    @Override
    public String toString() {
        return super.toString() + " | tipo: Termica"
                + " | objetivo emisiones CO2 por hora: " + objetivoEmisionesCo2PorHora
                + " | capacidad de almacenamiento de combustible: " + capacidadAlmacenamientoCombustible;
    }
}
