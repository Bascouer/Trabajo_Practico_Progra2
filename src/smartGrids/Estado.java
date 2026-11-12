package smartGrids;

import java.time.LocalDateTime;

public class Estado {
	private LocalDateTime fechaHora;
	private boolean activa;

	public Estado(LocalDateTime fechaHora, boolean activa) {
		this.fechaHora = fechaHora;
		this.activa = activa;
	}

	public LocalDateTime getFechaHora() {
		return fechaHora;
	}

	public boolean estaActiva() {
		return activa;
	}

	@Override
	public String toString() {
		return "Estado [fechaHora=" + fechaHora + ", activa=" + activa + "]";
	}

}
