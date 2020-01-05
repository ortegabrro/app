package unicauca.front.end.service;

import java.util.HashMap;

public class Consulta {
	private HashMap<String,String> selector;

	public Consulta() {
	super();
	selector= new HashMap<String, String>();
	}

	public HashMap<String, String> getSelector() {
	return selector;
	}

	public void setSelector(HashMap<String, String> selector) {
	this.selector = selector;
	}

	}
