package modelo;

import enumeraciones.TipoIdentificacion;

public class Demandante extends Persona{

	public Demandante(String identificacion, String nombres, String apellidos, TipoIdentificacion tipoIdentificacion) {
		super(identificacion, nombres, apellidos, tipoIdentificacion);
	}

	public Demandante() {
		// TODO Auto-generated constructor stub
	}
}
