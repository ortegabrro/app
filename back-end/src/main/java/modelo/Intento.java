package modelo;

import java.time.LocalDate;
import java.util.ArrayList;

public class Intento {

	private LocalDate fechaEjecucion;
	private Boolean exito;
	private String mensaje;
	private ArrayList<Cuenta> cuentas;

	public Intento(LocalDate fechaEjecucion, Boolean exito, String mensaje, ArrayList<Cuenta> cuentas) {
		super();
		this.fechaEjecucion = fechaEjecucion;
		this.exito = exito;
		this.mensaje = mensaje;
		this.cuentas = cuentas;
	}

	public LocalDate getFechaEjecucion() {
		return fechaEjecucion;
	}

	public void setFechaEjecucion(LocalDate fechaEjecucion) {
		this.fechaEjecucion = fechaEjecucion;
	}

	public Boolean getExito() {
		return exito;
	}

	public void setExito(Boolean exito) {
		this.exito = exito;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public ArrayList<Cuenta> getCuentas() {
		return cuentas;
	}

	public void setCuentas(ArrayList<Cuenta> cuentas) {
		this.cuentas = cuentas;
	}

}
