package smartGrids;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.time.format.DateTimeFormatter;



public class SmartGrids implements ISmartGrids {
	   private final Map<String, Planta> plantas = new LinkedHashMap<>();
	    private final Map<String, List<Monitoreo>> monitoreos = new LinkedHashMap<>();
	    private final Map<String, List<Estado>> estados = new LinkedHashMap<>();


	 @Override
	    public void registrarPlantaSolar(String nombre, String direccion, double capacidadMaxima,
	            String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
	            double superficieCubierta, int cantidadPaneles, double horasRadiacionDiaria) {
	        validarNombreUnico(nombre);
	        PlantaSolar p = new PlantaSolar(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento,
	                fuentePrimaria, eficienciaPromedio, superficieCubierta, cantidadPaneles, horasRadiacionDiaria);
	        plantas.put(nombre, p);
	        monitoreos.put(nombre, new ArrayList<>());
	        estados.put(nombre, new ArrayList<>());
	    }

	    @Override
	    public void registrarPlantaEolica(String nombre, String direccion, double capacidadMaxima,
	            String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
	            int cantidadAerogeneradores, double alturaTorres, double velocidadMinimaViento) {
	        validarNombreUnico(nombre);
	        PlantaEolica p = new PlantaEolica(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento,
	                fuentePrimaria, eficienciaPromedio, cantidadAerogeneradores, alturaTorres, velocidadMinimaViento);
	        plantas.put(nombre, p);
	        monitoreos.put(nombre, new ArrayList<>());
	        estados.put(nombre, new ArrayList<>());
	    }

	    @Override
	    public void registrarPlantaHidraulica(String nombre, String direccion, double capacidadMaxima,
	            String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
	            int cantidadTurbinas, double capacidadEmbalse) {
	        validarNombreUnico(nombre);
	        PlantaHidraulica p = new PlantaHidraulica(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento,
	                fuentePrimaria, eficienciaPromedio, cantidadTurbinas, capacidadEmbalse);
	        plantas.put(nombre, p);
	        monitoreos.put(nombre, new ArrayList<>());
	        estados.put(nombre, new ArrayList<>());
	    }

	    @Override
	    public void registrarPlantaTermica(String nombre, String direccion, double capacidadMaxima,
	            String fechaHoraPuestaEnFuncionamiento, String tipoCombustible, double objetivoEmisionesCo2PorHora,
	            double capacidadAlmacenamientoCombustible) {
	        validarNombreUnico(nombre);
	        PlantaTermica p = new PlantaTermica(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento,
	                tipoCombustible, objetivoEmisionesCo2PorHora, capacidadAlmacenamientoCombustible);
	        plantas.put(nombre, p);
	        monitoreos.put(nombre, new ArrayList<>());
	        estados.put(nombre, new ArrayList<>());
	    }

	    @Override
	    public List<String> consultarPlantas() {
	        List<String> resultado = new ArrayList<>();
	        // 1. Creamos el formateador con el patrón exacto que pide el test
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

	        for (Map.Entry<String, Planta> entry : plantas.entrySet()) {
	            String nombre = entry.getKey();
	            Planta p = entry.getValue();
	            String estadoStr = estaActiva(nombre) ? "ACTIVA" : "INACTIVA";
	            
	            // 2. Formateamos la fecha usando el formatter en lugar de .toString()
	            String fechaFormateada = p.getPuestaEnFuncionamiento().format(formatter);
	            
	            // 3. Armamos el resultado
	            resultado.add(nombre + " (" + estadoStr + "): " + fechaFormateada);
	        }
	        return resultado;
	    }
	    @Override
	    public double capacidadMinimaCuota(String nombre) {
	        return obtenerPlanta(nombre).calcularCuota();
	    }

	    @Override
	    public void activarPlanta(String nombre, String fechaHora) {
	        obtenerPlanta(nombre);
	        LocalDateTime dt = parsearFechaHora(fechaHora);
	        estados.get(nombre).add(new Estado(dt, true));
	    }

	    @Override
	    public void desactivarPlanta(String nombre, String fechaHora) {
	        obtenerPlanta(nombre);
	        LocalDateTime dt = parsearFechaHora(fechaHora);
	        estados.get(nombre).add(new Estado(dt, false));
	    }

