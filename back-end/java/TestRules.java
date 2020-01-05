import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;

import util.*;
import modelo.*;
import enumeraciones.*;
import datos.*;

public class TestRules {

	static KieContainer kieContainer;
	StatelessKieSession sessionStateless = null;
	KieSession sessionStatefull = null;

	@BeforeClass
	public static void beforeClass() {
		kieContainer = KnowledgeSessionHelper.createRuleBase();
	}

	@Test
	public void testCuentasJudicialLimite() {

		// Cuentas Demandado1
		ArrayList<Cuenta> cuentasD1 = new ArrayList<>();
		cuentasD1.add(new Cuenta("idCuenta1", "idBanco1", TipoCuenta.AHORROS, SubtipoCuenta.BASICA,
				LocalDate.of(2016, 05, 26), new BigDecimal(35000000), EstadoCuenta.ACTIVA));
		cuentasD1.add(new Cuenta("idCuenta2", "idBanco1", TipoCuenta.AHORROS, SubtipoCuenta.BASICA,
				LocalDate.of(2016, 05, 26), new BigDecimal(3000000), EstadoCuenta.ACTIVA));
		cuentasD1.add(new Cuenta("idCuenta3", "idBanco1", TipoCuenta.CORRIENTE, SubtipoCuenta.BASICA,
				LocalDate.of(2016, 05, 26), new BigDecimal(2000000), EstadoCuenta.ACTIVA));

		// Cuentas Demandado2
		ArrayList<Cuenta> cuentasD2 = new ArrayList<>();
		cuentasD2.add(new Cuenta("idCuenta4", "idBanco1", TipoCuenta.AHORROS, SubtipoCuenta.BASICA,
				LocalDate.of(2016, 05, 26), new BigDecimal(3000000), EstadoCuenta.ACTIVA));
		cuentasD2.add(new Cuenta("idCuenta5", "idBanco1", TipoCuenta.CDT, SubtipoCuenta.BASICA,
				LocalDate.of(2016, 05, 26), new BigDecimal(1000000), EstadoCuenta.ACTIVA));
		cuentasD2.add(new Cuenta("idCuenta6", "idBanco1", TipoCuenta.CDAT, SubtipoCuenta.BASICA,
				LocalDate.of(2016, 05, 26), new BigDecimal(1000000), EstadoCuenta.ACTIVA));
		cuentasD2.add(new Cuenta("idCuenta7", "idBanco1", TipoCuenta.ELECTRONICOS, SubtipoCuenta.PUBLICO,
				LocalDate.of(2016, 05, 26), new BigDecimal(1000000), EstadoCuenta.ACTIVA));

		// Cuentas Demandado3
		ArrayList<Cuenta> cuentasD3 = new ArrayList<>();
		cuentasD3.add(new Cuenta("idCuenta8", "idBanco1", TipoCuenta.AHORROS, SubtipoCuenta.BASICA,
				LocalDate.of(2014, 05, 26), new BigDecimal(37000000), EstadoCuenta.ACTIVA));
		cuentasD3.add(new Cuenta("idCuenta9", "idBanco1", TipoCuenta.AHORROS, SubtipoCuenta.BASICA,
				LocalDate.of(2015, 05, 26), new BigDecimal(3000000), EstadoCuenta.ACTIVA));

		ArrayList<Demandado> demandados = new ArrayList<>();

		demandados.add(
				new Demandado("idDemandado1", "", "", TipoIdentificacion.NATURAL, new BigDecimal(6000000), cuentasD1));
		demandados.add(
				new Demandado("idDemandado2", "", "", TipoIdentificacion.NATURAL, new BigDecimal(6000000), cuentasD2));
		demandados.add(
				new Demandado("idDemandado3", "", "", TipoIdentificacion.JURIDICA, new BigDecimal(6000000), cuentasD3));

		Embargo embargo=null;
//		Embargo embargo = new EmbargoJudicial("e1", "", "", "", LocalDate.of(2017, 11, 24), TipoEmbargo.JUDICIAL,
//				new BigDecimal(18000000), "", "", "", null, demandados);

		//
		sessionStatefull = KnowledgeSessionHelper.getStatefulKnowledgeSession(kieContainer, "ksession-rules");
		Data datos = new Data();
		sessionStatefull.setGlobal("datos", datos);
		sessionStatefull.insert(embargo);
		sessionStatefull.fireAllRules();
		imprimir(embargo);
	}

