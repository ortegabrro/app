package simulacion;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import modelo.Demandado;

public class SimulacionPasarelas {
	
	ArrayList<Pasarela> pasarelas;

	public SimulacionPasarelas() {
		super();
		pasarelas=new ArrayList<Pasarela>();
		pasarelas.add(new Pasarela("BANCO DE BOGOT�"));
		pasarelas.add(new Pasarela("BANCO POPULAR"));
		pasarelas.add(new Pasarela("BANCO CORPBANCA COLOMBIA S.A."));
		pasarelas.add(new Pasarela("BANCOLOMBIA S.A."));
		pasarelas.add(new Pasarela("CITIBANK COLOMBIA"));
		pasarelas.add(new Pasarela("BANCO GNB COLOMBIA S.A."));
		pasarelas.add(new Pasarela("BANCO GNB SUDAMERIS COLOMBIA"));
		pasarelas.add(new Pasarela("BBVA COLOMBIA"));
		pasarelas.add(new Pasarela("HELM BANK"));
		pasarelas.add(new Pasarela("RED MULTIBANCA COLPATRIA S.A."));
		pasarelas.add(new Pasarela("BANCO DE OCCIDENTE"));
		pasarelas.add(new Pasarela("BANCO CAJA SOCIAL - BCSC S.A."));
		pasarelas.add(new Pasarela("BANCO AGRARIO DE COLOMBIA S.A."));
		pasarelas.add(new Pasarela("BANCO DAVIVIENDA S.A."));
		pasarelas.add(new Pasarela("BANCO AV VILLAS"));
		pasarelas.add(new Pasarela("BANCOOMEVA"));
		pasarelas.add(new Pasarela("BANCO FALABELLA S.A."));
		pasarelas.add(new Pasarela("BANCO FINANDINA S.A."));
		pasarelas.add(new Pasarela("BANCO SANTANDER DE NEGOCIOS COLOMBIA S.A. - BANCO SANTANDER"));
		pasarelas.add(new Pasarela("BANCO COOPERATIVO COOPCENTRAL"));
		pasarelas.add(new Pasarela("BANCAMIA"));	
	}



	public String llamarPasarelas(ArrayList<Demandado> demandados) {
		String mensaje="Se obtuvieron informacion de los siguientes bancos:";
	    ArrayList<Pasarela> listaClonada = new ArrayList<Pasarela>(pasarelas);
	    int numeroPasarelas = ThreadLocalRandom.current().nextInt(0, 5 + 1);
	    for (int i = 0; i < numeroPasarelas; i++) {
	        int pasarelaRandom = ThreadLocalRandom.current().nextInt(0, listaClonada.size());
	        Pasarela pasarela = listaClonada.get(pasarelaRandom);
	        mensaje+=pasarela.getNombre();
	        for (Demandado demandado: demandados) {
	        	demandado.getCuentas().addAll(pasarela.consultaCuentas());	
			}
	        listaClonada.remove(pasarelaRandom);
	    }
	    return mensaje;
	}
}
