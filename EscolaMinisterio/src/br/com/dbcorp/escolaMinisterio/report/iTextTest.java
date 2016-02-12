package br.com.dbcorp.escolaMinisterio.report;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import br.com.dbcorp.escolaMinisterio.Log;

public class iTextTest {
	
	private Log log = Log.getInstance();
	
	public static final String RESULT1 = "report/designacoes.pdf";
	
	private static final Map<String, Integer> NUMEROS_CAMPOS = new HashMap<String, Integer>();
	private static final Map<Integer, String> SUFIX = new HashMap<Integer, String>();
	
	static {
		NUMEROS_CAMPOS.put("nome", 1);
		NUMEROS_CAMPOS.put("numero", 2);
		NUMEROS_CAMPOS.put("data", 3);
		NUMEROS_CAMPOS.put("ajudante", 4);
		NUMEROS_CAMPOS.put("fonte", 5);
		NUMEROS_CAMPOS.put("tema", 6);
		NUMEROS_CAMPOS.put("cena", 7);
		NUMEROS_CAMPOS.put("estudo", 8);
		
		SUFIX.put(1, "");
		SUFIX.put(2, ".");
		SUFIX.put(3, "-");
		SUFIX.put(4, "_");
	}
	
	
	public void manipulate() {
		try {
			File tempFile = File.createTempFile("folhas", ".pdf");
		    tempFile.deleteOnExit();
		    
		    OutputStream out = new FileOutputStream(tempFile);
			
			PdfReader reader = new PdfReader(RESULT1);
			PdfStamper stamper =  new PdfStamper(reader, out);
			AcroFields form = stamper.getAcroFields();
			
			for (Object key : form.getFields().keySet()) {
				form.setField(key.toString(), key.toString());
				
				System.out.println(key);
			}
			
			stamper.close();
			reader.close();
			
			this.print(tempFile);
		} catch (Exception e) {
			log.debug("", e);
		}
	}
	
	
	public void print(File tempFile) {
		try {
			Desktop.getDesktop().open(tempFile);
		} catch (IOException e) {
			log.debug("", e);
		}
	}
	
	public static void main(String[] args) {
		iTextTest itext = new iTextTest();
		
		itext.manipulate();
	}
}