	@Test
	public void testCuentasCoactivoLimite() {

		// Cuentas Demandado1
		ArrayList<Cuenta> cuentasD1 = new ArrayList<>();
		cuentasD1.add(new Cuenta("idCuenta1", "idBanco1", TipoCuenta.AHORROS, SubtipoCuenta.BASICA,
				LocalDate.of(2014, 05, 26), new BigDecimal(17000000), EstadoCuenta.ACTIVA));
		cuentasD1.add(new Cuenta("idCuenta2", "idBanco1", TipoCuenta.AHORROS, SubtipoCuenta.BASICA,
				LocalDate.of(2015, 05, 26), new BigDecimal(3000000), EstadoCuenta.ACTIVA));
		cuentasD1.add(new Cuenta("idCuenta3", "idBanco1", TipoCuenta.CORRIENTE, SubtipoCuenta.BASICA,
				LocalDate.of(2016, 05, 26), new BigDecimal(2000000), EstadoCuenta.ACTIVA));

		// Cuentas Demandado2
		ArrayList<Cuenta> cuentasD2 = new ArrayList<>();
		cuentasD2.add(new Cuenta("idCuenta4", "idBanco1", TipoCuenta.AHORROS, SubtipoCuenta.BASICA,
				LocalDate.of(2016, 05, 26), new BigDecimal(3000000), EstadoCuenta.ACTIVA));
		cuentasD2.add(new Cuenta("idCuenta5", "idBanco1", TipoCuenta.CDT, SubtipoCuenta.BASICA,
				LocalDate.of(2016, 05, 26), new BigDecimal(1000000), EstadoCuenta.ACTIVA));
		cuentasD2.add(new Cuenta("idCuenta6", "idBanco1", TipoCuenta.CDAT, SubtipoCuenta.BASICA,
				LocalDate.of(2016, 05, 26), new BigDecimal(1000000), EstadoCuenta.ACTIVA));
		cuentasD2.add(new Cuenta("idCuenta7", "idBanco1", TipoCuenta.ELECTRONICOS, SubtipoCuenta.PUBLICO,
				LocalDate.of(2016, 05, 26), new BigDecimal(1000000), EstadoCuenta.ACTIVA));

		// Cuentas Demandado3
		ArrayList<Cuenta> cuentasD3 = new ArrayList<>();
		cuentasD3.add(new Cuenta("idCuenta8", "idBanco1", TipoCuenta.AHORROS, SubtipoCuenta.BASICA,
				LocalDate.of(2014, 05, 26), new BigDecimal(37000000), EstadoCuenta.ACTIVA));
		cuentasD3.add(new Cuenta("idCuenta9", "idBanco1", TipoCuenta.AHORROS, SubtipoCuenta.BASICA,
				LocalDate.of(2015, 05, 26), new BigDecimal(3000000), EstadoCuenta.ACTIVA));

		ArrayList<Demandado> demandados = new ArrayList<>();

		demandados.add(
				new Demandado("idDemandado1", "", "", TipoIdentificacion.NATURAL, new BigDecimal(6000000), cuentasD1));
		demandados.add(
				new Demandado("idDemandado2", "", "", TipoIdentificacion.NATURAL, new BigDecimal(6000000), cuentasD2));
		demandados.add(
				new Demandado("idDemandado3", "", "", TipoIdentificacion.JURIDICA, new BigDecimal(6000000), cuentasD3));

		Embargo embargo=null;
//		Embargo embargo = new EmbargoJudicial("e1", "", "", "", LocalDate.of(2018, 11, 24), TipoEmbargo.COACTIVO,
//				new BigDecimal(18000000), "", "", "", null, demandados);

		//
		sessionStatefull = KnowledgeSessionHelper.getStatefulKnowledgeSession(kieContainer, "ksession-rules");
		Data datos = new Data();
		sessionStatefull.setGlobal("datos", datos);
		sessionStatefull.insert(embargo);
		sessionStatefull.fireAllRules();
		imprimir(embargo);
	}

