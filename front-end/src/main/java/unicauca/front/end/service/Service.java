package unicauca.front.end.service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import enumeraciones.EstadoCuenta;
import modelo.Cuenta;
import modelo.Demandado;
import modelo.Embargo;

public class Service {
	
	public String imprimir(Embargo embargo) {
		// Create a stream to hold the output
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		PrintStream old = System.out;
		System.setOut(ps);
		System.out.println(
				"Embargo de tipo: " + embargo.getTipoEmbargo() + " del: " + embargo.getFechaOficio());
		System.out.println("=============================================");
		for (Demandado demandado : embargo.getDemandados()) {
			System.out.println("Persona: " + demandado.getTipoIdentificacion() + " con identificacion: "
					+ demandado.getIdentificacion());
			System.out.println("Con un monto a Embargar de: " + demandado.getMontoAEmbargar());
			System.out.println("La(s) siguiente(s) cuenta(s)  ");
			System.out.println("-------------------------------------------------");

			if (demandado.getMontoAEmbargar().compareTo(demandado.getMontoEmbargado()) == 1) {
				demandado.getCuentas().stream().forEach(c -> c.setEstado(EstadoCuenta.BLOQUEADA));
			}

			for (Cuenta cuenta : demandado.getCuentas()) {
				if (demandado.getMontoEmbargado().compareTo(new BigDecimal(0)) > 0) {
					System.out.println("La cuenta:" + cuenta.getIdCuenta() + " de:" + cuenta.getTipoCuenta() + " de "
							+ cuenta.getSubTipoCuenta());
					System.out.println("Fecha de creacion: " + cuenta.getFechaCreacion());
					System.out.println("Embargada por un monto de: " + cuenta.getMontoEmbargado());
					System.out.println("Con saldo a la fecha de:" + cuenta.getSaldoCuentaFecha());
					System.out.println(" Estado de la cuenta: " + cuenta.getEstado());
					System.out.println("  en base a las siguientes leyes:");
				} else {
					System.out.println("La cuenta:" + cuenta.getIdCuenta() + " de:" + cuenta.getTipoCuenta() + " de "
							+ cuenta.getSubTipoCuenta());
					System.out.println("Con saldo a la fecha de:" + cuenta.getSaldoCuentaFecha());
					System.out.println("No se puede embargar");
					System.out.println("Estado de la cuenta: " + cuenta.getEstado());
					if (cuenta.getSaldoCuentaFecha().compareTo(new BigDecimal(0)) == 0) {
						System.out.println("Saldo insuficiente");
					}
					System.out.println("  en base a las siguientes leyes:");
				}
				for (String regla : cuenta.getReglas()) {
					System.out.println("\t" + regla);
				}
				System.out.println("-------------------------------------------------");

			}
			BigDecimal montoPorEmbargar = demandado.getMontoAEmbargar().subtract(demandado.getMontoEmbargado());
			if (montoPorEmbargar.compareTo(new BigDecimal(0)) == 1) {
				System.out.println("La(s) cuenta(s) de la Persona con identificacion: " + demandado.getIdentificacion()
						+ " fueron bloqueada(s)");
				System.out.println("Por un faltante por embargar de: " + montoPorEmbargar);
			}
			System.out.println("=============================================");
		}
		System.out.flush();
		System.setOut(old);
		return baos.toString();
	}
	
	
	
}
