package util;

import java.io.File;
import java.net.URISyntaxException;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import datos.Data;

public class SessionHelper {
	

	public SessionHelper() {

	}

	public  KieSession obtenerSesion(){
		KieServices kServices = KieServices.Factory.get();
		KieFileSystem kfs = kServices.newKieFileSystem();
		KieRepository kr = kServices.getRepository();
		File file;
		try {
			file = new File(getClass().getResource("/com/rule/Rules.drl").toURI());
			Resource resource = kServices.getResources().newFileSystemResource(file).setResourceType(ResourceType.DRL);
			kfs.write(resource);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		KieBuilder kb = kServices.newKieBuilder(kfs);
		kb.buildAll();
		KieContainer kContainer = kServices.newKieContainer(kr.getDefaultReleaseId());
		KieSession sessionStatefull = kContainer.newKieSession();
		Data datos = new Data();
		sessionStatefull.setGlobal("datos", datos);
		return sessionStatefull;
	}
	
	public void cerrarSesion(KieSession sesion) {
		sesion.dispose();
	}
}
