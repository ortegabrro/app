package unicauca.front.end.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.google.gson.Gson;

import controladores.EmbargosController;
import enumeraciones.Ciudad;
import enumeraciones.Departamento;
import enumeraciones.TipoAutoridad;
import enumeraciones.TipoIdentificacion;
import modelo.Autoridad;
import modelo.Usuario;
import unicauca.front.end.service.Consulta;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private String boton;

	@GetMapping("/app")
	public String main(Model model) {
		Autoridad autoridad = new Autoridad();
		autoridad.getUsuarios().add(new Usuario());
		model.addAttribute("titulo", "App");
		model.addAttribute("form", "Formulario");
		model.addAttribute("autoridad", autoridad);
		model.addAttribute("backendcontroller", new BackEndController());
		boton = "all";
		model.addAttribute("boton", boton);
		return "admin/main";
	}

	@RequestMapping(value = "/app/form/{consulta}", method = RequestMethod.POST, params = "action=crear")
	public String crearAut(@ModelAttribute(name = "autoridad") Autoridad autoridad, Model model,
			RedirectAttributes flash) throws JSONException {
		boolean band = false;
		int tam = autoridad.getUsuarios().size();
		if (checkAutoridad(autoridad)) {
			if (BackEndController.obtenerAutoridad(autoridad.getIdAutoridad()) == null) {
				if (checkUsuario(autoridad.getUsuarios().get(tam - 1))) {
					if (BackEndController
							.obtenerUsuarioByid(autoridad.getUsuarios().get(tam - 1).getIdentificacion()) == null) {
						if (BackEndController
								.obtenerUsuario(autoridad.getUsuarios().get(tam - 1).getUsername()) == null) {
							if (autoridad.getUsuarios().get(tam - 1).getConfirmPassword()
									.equals(autoridad.getUsuarios().get(tam - 1).getPassword())) {
								autoridad.getUsuarios().get(tam - 1).setOwnedBy(autoridad.getIdAutoridad());
								autoridad.getUsuarios().get(tam - 1).getRoles().add("ADMIN");
								EmbargosController.guardarUsuario(autoridad.getUsuarios().get(tam - 1));
								band = true;
							} else {
								flash.addFlashAttribute("error",
										"No se puede Crear Autoridad,Contraseñas de Usuario no coinciden");
								band = false;
							}
						} else {
							flash.addFlashAttribute("error", "No se puede Crear Autoridad,Nombre de Usuario ya existe");
							autoridad.getUsuarios().get(tam - 1).setUsername(null);
							band = false;
						}
					} else {
						flash.addFlashAttribute("error",
								"No se puede Crear Autoridad,Identificacion de Usuario ya existe");
						autoridad.getUsuarios().get(tam - 1).setIdentificacion(null);
						band = false;
					}
				} else {
					flash.addFlashAttribute("error",
							"No se puede Crear Autoridad,Por favor llenar el formulario Usuario");
					band = false;
				}
			} else {
				flash.addFlashAttribute("error", "No se puede Crear Autoridad,Identificacion de Autoridad ya existe");
				band = false;
			}
		} else {
			flash.addFlashAttribute("error", "No se puede Crear Autoridad,Por favor llenar el formulario");
			band = false;
		}
		if (band == true) {
			flash.addFlashAttribute("success", "Autoridad creada con exito");
			EmbargosController.guardarAutoridad(autoridad);
		}
		flash.addFlashAttribute("autoridad", autoridad);
		boton = "all";
		flash.addFlashAttribute("boton", boton);
		return "redirect:/admin/app/crear";
	}

	@GetMapping("/app/crear")
	public String system(Model model) {
		model.addAttribute("titulo", "App");
		model.addAttribute("form", "Formulario");

		return "admin/main";
	}

	@RequestMapping(value = "/app/form/{consulta}", method = RequestMethod.POST, params = "action=agregar")
	public String agregar(Model model, @ModelAttribute(name = "autoridad") Autoridad autoridad,
			RedirectAttributes flash) {
		boton = "all";
		flash.addFlashAttribute("autoridad", autoridad);
		flash.addFlashAttribute("boton", boton);
		return "redirect:/admin/app/agregar";
	}

	@RequestMapping(value = "/app/form/{consulta}", method = RequestMethod.POST, params = "action=inactualizar")
	public String upagregar(@ModelAttribute(name = "autoridad") Autoridad autoridad, RedirectAttributes flash,
			@PathVariable(value = "consulta") String consulta) {
		boton = "onactualizar";
		autoridad.getUsuarios().add(new Usuario());
		flash.addFlashAttribute("autoridad", autoridad);
		flash.addFlashAttribute("boton", boton);
		flash.addFlashAttribute("consulta", consulta);
		return "redirect:/admin/app/crear";
	}

	@RequestMapping(value = "/app/form/{consulta}", method = RequestMethod.POST, params = "action=onagregar")
	public String onagregar(Model model, @ModelAttribute(name = "autoridad") Autoridad autoridad,
			RedirectAttributes flash) {
		boton = "onactualizar";
		flash.addFlashAttribute("autoridad", autoridad);
		flash.addFlashAttribute("boton", boton);
		return "redirect:/admin/app/agregar";
	}

	@GetMapping("/app/agregar")
	public String add(Model model, @ModelAttribute(name = "autoridad") Autoridad autoridad,
			@ModelAttribute(name = "boton") String boton, RedirectAttributes flash) throws JSONException {

		int tam = autoridad.getUsuarios().size();
		if (checkUsuario(autoridad.getUsuarios().get(tam - 1))) {
			if (BackEndController
					.obtenerUsuarioByid(autoridad.getUsuarios().get(tam - 1).getIdentificacion()) == null) {
				if (BackEndController.obtenerUsuario(autoridad.getUsuarios().get(tam - 1).getUsername()) == null) {
					if (autoridad.getUsuarios().get(tam - 1).getConfirmPassword()
							.equals(autoridad.getUsuarios().get(tam - 1).getPassword())) {
						EmbargosController.guardarUsuario(autoridad.getUsuarios().get(tam - 1));
						autoridad.getUsuarios().add(new Usuario());
					} else {
						flash.addFlashAttribute("error", "No se puede Agregar Usuario,Contraseñas no coinciden");
						autoridad.getUsuarios().get(tam - 1).setPassword(null);
						autoridad.getUsuarios().get(tam - 1).setConfirmPassword(null);
					}
				} else {
					flash.addFlashAttribute("error", "No se puede Agregar Usuario,Nombre de Usuario ya existe");
					autoridad.getUsuarios().get(tam - 1).setUsername(null);
				}
			} else {
				flash.addFlashAttribute("error", "No se puede Agregar Usuario,Identificacion ya existe");
				autoridad.getUsuarios().get(tam - 1).setIdentificacion(null);
			}
		} else {
			flash.addFlashAttribute("error", "No se puede Agregar Usuario,Por favor llenar el formulario");
			autoridad.setIdAutoridad(null);
		}
		flash.addFlashAttribute("autoridad", autoridad);
		flash.addFlashAttribute("boton", boton);
		return "redirect:/admin/app/crear";
	}

	@RequestMapping(value = "/app/form/{consulta}", method = RequestMethod.POST, params = "action=consultar")
	public String consultaAut(@ModelAttribute(name = "autoridad") Autoridad autoridad, Model model,
			RedirectAttributes flash) throws JSONException {

		Consulta selector = new Consulta();
		if (!consulta(autoridad).isEmpty()) {
			selector.setSelector(consulta(autoridad));
			Gson gson = new Gson();
			String consulta = gson.toJson(selector);
			ArrayList<Autoridad> autoridades = jsontoAutoridades(consulta);
			if (!autoridades.isEmpty()) {
				model.addAttribute("titulo", "Consulta");
				model.addAttribute("form", "Consultas");
				model.addAttribute("autoridades", autoridades);
				model.addAttribute("consulta", consulta);
				boton = "actualizar";
				model.addAttribute("boton", boton);
				return "admin/consulta";
			} else {
				flash.addFlashAttribute("warning", "No se encontraron resultados");
				return "redirect:/admin/app";
			}
		} else {
			flash.addFlashAttribute("warning", "No se puede Consultar, Por favor ingresar el campo a consultar");
			return "redirect:/admin/app";
		}

	}

	@RequestMapping(value = "/app/consulta/{consulta}", method = RequestMethod.POST, params = "action=actualizar")
	public String actualizar(@ModelAttribute(name = "autoridad") Autoridad autoridad, Model model,
			RedirectAttributes flash, @PathVariable(value = "consulta") String consulta) {

		Autoridad autoridadnew = BackEndController.obtenerAutoridad(autoridad.getIdAutoridad());
		autoridad.setHabilitado(true);
		model.addAttribute("titulo", "Actualizar");
		model.addAttribute("form", "Formulario");
		model.addAttribute("autoridad", autoridadnew);
		boton = "inactualizar";
		model.addAttribute("boton", boton);
		model.addAttribute("consulta", consulta);
		return "admin/main";
	}

	@RequestMapping(value = "/app/form/{consulta}", method = RequestMethod.POST, params = "action=onactualizar")
	public String actualizarOn(Model model, @ModelAttribute(name = "autoridad") Autoridad autoridad,
			RedirectAttributes flash, @PathVariable(value = "consulta") String consulta) throws JSONException {
		boolean band = false;
		if (checkAutoridad(autoridad)) {
			for (int i = 0; i < autoridad.getUsuarios().size(); i++) {
				if (checkUsuario(autoridad.getUsuarios().get(i))) {
					if (autoridad.getUsuarios().get(i).getPassword()
							.equals(autoridad.getUsuarios().get(i).getConfirmPassword())) {
						if (BackEndController
								.obtenerUsuarioByid(autoridad.getUsuarios().get(i).getIdentificacion()) == null) {
							if (BackEndController
									.obtenerUsuario(autoridad.getUsuarios().get(i).getUsername()) == null) {
								EmbargosController.guardarUsuario(autoridad.getUsuarios().get(i));
								band = true;
							} else {
								Usuario userfind = BackEndController
										.obtenerUsuario(autoridad.getUsuarios().get(i).getUsername());
								if (userfind == null) {
									flash.addFlashAttribute("warning",
											"No se puede Actualizar,Nombre de Usuario ya existe");
									autoridad.getUsuarios().get(i).setUsername(null);
									band = false;
								} else {
									updateUser(userfind, autoridad.getUsuarios().get(i));
									band = true;
								}
							}
						} else {
							Usuario userfind = BackEndController
									.obtenerUsuario(autoridad.getUsuarios().get(i).getUsername());
							if (userfind == null) {
								flash.addFlashAttribute("warning",
										"No se puede Actualizar,Identificacion de Usuario ya existe");
								autoridad.getUsuarios().get(i).setIdentificacion(null);
								band = false;
							} else {
								updateUser(userfind, autoridad.getUsuarios().get(i));
								band = true;
							}
						}
					} else {
						flash.addFlashAttribute("error",
								"No se puede Actualizar Autoridad,Contraseñas de Usuario no coinciden");
						band = false;
					}
				} else {
					flash.addFlashAttribute("warning",
							"No se puede Actualizar Autoridad,Por favor llenar el formulario Usuario");
					band = false;
				}
			}
		} else {
			flash.addFlashAttribute("error", "No se puede Crear Autoridad,Por favor llenar el formulario");
			band = false;
		}
		if (band == true) {
			Autoridad autoridadfind = BackEndController.obtenerAutoridad(autoridad.getIdAutoridad());
			updateAutoridad(autoridadfind, autoridad);
			flash.addFlashAttribute("success", "Autoridad actualizada con exito");
			boton = "actualizar";
			flash.addFlashAttribute("boton", boton);
			flash.addFlashAttribute("consulta", consulta);
			return "redirect:/admin/app/consulta";
		} else {
			flash.addFlashAttribute("autoridad", autoridad);
			return "redirect:/admin/app/actualizar";
		}
	}

	@GetMapping("/app/consulta")
	public String loadConsulta(Model model, @ModelAttribute(name = "consulta") String consulta) throws JSONException {

		ArrayList<Autoridad> autoridades = jsontoAutoridades(consulta);
		model.addAttribute("titulo", "Consulta");
		model.addAttribute("form", "Consultas");
		model.addAttribute("autoridades", autoridades);
		return "admin/consulta";

	}

	@GetMapping("/app/actualizar")
	public String loadActualizar(Model model) {
		model.addAttribute("titulo", "Actualizar");
		model.addAttribute("form", "Formulario");
		boton = "inactualizar";
		model.addAttribute("boton", boton);
		return "admin/main";
	}

	public void updateUser(Usuario userfind, Usuario usuario) {
		userfind.setTipoIdentificacion(usuario.getTipoIdentificacion());
		userfind.setNombres(usuario.getNombres());
		userfind.setApellidos(usuario.getApellidos());
		userfind.setPassword(usuario.getPassword());
		userfind.setConfirmPassword(usuario.getPassword());
		EmbargosController.editarUsuario(userfind);
	}

	public void updateAutoridad(Autoridad autoridadfind, Autoridad autoridad) {
		autoridadfind.setNombre(autoridad.getNombre());
		autoridadfind.setTipoAutoridad(autoridad.getTipoAutoridad());
		autoridadfind.setDireccion(autoridad.getDireccion());
		autoridadfind.setCiudad(autoridad.getCiudad());
		autoridadfind.setDepartamento(autoridad.getDepartamento());
		autoridadfind.setUsuarios(autoridad.getUsuarios());
		EmbargosController.editarAutoridad(autoridadfind);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public HashMap<String, String> consulta(Autoridad autoridad) {
		HashMap<String, String> campos = new HashMap<String, String>();

		if (!autoridad.getIdAutoridad().isEmpty()) {
			campos.put("idAutoridad", autoridad.getIdAutoridad());
		}
		if (!autoridad.getNombre().isEmpty()) {
			campos.put("nombre", autoridad.getNombre());
		}
		if (autoridad.getTipoAutoridad() != null) {
			campos.put("tipoAutoridad", autoridad.getTipoAutoridad().toString());
		}
		if (!autoridad.getDireccion().isEmpty()) {
			campos.put("direccion", autoridad.getDireccion());
		}
		if (autoridad.getDepartamento() != null) {
			campos.put("departamento", autoridad.getDepartamento().toString());
		}
		if (autoridad.getCiudad() != null) {
			campos.put("ciudad", autoridad.getCiudad().toString());
		}
		if (!autoridad.getUsuarios().get(0).getIdentificacion().isEmpty()) {
			campos.put("identificacion", autoridad.getUsuarios().get(0).getIdentificacion());
		}
		if (autoridad.getUsuarios().get(0).getTipoIdentificacion() != null) {
			campos.put("tipoIdentificacion", autoridad.getUsuarios().get(0).getTipoIdentificacion().toString());
		}
		if (!autoridad.getUsuarios().get(0).getNombres().isEmpty()) {
			campos.put("nombres", autoridad.getUsuarios().get(0).getNombres());
		}
		if (!autoridad.getUsuarios().get(0).getApellidos().isEmpty()) {
			campos.put("apellidos", autoridad.getUsuarios().get(0).getApellidos());
		}
		if (!autoridad.getUsuarios().get(0).getUsername().isEmpty()) {
			campos.put("username", autoridad.getUsuarios().get(0).getUsername());
		}
		return campos;
	}

	public ArrayList<Autoridad> jsontoAutoridades(String consulta) throws JSONException {
		String consultanew = consulta;
		ArrayList<Autoridad> autoridades = new ArrayList<Autoridad>();
		String mensaje = EmbargosController.consultaGeneral(consultanew);
		mensaje = "[" + mensaje + "]";
		JSONArray myjson = new JSONArray(mensaje);
		for (int i = 0; i < myjson.length(); i++) {
			JSONObject jsonRecord = myjson.getJSONObject(i).getJSONObject("Record");
			autoridades.add(jsontoAutoridad(jsonRecord));
		}
		return autoridades;
	}

	public Autoridad jsontoAutoridad(JSONObject jsonRecord) throws JSONException {
		Autoridad autoridad = new Autoridad();

		if (jsonRecord.has("idAutoridad")) {
			autoridad.setIdAutoridad(jsonRecord.getString("idAutoridad"));
		}
		if (jsonRecord.has("nombre")) {
			autoridad.setNombre(jsonRecord.getString("nombre"));
		}
		if (jsonRecord.has("tipoAutoridad")) {
			autoridad.setTipoAutoridad(TipoAutoridad.valueOf(jsonRecord.getString("tipoAutoridad")));
		}
		if (jsonRecord.has("direccion")) {
			autoridad.setDireccion(jsonRecord.getString("direccion"));
		}
		if (jsonRecord.has("departamento")) {
			autoridad.setDepartamento(Departamento.valueOf(jsonRecord.getString("departamento")));
		}
		if (jsonRecord.has("ciudad")) {
			autoridad.setCiudad(Ciudad.valueOf(jsonRecord.getString("ciudad")));
		}
		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
		if (jsonRecord.has("usuarios")) {
			JSONArray jsonUsuarios = jsonRecord.getJSONArray("usuarios");

			for (int k = 0; k < jsonUsuarios.length(); k++) {
				Usuario usuario = new Usuario();
				usuario.setIdentificacion(jsonUsuarios.getJSONObject(k).getString("identificacion"));
				usuario.setTipoIdentificacion(
						TipoIdentificacion.valueOf(jsonUsuarios.getJSONObject(k).getString("tipoIdentificacion")));
				usuario.setNombres(jsonUsuarios.getJSONObject(k).getString("nombres"));
				usuario.setApellidos(jsonUsuarios.getJSONObject(k).getString("apellidos"));
				usuario.setUsername(jsonUsuarios.getJSONObject(k).getString("username"));
				usuarios.add(usuario);
			}
			autoridad.setUsuarios(usuarios);
		}

		return autoridad;
	}

	private boolean checkAutoridad(Autoridad autoridad) {
		return !autoridad.getIdAutoridad().isEmpty() && !autoridad.getNombre().isEmpty()
				&& autoridad.getTipoAutoridad() != null && !autoridad.getDireccion().isEmpty()
				&& autoridad.getDepartamento() != null && autoridad.getCiudad() != null
				&& !autoridad.getUsuarios().isEmpty();
	}

	private boolean checkUsuario(Usuario usuario) {
		return !usuario.getIdentificacion().isEmpty() && usuario.getTipoIdentificacion() != null
				&& !usuario.getNombres().isEmpty() && !usuario.getApellidos().isEmpty()
				&& !usuario.getUsername().isEmpty() && !usuario.getPassword().isEmpty()
				&& !usuario.getConfirmPassword().isEmpty();
	}
}