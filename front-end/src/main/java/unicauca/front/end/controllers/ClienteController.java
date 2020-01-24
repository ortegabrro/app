package unicauca.front.end.controllers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import controladores.EmbargosController;
import enumeraciones.Ciudad;
import enumeraciones.Departamento;

import modelo.Autoridad;

import modelo.Embargo;

import modelo.Usuario;

@Controller
public class ClienteController {

	@GetMapping("/persona")
	public String persona(Model model, RedirectAttributes flash) throws JSONException {

		ArrayList<Embargo> embargos = getEmbargos();
		ArrayList<Autoridad> autoridades = getAutoridades();

		if (!embargos.isEmpty() && !autoridades.isEmpty()) {
			flash.addFlashAttribute("embargos", embargos);
			flash.addFlashAttribute("autoridades", autoridades);
			return "redirect:persona/embargos";

		} else {
			flash.addFlashAttribute("warning", "No hay Embargos con su identificacion");
			return "redirect:persona/embargos";
		}
	}

	@GetMapping("/persona/embargos")
	public String system(Model model) {
		model.addAttribute("titulo", "Embargos");
		model.addAttribute("form", "Embargos Aplicados");
		return "persona/main";
	}

	@GetMapping("/persona/imprimir")
	public ResponseEntity<byte[]> print() throws DocumentException, IOException, JSONException {
		
		String filepdf = "file.pdf";
		ArrayList<Embargo> embargos = getEmbargos();
		ArrayList<Autoridad> autoridades = getAutoridades();

		createPdf(filepdf, embargos, autoridades);
		HttpHeaders headers = new HttpHeaders();
		Path pdfPath = Paths.get(filepdf);
		byte[] pdf = Files.readAllBytes(pdfPath);
		headers.setContentType(MediaType.parseMediaType("application/pdf"));
		headers.add("Content-Disposition", "inline; filename=" + filepdf);
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(pdf, headers, HttpStatus.OK);

		return response;
	}

	public ArrayList<Embargo> getEmbargos() throws JSONException {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		Usuario usuario = BackEndController.obtenerUsuario(username);
		
		String consulta = "{\"selector\": {\"demandados\": {\"$elemMatch\": {\"identificacion\": {\"$eq\": \""+usuario.getIdentificacion() + "\"},\"tipoIdentificacion\": {\"$eq\": \""+ usuario.getTipoIdentificacion() + "\"}}}}}";
		System.out.println("consulta:" + consulta);
		ArrayList<Embargo> embargos = jsontoEmbargos(consulta);
		return embargos;
	}

	public ArrayList<Autoridad> getAutoridades() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Departamento departamento = Departamento.values()[ThreadLocalRandom.current().nextInt(0,
				Departamento.values().length)];
		Ciudad ciudad = Ciudad.values()[ThreadLocalRandom.current().nextInt(0, Ciudad.values().length)];
		Autoridad auto = new Autoridad("aut1", null, "JUZGADO PRIMERO", "CALLE 3 #5-18 CENTRO", ciudad, departamento,
				null);
		ArrayList<Autoridad> autoridades = new ArrayList<Autoridad>();
		autoridades.add(auto);
		return autoridades;
	}

	public void createPdf(String dest, ArrayList<Embargo> embargos, ArrayList<Autoridad> autoridades)
			throws FileNotFoundException, DocumentException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Usuario usuario = BackEndController.obtenerUsuario(username);
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(dest));
		document.open();

		for (int i = 0; i < embargos.size(); i++) {

			PdfPTable table = new PdfPTable(6);
			table.setSpacingBefore(10f);
			table.setSpacingAfter(12.5f);
			document.add(table);
			Font f = new Font(FontFamily.HELVETICA, 13, Font.NORMAL, GrayColor.GRAYWHITE);
			PdfPCell cell = new PdfPCell(new Phrase("Embargos Aplicados a la Identificacion:"+usuario.getIdentificacion(), f));
			cell.setBackgroundColor(GrayColor.GRAYBLACK);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setColspan(6);
			table.addCell(cell);
			table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));
			for (int j = 0; j < 1; j++) {
				table.addCell("Numero Proceso Embargo");
				table.addCell("Fecha Oficio Embargo");
				table.addCell("Nombre Autoridad");
				table.addCell("Direccion de la Autoridad");
				table.addCell("Departamento de la Autoridad");
				table.addCell("Ciudad de la Autoridad");
			}
			table.setHeaderRows(1);
			table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			for (Autoridad autoridad : autoridades) {
				for (Embargo embargo : embargos) {
					if (autoridad.getIdAutoridad().equals(embargo.getIdAutoridad())) {
						table.addCell(embargo.getNumProceso());
						table.addCell(embargo.getFechaOficio().toString());
						table.addCell(autoridad.getNombre());
						table.addCell(autoridad.getDireccion());
						table.addCell(autoridad.getDepartamento().toString());
						table.addCell(autoridad.getCiudad().toString());
					}
				}
			}
			document.add(table);
		}
		document.close();
	}

	public ArrayList<Embargo> jsontoEmbargos(String consulta) throws JSONException {
		String consultanew = consulta;
		ArrayList<Embargo> embargos = new ArrayList<Embargo>();
		String mensaje = EmbargosController.consultaGeneral(consultanew);
		mensaje = "[" + mensaje + "]";
		JSONArray myjson = new JSONArray(mensaje);
		for (int i = 0; i < myjson.length(); i++) {
			JSONObject jsonRecord = myjson.getJSONObject(i).getJSONObject("Record");
			embargos.add(jsontoObject(jsonRecord));
		}
		return embargos;
	}
	
	public Embargo jsontoObject(JSONObject jsonRecord) throws JSONException {
		Embargo embargo = new Embargo();

		if (jsonRecord.has("idAutoridad")) {
			embargo.setIdAutoridad(jsonRecord.getString("idAutoridad"));
		}
		if (jsonRecord.has("numProceso")) {
			embargo.setNumProceso(jsonRecord.getString("numProceso"));
		}
		if (jsonRecord.has("fechaOficio")) {
			JSONObject jsonFecha = jsonRecord.getJSONObject("fechaOficio");
			LocalDate localDate = LocalDate.of(Integer.parseInt(jsonFecha.getString("year")),
					Integer.parseInt(jsonFecha.getString("month")), Integer.parseInt(jsonFecha.getString("day")));
			embargo.setFechaOficio(localDate);
		}

		return embargo;
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

		if (jsonRecord.has("nombre")) {
			autoridad.setNombre(jsonRecord.getString("nombre"));
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
		
		return autoridad;
	}

}
