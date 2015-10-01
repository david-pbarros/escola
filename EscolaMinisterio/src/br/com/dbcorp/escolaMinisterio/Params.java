package br.com.dbcorp.escolaMinisterio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class Params {
	
	private static Properties props;

	public static Properties propriedades() {
		if (props == null) {
			props = new Properties();
			
			try {
				props.load(iniLoad());
				
				for(Object key : props.keySet()) {
					System.out.println("chave: " + key + " valor: " + props.get(key));
				}
	
				props.put("verionNumber", 302);
				props.put("versionName", "3.0.2");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return props;
	}
	
	public static InputStream iniLoad() throws FileNotFoundException {
		File f = new File(getAppPath() + File.separator + "escola.ini");
		return new FileInputStream(f);
	}
	
	private static String getAppPath() {
		String url = Params.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        File dir = new File(url);
        
       System.out.println(dir.getParentFile().getPath() + " parent");
        
        File temp = new File(dir.getParentFile().getPath() + File.separator + "EscolaMinisterio.jar");
        
        // se está executando do jar
        if (temp.exists()) {
    		return dir.getParentFile().getPath().replaceAll("%20", " ").replaceAll("%23", "#").replaceAll("%c3%a3", "ã").replaceAll("%c3%b3", "ó");
    	}
    	
    	return dir.getParentFile().getParentFile().getPath().replaceAll("%20", " ").replaceAll("%23", "#").replaceAll("%c3%a3", "ã").replaceAll("%c3%b3", "ó");
		
		
	}
}
