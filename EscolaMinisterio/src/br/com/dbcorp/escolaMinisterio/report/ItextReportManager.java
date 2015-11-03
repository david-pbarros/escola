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
	
	public Set<File> createReport(String fileName, List<Designacao> designacoes, boolean novoModelo) {
		try {
			this.files = new TreeSet<File>();
			
			fileName += (novoModelo ? "" : "Pre2016");
			
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
					
					this.creatFileOut(new PdfReader(input), selecionadas, novoModelo);
					
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
	
	private void creatFileOut(PdfReader reader, List<Designacao> designacoes, boolean novoModelo) throws IOException, DocumentException {
		File tempFile = File.createTempFile("folhas" + this.files.size(), ".pdf");
	    tempFile.deleteOnExit();
	    
	    OutputStream out = new FileOutputStream(tempFile);
		
		PdfStamper stamper =  new PdfStamper(reader, out);
		AcroFields form = stamper.getAcroFields();
		
		for (int i = 0; i < designacoes.size(); i++) {
			if (novoModelo) {
				this.novoModelo(form, designacoes.get(i), i);
				
			} else {
				this.pre2016(form, designacoes.get(i), i);
			}
		}
		
		stamper.close();
		reader.close();
		
		this.files.add(tempFile);
	}
	
	private void pre2016(AcroFields form, Designacao designacao, int position) throws IOException, DocumentException {
		form.setField("nome" + position, this.capitalize(designacao.getEstudante().getNome()));
		form.setField("numero" + position, designacao.getNumero() + "");
		form.setField("data" + position, designacao.getData().format(Params.dateFormate()));
		form.setField("fonte" + position, designacao.getFonte());
		form.setField("tema" + position, designacao.getTema());
		form.setField("estudo" + position, designacao.getEstudo().getNrEstudo() + "");
		form.setField("sala_" + designacao.getSala()  + "_" + position, "Yes");
		form.setField("obs" + position, designacao.getObsFolha());
		
		if (designacao.getAjudante() != null) {
			form.setField("ajudante"  + position, this.capitalize(designacao.getAjudante().getNome()));
		}
	}
	
	private void novoModelo(AcroFields form, Designacao designacao, int position) throws IOException, DocumentException {
		form.setField("nome" + position, this.capitalize(designacao.getEstudante().getNome()));
		form.setField("data" + position, designacao.getData().format(Params.dateFormate()));
		form.setField("estudo" + position, designacao.getEstudo().getNrEstudo() + "");
		form.setField("ch" + designacao.getSala().toUpperCase()  + position, "Yes");
		form.setField("obs" + position, designacao.getObsFolha());
		
		switch (designacao.getNumero()) {
		case 1:
			form.setField("chLeitura" + position, "Yes");
			break;
		case 2:
			form.setField("chVisita" + position, "Yes");
			break;
		case 3:
			form.setField("chRevisita" + position, "Yes");
			break;
		case 4:
			form.setField("chEstudo" + position, "Yes");
			break;
		}
		
		if (designacao.getAjudante() != null) {
			form.setField("ajud"  + position, this.capitalize(designacao.getAjudante().getNome()));
		}
	}
	
	private String capitalize(String original) {
		StringBuffer novo = new StringBuffer();
		
		if (original != null) {
			String[] palavras = original.toLowerCase().split(" ");
			
			for (String palavra : palavras) {
				if (palavra.trim().length() > 2) {
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
