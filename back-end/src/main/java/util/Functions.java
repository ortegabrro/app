package util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import modelo.*;

public class Functions {

	public static void embargarCuentasAhorroJudicialLimite(Demandado demandado, ArrayList<Cuenta> cuentas,
			BigDecimal limite) {

		BigDecimal montoPorEmbargar;
		if (demandado.getMontoAEmbargar().compareTo(demandado.getMontoEmbargado()) == 1) {
			BigDecimal monto = demandado.getMontoAEmbargar().subtract(demandado.getMontoEmbargado());
			if (monto.compareTo(new BigDecimal(0)) > 0) {
				montoPorEmbargar = monto;
			} else {
				montoPorEmbargar = demandado.getMontoAEmbargar();
			}

			BigDecimal saldoExcedente = cuentas.stream().map(Cuenta::getSaldoCuentaFecha).reduce(BigDecimal::add)
					.orElse(null);

			if (saldoExcedente != null) {
				if (saldoExcedente.compareTo(limite) > 0) {
					BigDecimal montoSinBenificio = saldoExcedente.subtract(limite);
					BigDecimal montoAEmbargar;
					if (montoPorEmbargar.compareTo(montoSinBenificio) > 0) {
						montoAEmbargar = montoSinBenificio;
					} else {
						montoAEmbargar = montoPorEmbargar;
					}
					for (int i = 0; i < cuentas.size() && montoAEmbargar.compareTo(new BigDecimal(0)) > 0; i++) {
						Cuenta cuenta = cuentas.get(i);
						if (montoAEmbargar.compareTo(cuenta.getSaldoCuentaFecha()) > 0) {
							cuenta.setMontoEmbargado(cuenta.getSaldoCuentaFecha());
							cuenta.getReglas().add("Carta circular 64 de 2018 por la SuperFinanciera");
							demandado
									.setMontoEmbargado(cuenta.getSaldoCuentaFecha().add(demandado.getMontoEmbargado()));
							montoAEmbargar = montoAEmbargar.subtract(cuenta.getSaldoCuentaFecha());
							cuenta.setSaldoCuentaFecha(
									cuenta.getSaldoCuentaFecha().subtract(cuenta.getMontoEmbargado()));
						} else {
							cuenta.setMontoEmbargado(montoAEmbargar);
							cuenta.getReglas().add("Carta circular 64 de 2018 por la SuperFinanciera");
							demandado.setMontoEmbargado(montoAEmbargar.add(demandado.getMontoEmbargado()));
							cuenta.setSaldoCuentaFecha(
									cuenta.getSaldoCuentaFecha().subtract(cuenta.getMontoEmbargado()));
							montoAEmbargar = montoAEmbargar.subtract(montoAEmbargar);
						}
					}
				} else {
					cuentas.stream()
							.forEach(c -> c.getReglas().add("Carta circular 64 de 2018 por la SuperFinanciera"));
				}
			}
		}
	}

	public static void embargarCuentasAhorroCoactivoLimite(Demandado demandado, ArrayList<Cuenta> cuentas,
			BigDecimal limite) {

		BigDecimal montoAEmbargar;
		if (demandado.getMontoAEmbargar().compareTo(demandado.getMontoEmbargado()) == 1) {
			BigDecimal montoPorEmbargar = demandado.getMontoAEmbargar().subtract(demandado.getMontoEmbargado());
			if (montoPorEmbargar.compareTo(new BigDecimal(0)) > 0) {
				montoAEmbargar = montoPorEmbargar;
			} else {
				montoAEmbargar = demandado.getMontoAEmbargar();
			}

			Comparator<Cuenta> comparator = Comparator.comparing(Cuenta::getFechaCreacion);
			Cuenta cuentaProtegida = cuentas.stream().min(comparator).orElse(null);

			for (int i = 0; i < cuentas.size() && montoAEmbargar.compareTo(new BigDecimal(0)) > 0; i++) {
				Cuenta cuenta = cuentas.get(i);
				cuenta.getReglas().add("Carta circular 65 de 2018 por la SuperFinanciera");
				if (cuenta.equals(cuentaProtegida)) {
					if (cuenta.getSaldoCuentaFecha().compareTo(limite) > 0) {
						BigDecimal montoDisponible = cuenta.getSaldoCuentaFecha().subtract(limite);
						if (montoAEmbargar.compareTo(montoDisponible) > 0) {
							cuenta.setMontoEmbargado(montoDisponible);
							demandado.setMontoEmbargado(montoDisponible.add(demandado.getMontoEmbargado()));
							montoAEmbargar = montoAEmbargar.subtract(montoDisponible);
							cuenta.setSaldoCuentaFecha(
									cuenta.getSaldoCuentaFecha().subtract(cuenta.getMontoEmbargado()));
						} else {
							cuenta.setMontoEmbargado(montoAEmbargar);
							demandado.setMontoEmbargado(montoAEmbargar.add(demandado.getMontoEmbargado()));
							montoAEmbargar = montoAEmbargar.subtract(montoAEmbargar);
							cuenta.setSaldoCuentaFecha(
									cuenta.getSaldoCuentaFecha().subtract(cuenta.getMontoEmbargado()));
						}
					}
				} else {
					if (montoAEmbargar.compareTo(cuenta.getSaldoCuentaFecha()) > 0) {
						cuenta.setMontoEmbargado(cuenta.getSaldoCuentaFecha());
						demandado.setMontoEmbargado(cuenta.getSaldoCuentaFecha().add(demandado.getMontoEmbargado()));
						montoAEmbargar = montoAEmbargar.subtract(cuenta.getSaldoCuentaFecha());
						cuenta.setSaldoCuentaFecha(cuenta.getSaldoCuentaFecha().subtract(cuenta.getMontoEmbargado()));
					} else {
						cuenta.setMontoEmbargado(montoAEmbargar);
						demandado.setMontoEmbargado(montoAEmbargar.add(demandado.getMontoEmbargado()));
						montoAEmbargar = montoAEmbargar.subtract(montoAEmbargar);
						cuenta.setSaldoCuentaFecha(cuenta.getSaldoCuentaFecha().subtract(cuenta.getMontoEmbargado()));
					}
				}
			}
		}
	}