	@Test
	public void testCuentasAlimentos() {

		// Cuentas Demandado1
		ArrayList<Cuenta> cuentasD1 = new ArrayList<>();
		cuentasD1.add(new Cuenta("idCuenta1", "idBanco1", TipoCuenta.AHORROS, SubtipoCuenta.BASICA,
				LocalDate.of(2014, 05, 26), new BigDecimal(35000000), EstadoCuenta.ACTIVA));
		cuentasD1.add(new Cuenta("idCuenta2", "idBanco1", TipoCuenta.AHORROS, SubtipoCuenta.BASICA,
				LocalDate.of(2015, 05, 26), new BigDecimal(3000000), EstadoCuenta.ACTIVA));
		cuentasD1.add(new Cuenta("idCuenta3", "idBanco1", TipoCuenta.CORRIENTE, SubtipoCuenta.BASICA,
				LocalDate.of(2016, 05, 26), new BigDecimal(2000000), EstadoCuenta.ACTIVA));

		// Cuentas Demandado2
		ArrayList<Cuenta> cuentasD2 = new ArrayList<>();
		cuentasD2.add(new Cuenta("idCuenta4", "idBanco1", TipoCuenta.AHORROS, SubtipoCuenta.BASICA,
				LocalDate.of(2016, 05, 26), new BigDecimal(3000000), EstadoCuenta.ACTIVA));
		cuentasD2.add(new Cuenta("idCuenta5", "idBanco1", TipoCuenta.CDT, SubtipoCuenta.BASICA,
				LocalDate.of(2016, 05, 26), new BigDecimal(1000000), EstadoCuenta.ACTIVA));
		cuentasD2.add(new Cuenta("idCuenta6", "idBanco1", TipoCuenta.CDAT, SubtipoCuenta.BASICA,
				LocalDate.of(2016, 05, 26), new BigDecimal(1000000), EstadoCuenta.ACTIVA));
		cuentasD2.add(new Cuenta("idCuenta7", "idBanco1", TipoCuenta.ELECTRONICOS, SubtipoCuenta.PUBLICO,
				LocalDate.of(2016, 05, 26), new BigDecimal(1000000), EstadoCuenta.ACTIVA));

		// Cuentas Demandado3
		ArrayList<Cuenta> cuentasD3 = new ArrayList<>();
		cuentasD3.add(new Cuenta("idCuenta8", "idBanco1", TipoCuenta.AHORROS, SubtipoCuenta.BASICA,
				LocalDate.of(2014, 05, 26), new BigDecimal(37000000), EstadoCuenta.ACTIVA));
		cuentasD3.add(new Cuenta("idCuenta9", "idBanco1", TipoCuenta.AHORROS, SubtipoCuenta.BASICA,
				LocalDate.of(2015, 05, 26), new BigDecimal(3000000), EstadoCuenta.ACTIVA));

		ArrayList<Demandado> demandados = new ArrayList<>();

		demandados.add(
				new Demandado("idDemandado1", "", "", TipoIdentificacion.NATURAL, new BigDecimal(6000000), cuentasD1));
		demandados.add(
				new Demandado("idDemandado2", "", "", TipoIdentificacion.NATURAL, new BigDecimal(6000000), cuentasD2));
		demandados.add(
				new Demandado("idDemandado3", "", "", TipoIdentificacion.JURIDICA, new BigDecimal(6000000), cuentasD3));

		Embargo embargo=null;
//		Embargo embargo = new EmbargoJudicial("e1", "", "", "", LocalDate.of(2018, 11, 24), TipoEmbargo.FAMILIAR,
//				new BigDecimal(18000000), "", "", "", null, demandados);

		//
		sessionStatefull = KnowledgeSessionHelper.getStatefulKnowledgeSession(kieContainer, "ksession-rules");
		Data datos = new Data();
		sessionStatefull.setGlobal("datos", datos);
		sessionStatefull.insert(embargo);
		sessionStatefull.fireAllRules();
		imprimir(embargo);
	}

	public void imprimir(Embargo embargo) {

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
	}

}
