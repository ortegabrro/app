package util;

import java.util.Comparator;

import modelo.Cuenta;

public class CuentaComp implements Comparator<Cuenta>{	 

	@Override
	public int compare(Cuenta o1, Cuenta o2) {
		return o1.getFechaCreacion().compareTo(o2.getFechaCreacion());
	}
}