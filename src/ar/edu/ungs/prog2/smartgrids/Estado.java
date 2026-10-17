package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class Estado {
	    LocalDateTime fechaHora;
	    boolean activa;

	    public Estado(LocalDateTime fechaHora, boolean activa) {
	        this.fechaHora = fechaHora;
	        this.activa = activa;
	    }
	}