	    @Override
	    public boolean estaActiva(String nombre) {
	        obtenerPlanta(nombre);
	        List<Estado> lista = estados.get(nombre);
	        if (lista.isEmpty()) {
	            return true; 
	        }
	        return lista.get(lista.size() - 1).estaActiva();
	    }


	    private void validarMonitoreoComun(String nombre, String fechaHora, double energia, int incidencias) {
	        Planta p = obtenerPlanta(nombre);
	        if (energia < 0 || incidencias < 0) {
	            throw new RuntimeException("Energía e incidencias no pueden ser negativas");
	        }
	        LocalDateTime dt = parsearFechaHora(fechaHora);
	        if (dt.isBefore(p.getPuestaEnFuncionamiento())) {
	            throw new RuntimeException("Fecha anterior a la puesta en funcionamiento");
	        }
	        // Verificar si estuvo inactiva en ese momento exacto o previo
	        if (!estaActiva(nombre)) {
	            throw new RuntimeException("La planta está inactiva");
	        }
	        // Validar duplicado para la misma hora
	        for (Monitoreo m : monitoreos.get(nombre)) {
	            if (m.getFechaHora().equals(dt)) {
	                throw new RuntimeException("Ya existe un monitoreo para esta hora");
	            }
	        }
	    }

	    @Override
	    public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias) {
	        Planta p = obtenerPlanta(nombre);
	        if (p instanceof PlantaTermica || p instanceof PlantaEolica) {
	            throw new RuntimeException("Tipo de planta requiere parámetros adicionales de monitoreo");
	        }
	        validarMonitoreoComun(nombre, fechaHora, energiaGenerada, incidencias);
	        monitoreos.get(nombre).add(new Monitoreo(parsearFechaHora(fechaHora), energiaGenerada, incidencias, 0, 0));
	    }

	    @Override
	    public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias, double velocidadViento) {
	        Planta p = obtenerPlanta(nombre);
	        if (!(p instanceof PlantaEolica)) {
	            throw new RuntimeException("Este método de monitoreo es exclusivo para plantas eólicas");
	        }
	        validarMonitoreoComun(nombre, fechaHora, energiaGenerada, incidencias);
	        monitoreos.get(nombre).add(new Monitoreo(parsearFechaHora(fechaHora), energiaGenerada, incidencias, velocidadViento, 0));
	    }

	    @Override
	    public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias, double co2Emitido, double combustibleUtilizado) {
	        Planta p = obtenerPlanta(nombre);
	        if (!(p instanceof PlantaTermica)) {
	            throw new RuntimeException("Este método de monitoreo es exclusivo para plantas térmicas");
	        }
	        validarMonitoreoComun(nombre, fechaHora, energiaGenerada, incidencias);
	        monitoreos.get(nombre).add(new Monitoreo(parsearFechaHora(fechaHora), energiaGenerada, incidencias, co2Emitido, combustibleUtilizado));
	    }

	    @Override
	    public double totalGenerado(String nombre) {
	        obtenerPlanta(nombre);
	        double total = 0;
	        for (Monitoreo m : monitoreos.get(nombre)) {
	            total += m.getEnergia();
	        }
	        return total;
	    }

	    @Override
	    public int[] incidenciasPorHora(String nombre) {
	        obtenerPlanta(nombre);
	        int[] resultado = new int[24];
	        for (Monitoreo m : monitoreos.get(nombre)) {
	            resultado[m.getFechaHora().getHour()] += m.getIncidencias();
	        }
	        return resultado;
	    }

	    @Override
	    public int[] histogramaCumplimiento(String nombre, String fechaDesde, String fechaHasta) {
	        Planta p = obtenerPlanta(nombre);
	        LocalDate inicio = parsearFecha(fechaDesde);
	        LocalDate fin = parsearFecha(fechaHasta);
	        if (inicio.isAfter(fin)) {
	            throw new RuntimeException("La fecha desde es posterior a la fecha hasta");
	        }

	        int[] hist = new int[24];
	        for (Monitoreo m : monitoreos.get(nombre)) {
	            LocalDate d = m.getFechaHora().toLocalDate();
	            if (!d.isBefore(inicio) && !d.isAfter(fin)) {
	                if (p.cumpleCuota(m)) {
	                    hist[m.getFechaHora().getHour()]++;
	                }
	            }
	        }
	        return hist;
	    }

	   @Override
         public Map<String, Integer> plantasRenovablesPorFuente() {
         Map<String, Integer> mapa = new LinkedHashMap<>();
         for (Planta p : plantas.values()) { //recorro plantas
          if (p.esRenovable()) { // Verifico si es renovable
              String fuente = p.getFuentePrimaria(); // Obtengo la fuente primaria
               if (mapa.containsKey(fuente)) { // Si ya existe la fuente en el mapa, incremento el contador
                  mapa.put(fuente, mapa.get(fuente) + 1); // Incremento el contador
               } else { // Si no existe la fuente en el mapa, la agrego con contador 1
                mapa.put(fuente, 1);
            }
        }
    }
    return mapa;
}

	    @Override
	    public List<String> plantasQueNuncaCumplieronCuota() {
	        List<String> resultado = new ArrayList<>();
	        for (Map.Entry<String, Planta> entry : plantas.entrySet()) {
	            String nombre = entry.getKey();
	            Planta p = entry.getValue();
	            List<Monitoreo> lista = monitoreos.get(nombre);
	            boolean cumplio = false;
	            for (Monitoreo m : lista) {
	                if (p.cumpleCuota(m)) {
	                    cumplio = true;
	                    break;
	                }
	            }
	            if (!cumplio) {
	                resultado.add(nombre);
	            }
	        }
	        return resultado;
	    }

	    @Override
	    public String horasSinVelocidadMinimaPorPlantaEolica() {
	        StringBuilder sb = new StringBuilder();
	        sb.append("Plantas Eolicas: Horas con poco viento\n");
	        
	        for (Map.Entry<String, Planta> entry : plantas.entrySet()) {
	            if (entry.getValue() instanceof PlantaEolica) {
	                PlantaEolica pe = (PlantaEolica) entry.getValue();
	                List<Monitoreo> lista = monitoreos.get(pe.getNombre());
	                
	                // Usamos arrays para las 24 horas del día
	                boolean[] horaMonitoreada = new boolean[24];
	                boolean[] alcanzoMinimo = new boolean[24];
	                
	                // Recorremos todos los monitoreos para ver en qué horas se llegó a la cuota
	                for (Monitoreo m : lista) {
	                    int horaDelDia = m.getFechaHora().getHour();
	                    horaMonitoreada[horaDelDia] = true; // Marcamos que existe un registro a esta hora
	                    
	                    if (m.getExtra1() >= pe.getVelocidadMinimaViento()) {
	                        alcanzoMinimo[horaDelDia] = true; // Si en algún día superó el mínimo, se salva
	                    }
	                }
	                
	                // Juntamos las horas que nunca alcanzaron el mínimo
	                List<Integer> horasBajas = new ArrayList<>();
	                for (int i = 0; i < 24; i++) {
	                    if (horaMonitoreada[i] && !alcanzoMinimo[i]) {
	                        horasBajas.add(i);
	                    }
	                }
	                
	                // Si hay horas reportables, las agregamos al texto
	                if (!horasBajas.isEmpty()) {
	                    sb.append("- ").append(pe.getNombre()).append(":\n");
	                    for (Integer h : horasBajas) {
	                        sb.append(String.format("        %02d hs\n", h));
	                    }
	                }
	            }
	        }
	        return sb.toString().trim();
	    }

	    @Override
	    public int limpiarMonitoreosSinDatos() { // Elimina todos los monitoreos que tengan energía e incidencias en 0 y devuelve la cantidad de monitoreos eliminados
	        int borrados = 0; // Contador de monitoreos eliminados
	        for (List<Monitoreo> lista : monitoreos.values()) { // Recorro todas las listas de monitoreos de cada planta
	            Iterator<Monitoreo> it = lista.iterator(); // Uso un iterador para poder eliminar elementos mientras recorro la lista
	            while (it.hasNext()) { // Mientras haya elementos en la lista
	                Monitoreo m = it.next(); // Obtengo el siguiente monitoreo
	                if (m.getEnergia() == 0 && m.getIncidencias() == 0) { // Si el monitoreo tiene energía e incidencias en 0, lo elimino
	                    it.remove(); 
	                    borrados++; // Incremento el contador de monitoreos eliminados
	                }
	            }
	        }
	        return borrados; // Devuelvo la cantidad de monitoreos eliminados
	    }

	@Override
    public List<String> plantasOrdenadasPorEnergiaGenerada() { 
    List<Planta> pendientes = new ArrayList<>(plantas.values()); // Copia de las plantas pendietes para ordenar
    List<String> resultado = new ArrayList<>(); // Lista donde estaran los nombres ordenados por energía generada

    while (!pendientes.isEmpty()) { // Mientras haya plantas pendientes
        Planta mayor = pendientes.get(0); // Tomo la primera como mayor
        for (Planta p : pendientes) { // Recorro todas las plantas pendientes
            if (totalGenerado(p.getNombre()) > totalGenerado(mayor.getNombre())) { // Si la energía generada de p es mayor que la de mayor
                mayor = p; // Actualizo mayor
            }
        }
        resultado.add(mayor.getNombre() + ": " + totalGenerado(mayor.getNombre())); // Agrego el nombre y la energía generada de la planta mayor a la lista de resultados
        pendientes.remove(mayor); // La elimino de la lista de pendientes para no volver a considerarla
    }
    return resultado; // Devuelvo la lista de nombres ordenados por energía generada
}
	    @Override
	    public List<String> plantasInactivasEn(String fechaHora) { // Devuelve una lista de nombres de plantas que estaban inactivas en la fecha y hora indicada
	        LocalDateTime dt = parsearFechaHora(fechaHora); // Parseo la fecha y hora indicada a LocalDateTime
	        List<String> inactivas = new ArrayList<>(); // Lista donde se guardaran los nombres de las plantas inactivas
	        
	        for (String nombre : plantas.keySet()) { // Recorro todas las plantas registradas
	            List<Estado> cambios = estados.get(nombre); // Obtengo la lista de cambios de estado de la planta
	            boolean activa = true; 
	            for (Estado ce : cambios) { // Recorro los cambios de estado de la planta
	                if (!ce.getFechaHora().isAfter(dt)) { // Si el cambio de estado es anterior o igual a la fecha y hora indicada
	                    activa = ce.estaActiva(); // Actualizo el estado de la planta según el cambio de estado
	                }
	            }
	            if (!activa) { // Si la planta estaba inactiva en la fecha y hora indicada, la agrego a la lista de inactivas
	                inactivas.add(nombre); // Agrego el nombre de la planta a la lista de inactivas
	            }
	        }
	        return inactivas;
	    }

	    @Override
	    public String toString() {
	        StringBuilder sb = new StringBuilder();
	        sb.append("SmartGrids System Summary:\n");
	        sb.append("Total Plantas Registradas: ").append(plantas.size()).append("\n");
	        for (Map.Entry<String, Planta> entry : plantas.entrySet()) {
	            sb.append(" - Planta: ").append(entry.getKey())
	              .append(", Activa: ").append(estaActiva(entry.getKey()))
	              .append(", Total Generado: ").append(totalGenerado(entry.getKey())).append("\n");
	        }
	        return sb.toString();
	    }

	// --- Métodos Privados de Validación ---

    private void validarNombreUnico(String nombre) {
        if (plantas.containsKey(nombre)) { // Si ya existe una planta con ese nombre, lanza excepción
            throw new RuntimeException("Ya existe una planta con el nombre: " + nombre); // Validación de nombre único
        }
    }

    private Planta obtenerPlanta(String nombre) {
        Planta p = plantas.get(nombre);// Obtengo la planta del mapa por su nombre
        if (p == null) { // Si no existe, lanza excepción
            throw new RuntimeException("La planta no existe: " + nombre); 
        }
        return p;
    }

    private LocalDateTime parsearFechaHora(String fechaHora) { // Método privado para parsear fecha y hora
        try { // Intento parsear la fechaHora usando LocalDateTime.parse
            return LocalDateTime.parse(fechaHora);
        } catch (Exception e) { // Si falla, lanza una RuntimeException con un mensaje de error
            throw new RuntimeException("Formato de fechaHora inválido: " + fechaHora);
        }
    }

    private LocalDate parsearFecha(String fecha) { // Método privado para parsear solo la fecha
        try {  // Intento parsear la fecha usando LocalDate.parse
            return LocalDate.parse(fecha);
        } catch (Exception e) { // Si falla, lanza una RuntimeException con un mensaje de error
            throw new RuntimeException("Formato de fecha inválido: " + fecha);
        }
    }

	

}
