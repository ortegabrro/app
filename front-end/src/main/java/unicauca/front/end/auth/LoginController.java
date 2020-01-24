package unicauca.front.end.auth;

import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import controladores.EmbargosController;
import modelo.Persona;
import modelo.Usuario;
import unicauca.front.end.controllers.BackEndController;

@Controller
public class LoginController {


	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, Model model, Principal principal,
			RedirectAttributes flash) {

		if (principal != null) {
			flash.addFlashAttribute("info", "Ya iniciado sesión anteriormente");
		}

		if (error != null) {
			model.addAttribute("error",
					"Error en el login: Nombre de usuario o contraseña incorrecta, por favor vuelva a intentarlo!");
		}
		model.addAttribute("titulo", "Login");
		
		return "login";
	}
	
	@GetMapping("/crear")
	public String usuario(Model model) {

		Usuario usuario = new Usuario();
		model.addAttribute("titulo", "Login");
		model.addAttribute("form", "Formulario");
		model.addAttribute("usuario", usuario);
		return "crear";
	}
	
	@GetMapping("/crear/usuario")
	public String again(Model model) {
	
		model.addAttribute("titulo", "Login");
		model.addAttribute("form", "Formulario");
		return "crear";
	}

	@PostMapping("/crear")
	public String crear(@ModelAttribute(name = "usuario") Usuario usuario, Model model, RedirectAttributes flash) {
		
		boolean band=false;
		if (checkUsuario(usuario)) {
			if (BackEndController.obtenerUsuario(usuario.getUsername()) == null) {
				if (usuario.getPassword().equals(usuario.getConfirmPassword())) {
					usuario.getRoles().add("PERSONA");
					EmbargosController.guardarUsuario(usuario);
					flash.addFlashAttribute("success", "Usuario creado con exito, por favor iniciar sesion");
					band=true;
				} else {
					flash.addFlashAttribute("error", "No se puede Crear,Contraseñas no coinciden");
					band=false;
				}
			}else {
				flash.addFlashAttribute("error", "No se puede Crear,Nombre de Usuario ya existe");
				usuario.setUsername(null);
				band=false;
			}
		}else {
			flash.addFlashAttribute("error", "No se puede Crear ,Por favor llenar el formulario");
			band=false;
		}
		if(band==true) {
			return "redirect:login";
		}else {
			System.out.println("Hola Mundo");
			flash.addFlashAttribute("usuario", usuario);
			return "redirect:crear/usuario";
		}
		
	}
	
	private boolean checkUsuario(Usuario usuario) {
		return !usuario.getIdentificacion().isEmpty() && usuario.getTipoIdentificacion() != null
				&& !usuario.getNombres().isEmpty() && !usuario.getApellidos().isEmpty()
				&& !usuario.getUsername().isEmpty() && !usuario.getPassword().isEmpty()
				&& !usuario.getConfirmPassword().isEmpty();
	}

}
