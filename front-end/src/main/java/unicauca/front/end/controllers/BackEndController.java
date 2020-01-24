package unicauca.front.end.controllers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import controladores.EmbargosController;
import enumeraciones.TipoIdentificacion;
import modelo.Autoridad;
import modelo.Embargo;
import modelo.EmbargoCoactivo;
import modelo.EmbargoJudicial;
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
	
	public static Autoridad obtenerAutoridad(String idAutoridad) {
		String json= EmbargosController.obtenerAutoridad(idAutoridad);
		Autoridad autoridad=null;
		if(!json.isEmpty()) {
			System.out.println("Json: " + json);
			autoridad = new Gson().fromJson(json, Autoridad.class);
		}
		return autoridad;
	}
	
	public static EmbargoJudicial obtenerEmbargoJudicial(String idEmbargo) {
		String json= EmbargosController.obtenerEmbargo(idEmbargo);
		EmbargoJudicial embargo=null;
		if(!json.isEmpty()) {
			System.out.println("Json: " + json);
			embargo = new Gson().fromJson(json, EmbargoJudicial.class);
		}
		return embargo;
	}
	
	public static EmbargoCoactivo obtenerEmbargoCoactivo(String idEmbargo) {
		String json= EmbargosController.obtenerEmbargo(idEmbargo);
		EmbargoCoactivo embargo=null;
		if(!json.isEmpty()) {
			System.out.println("Json: " + json);
			embargo = new Gson().fromJson(json, EmbargoCoactivo.class);
		}
		return embargo;
	}
	
	public static Usuario obtenerUsuarioByid(String identificacion) throws JSONException {

		Usuario usuariof = null;
		String consulta = "{\"selector\": {\"username\": {\"$regex\": \".*\"},\"identificacion\": \"" + identificacion
				+ "\"}}";
		String mensaje = EmbargosController.consultaGeneral(consulta);
		
		if (!mensaje.isEmpty()) {
			JSONObject myjson = new JSONObject(mensaje);
			JSONObject jsonRecord = myjson.getJSONObject("Record");
			usuariof = jsontoUser(jsonRecord);
		}

		return usuariof;
	}
	
	
	
	public static Usuario jsontoUser(JSONObject jsonRecord) throws JSONException {
		Usuario usuario = new Usuario();

		if (jsonRecord.has("identificacion")) {
			usuario.setIdentificacion(jsonRecord.getString("identificacion"));
		}
		if (jsonRecord.has("tipoIdentificacion")) {
			usuario.setTipoIdentificacion(TipoIdentificacion.valueOf(jsonRecord.getString("tipoIdentificacion")));
		}
		if (jsonRecord.has("nombres")) {
			usuario.setNombres(jsonRecord.getString("nombres"));
		}
		if (jsonRecord.has("apellidos")) {
			usuario.setApellidos(jsonRecord.getString("apellidos"));
		}
		if (jsonRecord.has("ownedBy")) {
			usuario.setOwnedBy(jsonRecord.getString("ownedBy"));
		}
		if (jsonRecord.has("username")) {
			usuario.setUsername(jsonRecord.getString("username"));
		}
		ArrayList<String> roles = new ArrayList<String>();
		if (jsonRecord.has("roles")) {
			JSONArray jsonRoles = jsonRecord.getJSONArray("roles");

			for (int k = 0; k < jsonRoles.length(); k++) {
				roles.add(jsonRoles.getString(k));
			}
			usuario.setRoles(roles);
		}
		return usuario;
	}
	
}
