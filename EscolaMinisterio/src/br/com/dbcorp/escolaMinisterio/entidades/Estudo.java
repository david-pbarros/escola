package br.com.dbcorp.escolaMinisterio.entidades;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@EntityListeners({RemoveListener.class, PersistListener.class})
public class Estudo {

	private int nrEstudo;
	private String descricao;
	private boolean leitura;
	private boolean demonstracao;
	private boolean discurso;
	private boolean excluido;
	private LocalDateTime dtUltimaAtualiza;
	
	@Id @GeneratedValue
	public int getNrEstudo() {
		return nrEstudo;
	}
	public void setNrEstudo(int nrEstudo) {
		this.nrEstudo = nrEstudo;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public LocalDateTime getDtUltimaAtualiza() {
		return dtUltimaAtualiza;
	}
	public void setDtUltimaAtualiza(LocalDateTime dtUltimaAtualiza) {
		this.dtUltimaAtualiza = dtUltimaAtualiza;
	}
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public boolean isLeitura() {
		return leitura;
	}
	public void setLeitura(boolean leitura) {
		this.leitura = leitura;
	}
	
	public boolean isDemonstracao() {
		return demonstracao;
	}
	public void setDemonstracao(boolean demonstracao) {
		this.demonstracao = demonstracao;
	}
	
	public boolean isDiscurso() {
		return discurso;
	}
	public void setDiscurso(boolean discurso) {
		this.discurso = discurso;
	}
	
	public boolean isExcluido() {
		return excluido;
	}
	public void setExcluido(boolean excluido) {
		this.excluido = excluido;
	}
}