	public static void embargarCuentasLimite(Demandado demandado, ArrayList<Cuenta> cuentas, ArrayList<String> reglas,
			BigDecimal limite) {

		if (demandado.getMontoAEmbargar().compareTo(demandado.getMontoEmbargado()) == 1) {
			BigDecimal montoAEmbargar;
			BigDecimal montoPorEmbargar = demandado.getMontoAEmbargar().subtract(demandado.getMontoEmbargado());
			if (montoPorEmbargar.compareTo(new BigDecimal(0)) > 0) {
				montoAEmbargar = montoPorEmbargar;
			} else {
				montoAEmbargar = demandado.getMontoAEmbargar();
			}
			for (int i = 0; i < cuentas.size() && montoAEmbargar.compareTo(new BigDecimal(0)) > 0; i++) {
				Cuenta cuenta = cuentas.get(i);
				if (cuenta.getSaldoCuentaFecha().compareTo(limite) > 0) {
					BigDecimal monto = cuenta.getSaldoCuentaFecha().subtract(limite);
					if (montoAEmbargar.compareTo(monto) > 0) {
						cuenta.setMontoEmbargado(monto);
						demandado.setMontoEmbargado(monto.add(demandado.getMontoEmbargado()));
						for (String regla : reglas) {
							cuenta.getReglas().add(regla);
						}
						montoAEmbargar = montoAEmbargar.subtract(monto);
						cuenta.setSaldoCuentaFecha(cuenta.getSaldoCuentaFecha().subtract(cuenta.getMontoEmbargado()));
					} else {
						cuenta.setMontoEmbargado(montoAEmbargar);
						demandado.setMontoEmbargado(montoAEmbargar.add(demandado.getMontoEmbargado()));
						for (String regla : reglas) {
							cuenta.getReglas().add(regla);
						}
						cuenta.setSaldoCuentaFecha(cuenta.getSaldoCuentaFecha().subtract(cuenta.getMontoEmbargado()));
						montoAEmbargar = montoAEmbargar.subtract(montoAEmbargar);
					}
				} else {
					for (String regla : reglas) {
						cuenta.getReglas().add(regla);
					}
				}
			}
		}
	}
	
	public static void agregarLeyes(ArrayList<Cuenta> cuentas) {
    	for(Cuenta o:cuentas){
    		o.getReglas().add("Artículo 25 ley 1751 de 2015 ");
	    	o.getReglas().add("Decreto 780 de 2016 ARTÍCULO 2.6.4.1.4 ");
	    	o.getReglas().add("Numeral 1 Artículo 594 código general del proceso Ley 1564 de 2012 ");
	    	o.getReglas().add("Numeral 4 Artículo 594 código general del proceso Ley 1564 de 2012 ");
	    	o.getReglas().add("Numeral 5 Artículo 594 código general del proceso Ley 1564 de 2012 ");
	    	o.getReglas().add("Artículo 91 de la Ley 715 de 2001");
	    	o.getReglas().add("Artículo 8 Decreto 050 de 2003");
    	}  
	}

}
