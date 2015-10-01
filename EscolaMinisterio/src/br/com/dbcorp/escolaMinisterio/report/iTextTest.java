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

public class iTextTest {
	
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
			
			/*form.setField("Texto2_12", "teste");
			
			
			form.setField("Text" + SUFIX.get(2) + NUMEROS_CAMPOS.get("tema"), "kleber");*/

			/*des1
			form.setField("Check Box1", "Yes");
			form.setField("Check Box2", "Yes");
			form.setField("Check Box3", "Yes");
			*/
			
			/*des2
			form.setField("Check Box.1", "Yes");
			form.setField("Check Box.2", "Yes");
			form.setField("Check Box.3", "Yes");
			*/
			
			/*des3
			form.setField("Check Box-1", "Yes");
			form.setField("Check Box-2", "Yes");
			form.setField("Check Box-3", "Yes");
			*/
			
			/*des4
			form.setField("Check Box_1", "Yes");
			form.setField("Check Box_2", "Yes");
			form.setField("Check Box_3", "Yes");
			*/
			
			stamper.close();
			reader.close();
			
			this.print(tempFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void print(File tempFile) {
		try {
			Desktop.getDesktop().open(tempFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		iTextTest itext = new iTextTest();
		
		itext.manipulate();
		
		
		
		
		
	}
}
