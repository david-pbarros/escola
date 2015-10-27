package br.com.dbcorp.escolaMinisterio.report;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import br.com.dbcorp.escolaMinisterio.Log;
import br.com.dbcorp.escolaMinisterio.entidades.Designacao;
import br.com.dbcorp.escolaMinisterio.entidades.MesDesignacao;
import br.com.dbcorp.escolaMinisterio.entidades.SemanaDesignacao;
import br.com.dbcorp.escolaMinisterio.report.dto.DesignacaoReport;

public class ReportCommon {
	
	private Log log = Log.getInstance();
	
	public void gerarTipoDirigente(MesDesignacao mesDesignacao) {
		this.comum(mesDesignacao, "designacao", true);
	}
	
	public void gerarTipoAuxiliar(MesDesignacao mesDesignacao) {
		this.comum(mesDesignacao, "auxiliar", false);
	}
	
	public void gerarTipoDesignacoes(MesDesignacao mesDesignacao) {
		try {
			List<Designacao> designacoes = new ArrayList<Designacao>();
		
			for (SemanaDesignacao semana : mesDesignacao.getSemanas()) {
				List<Designacao> salaA = new ArrayList<Designacao>();
				List<Designacao> salaB = new ArrayList<Designacao>();
				
				if (semana.getDesignacoes() != null) {
					for (Designacao designacao : semana.getDesignacoes()) {
						if ("A".equalsIgnoreCase(designacao.getSala())) {
							salaA.add(designacao);
							
						} else {
							salaB.add(designacao);
						}
					}
				}
				
				Collections.sort(salaA);
				Collections.sort(salaB);
				
				designacoes.addAll(salaA);
				designacoes.addAll(salaB);
			}
			
			if (!designacoes.isEmpty()) {
				ItextReportManager rp = new ItextReportManager();
				
				Set<File> arquivos = rp.createReport("designacoes"  + (mesDesignacao.getAno() > 2015 ? "" : "Pre2016"), designacoes);
				
				if (!arquivos.isEmpty()) {
					for (File tempFile : arquivos) {
						Desktop.getDesktop().open(tempFile);
					}
				}
			}
		} catch (Exception ex) {
			this.log.error("", ex);
			
			JOptionPane.showMessageDialog(null, "Não foi possivel gerar o relatório.", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void comum(MesDesignacao mesDesignacao, String reportName, boolean especiais) {
		Properties prop = new Properties();
		prop.put("mes", mesDesignacao.getMes().toString());
		
		ReportManager rp = new ReportManager();
		
		reportName += (mesDesignacao.getAno() > 2015 ? "" : "Pre2016");

		OutputStream out = null;
		
		try {
			rp.createReport(reportName, prop, new JRBeanCollectionDataSource(this.prepararSemanas(mesDesignacao, especiais)));
			
			InputStream in = rp.toPDF();
			
			File tempFile = File.createTempFile(reportName, ".pdf");
		    tempFile.deleteOnExit();
			
			out = new FileOutputStream(tempFile);
			
			int read = 0;
			byte[] bytes = new byte[1024];
	 
			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			
			Desktop.getDesktop().open(tempFile);
			
		} catch (Exception ex) {
			this.log.error("", ex);
			
			JOptionPane.showMessageDialog(null, "Não foi possivel gerar o relatório.", "Erro", JOptionPane.ERROR_MESSAGE);
			
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					this.log.error("", e);
				}
			}
		}
	}
	
	private List<DesignacaoReport> prepararSemanas(MesDesignacao mesDesignacao, boolean especiais) {
		List<DesignacaoReport> semanas = new ArrayList<DesignacaoReport>();
		
		List<DesignacaoReport> semanasA = new ArrayList<DesignacaoReport>();
		List<DesignacaoReport> semanasB = new ArrayList<DesignacaoReport>();
		
		for (SemanaDesignacao semana : mesDesignacao.getSemanas()) {
			if (!semana.isSemReuniao() && !semana.isVideos()) {
				DesignacaoReport designacaoReport = new DesignacaoReport();
				designacaoReport.setData(semana.getData());
				designacaoReport.setAssebleia(semana.isAssebleia());
				designacaoReport.setRecapitulacao(semana.isRecapitulacao());
				designacaoReport.setVisita(semana.isVisita());
				designacaoReport.setVideos(semana.isVideos());
				
				if (especiais && (designacaoReport.isAssebleia() || designacaoReport.isRecapitulacao() || designacaoReport.isVisita() || designacaoReport.isVideos())) {
					DesignacaoReport temp = designacaoReport.clone();
					temp.setSala("A");
					
					DesignacaoReport temp2 = designacaoReport.clone();
					temp2.setSala("B");
					
					semanasA.add(temp);
					semanasB.add(temp2);
				
				} else {
					for (Designacao designacao : semana.getDesignacoes()) {
						DesignacaoReport temp = designacaoReport.clone();
						temp.setSala(designacao.getSala());
						temp.setNumero(designacao.getNumero());
						temp.setNrEstudo(designacao.getEstudo().getNrEstudo());
						temp.setTema(designacao.getTema());
						temp.setFonte(designacao.getFonte());
						
						if (designacao.getEstudante() != null) {
							temp.setEstudante(designacao.getEstudante().getNome());
						}
						
						if (designacao.getAjudante() != null) {
							temp.setAjudante(designacao.getAjudante().getNome());
						}
						
						if ("A".equals(temp.getSala())) {
							semanasA.add(temp);
						
						} else {
							semanasB.add(temp);
						}
					}
				}
			}
		}
		
		Collections.sort(semanasA);
		Collections.sort(semanasB);
		
		semanas.addAll(semanasA);
		semanas.addAll(semanasB);
		
		return semanas;
	}
}