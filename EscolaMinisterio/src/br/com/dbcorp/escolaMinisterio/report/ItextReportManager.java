package br.com.dbcorp.escolaMinisterio.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import br.com.dbcorp.escolaMinisterio.Log;
import br.com.dbcorp.escolaMinisterio.entidades.Designacao;
import br.com.dbcorp.escolaMinisterio.ui.Params;

public class ItextReportManager {

	private Log log = Log.getInstance();
	private Set<File> files;
	
	public Set<File> createReport(String fileName, List<Designacao> designacoes) {
		try {
			this.files = new TreeSet<File>();
			
			List<Designacao> selecionadas = new ArrayList<Designacao>();
			
			for (int i = 0; i < designacoes.size(); i++) {
				selecionadas.add(designacoes.get(i));
				
				if (selecionadas.size() == 4 || i == designacoes.size() - 1) {
					InputStream input = ReportManager.class.getResourceAsStream(File.separator + fileName + ".pdf");
					
					if (input == null) {
						input = ReportManager.class.getResourceAsStream("/report/" + fileName + ".pdf");
					}
					
					if (input == null) {
						input = ReportManager.class.getResourceAsStream("resources/report/" + fileName + ".pdf");
					}
					
					if (input == null) {
						input = ReportManager.class.getResourceAsStream("/resources/report/" + fileName + ".pdf");
					}
					
					this.creatFileOut(new PdfReader(input), selecionadas);
					
					selecionadas.clear();
					input.close();
				}
			}
			
			return this.files;
			
		} catch (Exception e) {
			String erro = "Não pode compilar arquivo de relatorio!";
			
			this.log.error(erro, e);
			throw new RuntimeException(erro);
		}
	}
	
	private void creatFileOut(PdfReader reader, List<Designacao> designacoes) throws IOException, DocumentException {
		File tempFile = File.createTempFile("folhas" + this.files.size(), ".pdf");
	    tempFile.deleteOnExit();
	    
	    OutputStream out = new FileOutputStream(tempFile);
		
		PdfStamper stamper =  new PdfStamper(reader, out);
		AcroFields form = stamper.getAcroFields();
		
		for (int i = 0; i < designacoes.size(); i++) {
			Designacao designacao = designacoes.get(i);

			form.setField("nome" + i, this.capitalize(designacao.getEstudante().getNome()));
			form.setField("numero" + i, designacao.getNumero() + "");
			form.setField("data" + i, designacao.getData().format(Params.dateFormate()));
			form.setField("fonte" + i, designacao.getFonte());
			form.setField("tema" + i, designacao.getTema());
			form.setField("estudo" + i, designacao.getEstudo().getNrEstudo() + "");
			form.setField("sala_" + designacao.getSala()  + "_" + i, "Yes");
			form.setField("obs" + i, designacao.getObsFolha());
			
			//form.setFieldProperty("nome" + i, "textsize ", 2.0, null);
			
			if (designacao.getAjudante() != null) {
				form.setField("ajudante"  + i, this.capitalize(designacao.getAjudante().getNome()));
				//form.setFieldProperty("ajudante" + i, "textsize ", 2.0, null);
			}
		}
		
		
		
		
		
		stamper.close();
		reader.close();
		
		this.files.add(tempFile);
	}
	
	private String capitalize(String original) {
		StringBuffer novo = new StringBuffer();
		
		if (original != null) {
			String[] palavras = original.toLowerCase().split(" ");
			
			for (String palavra : palavras) {
				if (palavra.length() > 3) {
					novo.append(palavra.substring(0, 1).toUpperCase())
						.append(palavra.substring(1));
					
				} else {
					novo.append(palavra);
				}
				
				novo.append(" ");
			}
		}
		
		return novo.toString().trim();
	}
}
