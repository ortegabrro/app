package unicauca.front.end.controllers;

import com.google.gson.Gson;

import controladores.EmbargosController;
import modelo.Autoridad;
import modelo.Persona;
import modelo.Usuario;


public class BackEndController {

	public static Usuario obtenerUsuario(String username) {
		Usuario usuario=null;
		
		String json= EmbargosController.obtenerUsuario(username);
		if(!json.isEmpty()) {
			System.out.println("Json: " + json);
			usuario = new Gson().fromJson(json, Usuario.class);
		}
		return usuario;
	}
	
	public static Persona obtenerPersona(String idPersona, String tipoIdentificacion) {
		Persona persona=null;
		
		String json= EmbargosController.obtenerPersona(idPersona, tipoIdentificacion);
		if(!json.isEmpty()) {
			System.out.println("Json: " + json);
			persona = new Gson().fromJson(json, Persona.class);
		}
		return persona;
	}
	
	public static Autoridad obtenerAutoridad(String idAutoridad) {
		String json= EmbargosController.obtenerAutoridad(idAutoridad);
		Autoridad autoridad=null;
		if(!json.isEmpty()) {
			System.out.println("Json: " + json);
			autoridad = new Gson().fromJson(json, Autoridad.class);
		}
		return autoridad;
	}
	
}
