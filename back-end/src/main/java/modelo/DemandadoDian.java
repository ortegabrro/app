package modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import enumeraciones.TipoIdentificacion;

public class DemandadoDian extends Demandado{
	
	private String resEmbargo;
	private LocalDate fechaResolucion;
	
	public DemandadoDian(String identificacion, String nombres, String apellidos, TipoIdentificacion tipoIdentificacion,
			BigDecimal montoAEmbargar, String resEmbargo,LocalDate fechaResolucion) {
		super(identificacion, nombres, apellidos, tipoIdentificacion, montoAEmbargar);
		this.resEmbargo = resEmbargo;
		this.fechaResolucion = fechaResolucion;
	}
	
	public String getResEmbargo() {
		return resEmbargo;
	}
	public void setResEmbargo(String resEmbargo) {
		this.resEmbargo = resEmbargo;
	}
	public LocalDate getFechaResolucion() {
		return fechaResolucion;
	}
	public void setFechaResolucion(LocalDate fechaResolucion) {
		this.fechaResolucion = fechaResolucion;
	}
	
	
}
