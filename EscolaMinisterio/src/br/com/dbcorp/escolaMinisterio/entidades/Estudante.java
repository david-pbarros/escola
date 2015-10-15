package br.com.dbcorp.escolaMinisterio.entidades;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Transient;

@Entity
@EntityListeners({RemoveListener.class, PersistListener.class})
public class Estudante extends Pessoa {

	private String observacao;
	private char salaUltimaDesignacao;
	private boolean desabilitado;
	private boolean naoAjudante;
	private List<Designacao> designacoes;
	private Map<Integer, Designacao> estudos;
	
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	public char getSalaUltimaDesignacao() {
		return salaUltimaDesignacao;
	}
	public void setSalaUltimaDesignacao(char salaUltimaDesignacao) {
		this.salaUltimaDesignacao = salaUltimaDesignacao;
	}
	
	public boolean isDesabilitado() {
		return desabilitado;
	}
	public void setDesabilitado(boolean desabilitado) {
		this.desabilitado = desabilitado;
	}
	
	public boolean isNaoAjudante() {
		return naoAjudante;
	}
	public void setNaoAjudante(boolean naoAjudante) {
		this.naoAjudante = naoAjudante;
	}
	
	@OneToMany(mappedBy="estudante", cascade=CascadeType.ALL)
	public List<Designacao> getDesignacoes() {
		return designacoes;
	}
	public void setDesignacoes(List<Designacao> designacoes) {
		this.designacoes = designacoes;
	}
	
	@Transient
	public Map<Integer, Designacao> getEstudos() {
		return estudos;
	}
	public void setEstudos(Map<Integer, Designacao> estudos) {
		this.estudos = estudos;
	}
	
	@PostLoad
	public void onLoad() {
		if (this.estudos == null) {
			this.estudos = new HashMap<Integer, Designacao>();
		}
		
		for (Designacao designacao : this.designacoes) {
			if (designacao.getEstudo() != null) {
				this.estudos.put(designacao.getEstudo().getNrEstudo(), designacao);
			}
		}
	}
	
	public void reLoad() {
		this.estudos = new HashMap<Integer, Designacao>();
		
		for (Designacao designacao : this.designacoes) {
			if (designacao.getEstudo() != null) {
				this.estudos.put(designacao.getEstudo().getNrEstudo(), designacao);
			}
		}
	}
}