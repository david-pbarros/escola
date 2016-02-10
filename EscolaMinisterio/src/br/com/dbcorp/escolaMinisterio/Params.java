package br.com.dbcorp.escolaMinisterio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Params {
	
	private static Properties props;

	public synchronized static Properties propriedades() {
		if (props == null) {
			resetDB();
			
			props = new Properties();
			
			try {
				props.load(iniLoad());
				
				for(Object key : props.keySet()) {
					System.out.println("chave: " + key + " valor: " + props.get(key));
				}
	
				props.put("verionNumber", 403);
				props.put("versionName", "4.0.3");
				
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
	
	public static String getAppPath() {
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
	
	private static void resetDB() {
		try {
			Path iniPath = Paths.get(getAppPath() + File.separator + "escola.ini");
			Path oldPath = Paths.get(getAppPath() + File.separator + "escola.old");
		
			if (Files.lines(iniPath).anyMatch(l->"resetBase=true".equalsIgnoreCase(l))) {
				List<String> linhas = new ArrayList<>();
				
				Files.lines(iniPath).filter(l->!"resetBase=true".equalsIgnoreCase(l)).forEach(l->linhas.add(l));
				
				File file = iniPath.toFile();
				file.renameTo(oldPath.toFile());
				
				
				//Files.delete(iniPath);
				
				Files.createFile(iniPath);
				
				BufferedWriter bw = Files.newBufferedWriter(iniPath, StandardOpenOption.APPEND);
				
				for (String linha : linhas) {
					bw.write(linha);
					bw.newLine();
				}
				
				bw.flush();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		/*if (firstIni && "true".equalsIgnoreCase(Params.propriedades().getProperty("resetBase", "false"))) {
		Path basePath = Paths.get(Params.propriedades().getProperty("javax.persistence.jdbc.url").replace("jdbc:sqlite:", ""));
		
		try {
			Files.delete(basePath);
			
			rewriterINI();
			
			firstIni = false;

		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro apagando a base local.", "", JOptionPane.ERROR_MESSAGE);
		}*/
	}
}
