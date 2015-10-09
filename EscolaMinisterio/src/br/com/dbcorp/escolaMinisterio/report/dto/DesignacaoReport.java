package br.com.dbcorp.escolaMinisterio.report.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class DesignacaoReport implements Serializable, Comparable<DesignacaoReport> {
	private static final long serialVersionUID = -8093370049494799241L;

	private LocalDate data;
	private boolean visita;
	private boolean assebleia;
	private boolean recapitulacao;
	private Integer numero;
	private String sala;
	private String estudante;
	private String ajudante;
	private int nrEstudo;
	private String tema;
	private String fonte;
	
	public LocalDate getData() {
		return data;
	}
	public void setData(LocalDate data) {
		this.data = data;
	}
	
	public boolean isVisita() {
		return visita;
	}
	public void setVisita(boolean visita) {
		this.visita = visita;
	}
	
	public boolean isAssebleia() {
		return assebleia;
	}
	public void setAssebleia(boolean assebleia) {
		this.assebleia = assebleia;
	}
	
	public boolean isRecapitulacao() {
		return recapitulacao;
	}
	public void setRecapitulacao(boolean recapitulacao) {
		this.recapitulacao = recapitulacao;
	}
	
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	
	public String getSala() {
		return sala;
	}
	public void setSala(String sala) {
		this.sala = sala;
	}
	
	public String getEstudante() {
		return estudante;
	}
	public void setEstudante(String estudante) {
		this.estudante = estudante;
	}

	public String getAjudante() {
		return ajudante;
	}
	public void setAjudante(String ajudante) {
		this.ajudante = ajudante;
	}
	
	public int getNrEstudo() {
		return nrEstudo;
	}
	public void setNrEstudo(int nrEstudo) {
		this.nrEstudo = nrEstudo;
	}
	
	public String getTema() {
		return tema;
	}
	public void setTema(String tema) {
		this.tema = tema;
	}
	
	public String getFonte() {
		return fonte;
	}
	public void setFonte(String fonte) {
		this.fonte = fonte;
	}
	
	public DesignacaoReport clone() {
		DesignacaoReport temp = new DesignacaoReport();
		
		temp.setData(this.getData());
		temp.setAssebleia(this.isAssebleia());
		temp.setRecapitulacao(this.isRecapitulacao());
		temp.setVisita(this.isVisita());
		
		return temp;
	}
	public int compareTo(DesignacaoReport o) {
		if (o.getData().equals(this.getData())) {
			return this.numero.compareTo(o.getNumero());
			
		} else {
			return this.data.compareTo(o.getData());
		}
	}
}
