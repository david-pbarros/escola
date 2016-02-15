package br.com.dbcorp.escolaMinisterio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Params {
	
	private static Properties props;
	
	private static boolean offLine = false;

	public synchronized static Properties propriedades() {
		if (props == null) {
			props = new Properties();
			
			try {
				props.load(iniLoad());
				
				for(Object key : props.keySet()) {
					System.out.println("chave: " + key + " valor: " + props.get(key));
				}
				
				if ( !props.getProperty("logPath").endsWith("/") ) {
					props.put("logPath", props.getProperty("logPath") + "/");
				}
	
				props.put("verionNumber", 403);
				props.put("versionName", "4.0.3");
				
			} catch (Exception e) {
				Log.getInstance().error("Erro Lendo parametros de inicialização", e);
			}
		}
		
		return props;
	}
	
	public static InputStream iniLoad() throws FileNotFoundException {
		File f = getAppPath().resolve("escola.ini").toFile();
		return new FileInputStream(f);
	}
	
	public static Path getAppPath() {
		return Paths.get(".").toAbsolutePath().normalize();
	}
	
	public static void setOffLineMode() {
		offLine = true;
	}
	
	public static boolean isOffLine() {
		return offLine;
	}
}
