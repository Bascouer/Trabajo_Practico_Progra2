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
	    public List<String> consultarPlantas() { // Devuelve una lista de strings con el nombre, estado y fecha de puesta en funcionamiento de cada planta
	        List<String> resultado = new ArrayList<>();
	        // 1. Creamos el formateador con el patrón exacto que pide el test
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"); // Formateador para la fecha y hora

	        for (Map.Entry<String, Planta> entry : plantas.entrySet()) { // Recorro todas las plantas registradas
	            String nombre = entry.getKey(); // Obtengo el nombre de la planta
	            Planta p = entry.getValue(); // Obtengo la planta
	            String estadoStr = estaActiva(nombre) ? "ACTIVA" : "INACTIVA"; // Obtengo el estado de la planta y lo convierto a string
	            
	            // 2. Formateamos la fecha usando el formatter en lugar de .toString() 
	            String fechaFormateada = p.getPuestaEnFuncionamiento().format(formatter ); // Formateo la fecha de puesta en funcionamiento de la planta
	            
	            // 3. Armamos el resultado
	            resultado.add(nombre + " (" + estadoStr + "): " + fechaFormateada); 
	        }
	        return resultado; // Devuelvo la lista de strings con el nombre, estado y fecha de puesta en funcionamiento de cada planta
	    }
	    @Override
	    public double capacidadMinimaCuota(String nombre) { // Devuelve la capacidad mínima de cuota de la planta indicada
	        return obtenerPlanta(nombre).calcularCuota(); // Llamo al método calcularCuota() de la planta para obtener la capacidad mínima de cuota
	    }

	    @Override
	    public void activarPlanta(String nombre, String fechaHora) { // Cambia el estado de la planta a activa en la fecha y hora indicada agregando un nuevo Estado a la lista de estados de la planta
	        obtenerPlanta(nombre);
	        LocalDateTime dt = parsearFechaHora(fechaHora);
	        validarOrdenEstado(nombre, dt);
	        estados.get(nombre).add(new Estado(dt, true));
	    }

	    @Override
	    public void desactivarPlanta(String nombre, String fechaHora) { // Cambia el estado de la planta a inactiva en la fecha y hora indicada agregando un nuevo Estado a la lista de estados de la planta
	        obtenerPlanta(nombre);
	        LocalDateTime dt = parsearFechaHora(fechaHora);
	        validarOrdenEstado(nombre, dt);
	        estados.get(nombre).add(new Estado(dt, false));
	    }

	    @Override
	    public boolean estaActiva(String nombre) { // Devuelve true si la planta está activa, false si está inactiva
	        obtenerPlanta(nombre);
	        List<Estado> lista = estados.get(nombre);
	        if (lista.isEmpty()) {
	            return true; 
	        }
	        return lista.get(lista.size() - 1).estaActiva();
	    }


	    private void validarMonitoreoComun(String nombre, String fechaHora, double energia, int incidencias) { //Es llamado para verificar cada tipo de monitoreo
	        Planta p = obtenerPlanta(nombre); // Obtengo la planta por su nombre
	        if (energia < 0 || incidencias < 0) { // Verifico que la energía y las incidencias no sean negativas
	            throw new RuntimeException("Energía e incidencias no pueden ser negativas");
	        }
	        LocalDateTime dt = parsearFechaHora(fechaHora); // Parseo la fecha y hora del monitoreo
	        if (dt.isBefore(p.getPuestaEnFuncionamiento())) { // Verifico que la fecha y hora del monitoreo no sea anterior a la puesta en funcionamiento de la planta
	            throw new RuntimeException("Fecha anterior a la puesta en funcionamiento");
	        }
	        if (!estaActiva(nombre)) { // Verifico si la planta está activa, si no lo está lanzo una excepción
	            throw new RuntimeException("La planta está inactiva");
	        }
	        // Validar duplicado para la misma hora
	        for (Monitoreo m : monitoreos.get(nombre)) { // Recorro todos los monitoreos de la planta
	            if (m.getFechaHora().equals(dt)) { // Si ya existe un monitoreo para la misma fecha y hora, lanzo una excepción
	                throw new RuntimeException("Ya existe un monitoreo para esta hora");
	            }
	        }
	    }

	    @Override
	    public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias) {
	        Planta p = obtenerPlanta(nombre); // Obtengo la planta por su nombre
	        if (p instanceof PlantaTermica || p instanceof PlantaEolica) {// Verifico que la planta no sea térmica ni eólica, si lo es lanzo una excepción
	            throw new RuntimeException("Tipo de planta requiere parámetros adicionales de monitoreo");
	        }
	        validarMonitoreoComun(nombre, fechaHora, energiaGenerada, incidencias); // Valido los parámetros comunes del monitoreo
	        monitoreos.get(nombre).add(new Monitoreo(parsearFechaHora(fechaHora), energiaGenerada, incidencias, 0, 0));
	    }

	    @Override
	    public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias, double velocidadViento) {
	        Planta p = obtenerPlanta(nombre); // Obtengo la planta por su nombre
	        if (!(p instanceof PlantaEolica)) { // Verifico que la planta sea eólica, si no lo es lanzo una excepción
	            throw new RuntimeException("Este método de monitoreo es exclusivo para plantas eólicas");
	        }
	        validarMonitoreoComun(nombre, fechaHora, energiaGenerada, incidencias); // Valido los parámetros comunes del monitoreo
	        monitoreos.get(nombre).add(new Monitoreo(parsearFechaHora(fechaHora), energiaGenerada, incidencias, velocidadViento, 0));
	    }

	    @Override
	    public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias, double co2Emitido, double combustibleUtilizado) {
	        Planta p = obtenerPlanta(nombre); // Obtengo la planta por su nombre
	        if (!(p instanceof PlantaTermica)) { // Verifico que la planta sea térmica, si no lo es lanzo una excepción
	            throw new RuntimeException("Este método de monitoreo es exclusivo para plantas térmicas");
	        }
	        validarMonitoreoComun(nombre, fechaHora, energiaGenerada, incidencias); // Valido los parámetros comunes del monitoreo
	        monitoreos.get(nombre).add(new Monitoreo(parsearFechaHora(fechaHora), energiaGenerada, incidencias, co2Emitido, combustibleUtilizado)); // Agrego el monitoreo a la lista de monitoreos de la planta
	    }

	    @Override
	    public double totalGenerado(String nombre) { // Devuelve la suma de la energía generada por todos los monitoreos de la planta indicada
	        obtenerPlanta(nombre); // Verifico que la planta exista
	        double total = 0; // Inicializo el total en 0
	        for (Monitoreo m : monitoreos.get(nombre)) { // Recorro todos los monitoreos de la planta
	            total += m.getEnergia();// Sumo la energía generada del monitoreo al total
	        }
	        return total; // Devuelvo el total de energía generada por la planta
	    }

	    @Override
	    public int[] incidenciasPorHora(String nombre) { // Devuelve un arreglo de 24 posiciones con la cantidad de incidencias por hora para la planta indicada
	        obtenerPlanta(nombre); // Verifico que la planta exista
	        int[] resultado = new int[24]; // Inicializo el arreglo de 24 posiciones con ceros
	        for (Monitoreo m : monitoreos.get(nombre)) { // Recorro todos los monitoreos de la planta
	            resultado[m.getFechaHora().getHour()] += m.getIncidencias(); // Sumo las incidencias del monitoreo a la posición correspondiente a la hora del monitoreo
	        }
	        return resultado; // Devuelvo el arreglo con la cantidad de incidencias por hora
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
	        List<String> resultado = new ArrayList<>(); // Lista donde se guardaran los nombres de las plantas que nunca cumplieron la cuota
	        for (Map.Entry<String, Planta> entry : plantas.entrySet()) { // Recorro todas las plantas registradas
	            String nombre = entry.getKey(); // Obtengo el nombre de la planta
	            Planta p = entry.getValue(); // Obtengo la planta
	            List<Monitoreo> lista = monitoreos.get(nombre); // Obtengo la lista de monitoreos de la planta
	            boolean cumplio = false; // Booleano para indicar si la planta cumplió la cuota en algún monitoreo
	            for (Monitoreo m : lista) { // Recorro todos los monitoreos de la planta
	                if (p.cumpleCuota(m)) { // Si la planta cumplió la cuota en algún monitoreo, actualizo el booleano y salgo del bucle
	                    cumplio = true; // Actualizo el booleano
	                    break; // Salgo del bucle ya que no necesito seguir buscando
	                }
	            }
	            if (!cumplio) { // Si la planta nunca cumplió la cuota, la agrego a la lista de resultados
	                resultado.add(nombre);
	            }
	        }
	        return resultado; // Devuelvo la lista de nombres de plantas que nunca cumplieron la cuota
	    }

	@Override
