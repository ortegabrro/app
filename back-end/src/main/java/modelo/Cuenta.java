package modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import enumeraciones.EstadoCuenta;
import enumeraciones.SubtipoCuenta;
import enumeraciones.TipoCuenta;

public class Cuenta {

	private String idCuenta;
	private String idBanco;
	private TipoCuenta tipoCuenta;
	private SubtipoCuenta subTipoCuenta;
	private LocalDate fechaCreacion;
	private BigDecimal montoEmbargado;
	private BigDecimal saldoCuentaFecha;
	private EstadoCuenta estado;
	private ArrayList<String> reglas;

	public Cuenta(String idCuenta, String idBanco, TipoCuenta tipoCuenta, SubtipoCuenta subTipoCuenta,
			LocalDate fechaCreacion, BigDecimal saldoCuentaFecha, EstadoCuenta estado) {
		super();
		this.idCuenta = idCuenta;
		this.idBanco = idBanco;
		this.tipoCuenta = tipoCuenta;
		this.subTipoCuenta = subTipoCuenta;
		this.fechaCreacion = fechaCreacion;
		this.montoEmbargado = new BigDecimal(0);
		this.saldoCuentaFecha = saldoCuentaFecha;
		this.estado = estado;
		this.reglas = new ArrayList<>();
	}

	public String getIdCuenta() {
		return idCuenta;
	}

	public void setIdCuenta(String idCuenta) {
		this.idCuenta = idCuenta;
	}

	public String getIdBanco() {
		return idBanco;
	}

	public void setIdBanco(String idPersona) {
		this.idBanco = idPersona;
	}

	public TipoCuenta getTipoCuenta() {
		return tipoCuenta;
	}

	public void setTipoCuenta(TipoCuenta tipo) {
		this.tipoCuenta = tipo;
	}

	public SubtipoCuenta getSubTipoCuenta() {
		return subTipoCuenta;
	}

	public void setSubTipoCuenta(SubtipoCuenta subtipo) {
		this.subTipoCuenta = subtipo;
	}

	public LocalDate getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDate fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public BigDecimal getMontoEmbargado() {
		return montoEmbargado;
	}

	public void setMontoEmbargado(BigDecimal montoEmbargado) {
		this.montoEmbargado = montoEmbargado;
	}

	public BigDecimal getSaldoCuentaFecha() {
		return saldoCuentaFecha;
	}

	public void setSaldoCuentaFecha(BigDecimal saldoCuentaFecha) {
		this.saldoCuentaFecha = saldoCuentaFecha;
	}

	public EstadoCuenta getEstado() {
		return estado;
	}

	public void setEstado(EstadoCuenta estado) {
		this.estado = estado;
	}

	public ArrayList<String> getReglas() {
		return reglas;
	}

	public void setReglas(ArrayList<String> reglas) {
		this.reglas = reglas;
	}

}
