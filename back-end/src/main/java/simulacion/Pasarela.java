package simulacion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import enumeraciones.*;
import enumeraciones.EstadoCuenta;
import modelo.Cuenta;

public class Pasarela {
	String idBanco;

	public Pasarela(String nombre) {
		super();
		this.idBanco = nombre;
	}

	public ArrayList<Cuenta> consultaCuentas() {
		int randomNum = ThreadLocalRandom.current().nextInt(0, 2 + 1);
		ArrayList<Cuenta> cuentas = new ArrayList<>();
		for (int i = 0; i < randomNum; i++) {
			long minDay = LocalDate.of(1970, 1, 1).toEpochDay();
			long maxDay = LocalDate.of(2015, 12, 31).toEpochDay();
			long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
			BigDecimal min = new BigDecimal(0);
			BigDecimal max = new BigDecimal(70000000);			
			
			String idCuenta=Integer.toString(ThreadLocalRandom.current().nextInt(0, 99999999));				
			TipoCuenta tipoCuenta= TipoCuenta.values()[ThreadLocalRandom.current().nextInt(0, TipoCuenta.values().length)];
			SubtipoCuenta subTipoCuenta= SubtipoCuenta.values()[ThreadLocalRandom.current().nextInt(0, SubtipoCuenta.values().length)];
			LocalDate fechaCreacion = LocalDate.ofEpochDay(randomDay);
			BigDecimal saldoCuentaFecha = min.add(new BigDecimal(Math.random()).multiply(max.subtract(min))).setScale(2,BigDecimal.ROUND_HALF_UP);
			EstadoCuenta estado= EstadoCuenta.values()[ThreadLocalRandom.current().nextInt(0, EstadoCuenta.values().length)];
			
			cuentas.add(new Cuenta(idCuenta, idBanco, tipoCuenta, subTipoCuenta, fechaCreacion, saldoCuentaFecha, estado));
		}
		return cuentas;
	}
	
	public String getNombre() {
		return idBanco;
	}

	public void setNombre(String nombre) {
		this.idBanco = nombre;
	}

}