public String horasSinVelocidadMinimaPorPlantaEolica() {
    StringBuilder sb = new StringBuilder(); // StringBuilder para construir el resultado
    sb.append("Plantas Eolicas: Horas con poco viento\n"); // Encabezado del resultado

    for (Planta p : plantas.values()) { // Recorro todas las plantas registradas
        if (p instanceof PlantaEolica) { // Verifico si la planta es eólica
            PlantaEolica pe = (PlantaEolica) p; // Cast a PlantaEolica para acceder a sus métodos específicos

            // para cada hora: la mayor velocidad registrada y si hubo algún registro 
            double[] mayorViento = new double[24]; // Array para almacenar la mayor velocidad de viento por hora
            boolean[] hayRegistro = new boolean[24]; // Array para indicar si hubo algún registro de monitoreo en esa hora

            for (Monitoreo m : monitoreos.get(pe.getNombre())) { // Recorro todos los monitoreos de la planta eólica
                int hora = m.getFechaHora().getHour(); // Obtengo la hora del monitoreo
                if (!hayRegistro[hora] || m.getExtra1() > mayorViento[hora]) { // Si no hay registro o la velocidad del monitoreo es mayor que la registrada
                    mayorViento[hora] = m.getExtra1(); // Actualizo la mayor velocidad de viento registrada para esa hora
                }
                hayRegistro[hora] = true; // Marco que hubo un registro para esa hora
            }

            sb.append("- ").append(pe.getNombre()).append(": "); // Agrego el nombre de la planta eólica al resultado
            sb.append(pe.getVelocidadMinimaViento()).append(" m/s\n"); // Agrego la velocidad mínima de viento al resultado

            // se listan las horas donde ni la mejor velocidad llegó al mínimo
            for (int hora = 0; hora < 24; hora++) {  // Recorro todas las horas del día
                if (hayRegistro[hora] && mayorViento[hora] < pe.getVelocidadMinimaViento()) { // Si hubo registro y la mayor velocidad de viento registrada es menor que la mínima requerida
                    sb.append("        ");
                    if (hora < 10) { // Formateo para horas de un dígito
                        sb.append("0"); // Agrego un cero a la izquierda para horas de un dígito
                    }
                    sb.append(hora).append(": ").append(mayorViento[hora]).append(" m/s\n"); // Agrego la hora y la mayor velocidad de viento registrada para esa hora al resultado
                }
            }
        }
    }
    return sb.toString(); // Devuelvo el resultado como un string
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
    public String toString() { // Devuelve un string con la información de todas las plantas registradas, sus estados y monitoreos
        String resultado = "SmartGrids - plantas registradas: " + plantas.size() + "\n";

    for (Planta p : plantas.values()) {
        String nombre = p.getNombre();

        resultado += "\n" + p + "\n";
        resultado += "  Activa: " + estaActiva(nombre) + "\n";
        resultado += "  Total generado: " + totalGenerado(nombre) + "\n";

        resultado += "  Cambios de estado:\n";
        for (Estado e : estados.get(nombre)) {
            resultado += "    - " + e + "\n";
        }

        resultado += "  Monitoreos:\n";
        for (Monitoreo m : monitoreos.get(nombre)) {
            resultado += "    - " + m + "\n";
        }
    }
    return resultado;
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
    private void validarOrdenEstado(String nombre, LocalDateTime fechaHora) {
        Planta p = obtenerPlanta(nombre);
        if (fechaHora.isBefore(p.getPuestaEnFuncionamiento())) {
            throw new RuntimeException("La fecha de cambio de estado es anterior a la puesta en funcionamiento");
        }
        List<Estado> lista = estados.get(nombre);
        if (!lista.isEmpty()) {
            LocalDateTime ultimaFecha = lista.get(lista.size() - 1).getFechaHora();
            if (fechaHora.isBefore(ultimaFecha)) {
                throw new RuntimeException("La fecha del estado no puede ser anterior al último estado registrado");
            }
        }
    }
    

}
