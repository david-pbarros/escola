package br.com.dbcorp.escolaMinisterio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JOptionPane;

import br.com.dbcorp.escolaMinisterio.dataBase.DataBaseHelper;

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
				Log.getInstance().error("Erro Lendo parametros de inicializa��o", e);
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
        
        // se est� executando do jar
        if (temp.exists()) {
    		return dir.getParentFile().getPath().replaceAll("%20", " ").replaceAll("%23", "#").replaceAll("%c3%a3", "�").replaceAll("%c3%b3", "�");
    	}
    	
    	return dir.getParentFile().getParentFile().getPath().replaceAll("%20", " ").replaceAll("%23", "#").replaceAll("%c3%a3", "�").replaceAll("%c3%b3", "�");
	}
	
	private static void resetDB() {
		try {
			if (IniTools.hasLine("resetBase=true")) {
				DataBaseHelper.resetDB(IniTools.obterValor("javax.persistence.jdbc.url"));
				IniTools.apagarLinha("resetBase=true");
			}
		} catch (IOException ioe) {
			String msg = "Erro preparando a dele��o da base local";
			
			Log.getInstance().error(msg, ioe);
			JOptionPane.showMessageDialog(null, "Erro preparando a dele��o da base local", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}
}
