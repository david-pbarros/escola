package br.com.dbcorp.escolaMinisterio.entidades;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@EntityListeners({RemoveListener.class, PersistListener.class})
public class Designacao implements Comparable<Designacao>, Entidade {

	private int id;
	private Estudante estudante;
	private Pessoa ajudante;
	private Estudo estudo;
	private int numero;
	private String sala;
	private String observacao;
	private char status;
	private LocalDate data;
	private String tema;
	private String fonte;
	private String ObsFolha;
	private boolean copiar;
	private String idOnline;
	private LocalDateTime dtUltimaAtualiza;
	private SemanaDesignacao semana;
	
	@Id @GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getIdOnline() {
		return idOnline;
	}
	public void setIdOnline(String idOnline) {
		this.idOnline = idOnline;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public LocalDateTime getDtUltimaAtualiza() {
		return dtUltimaAtualiza;
	}
	public void setDtUltimaAtualiza(LocalDateTime dtUltimaAtualiza) {
		this.dtUltimaAtualiza = dtUltimaAtualiza;
	}
	
	@ManyToOne(cascade={CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name="estudante_id")
	public Estudante getEstudante() {
		return estudante;
	}
	public void setEstudante(Estudante estudante) {
		this.estudante = estudante;
	}
	
	@ManyToOne(cascade={CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name="ajudante_id")
	public Pessoa getAjudante() {
		return ajudante;
	}
	public void setAjudante(Pessoa ajudante) {
		this.ajudante = ajudante;
	}
	
	@ManyToOne(cascade={CascadeType.MERGE, CascadeType.REFRESH})
	@JoinTable(name="designacao_estudo", joinColumns={@JoinColumn(name="designacao_id")}, inverseJoinColumns={@JoinColumn(name="estudo_id")})
	public Estudo getEstudo() {
		return estudo;
	}
	public void setEstudo(Estudo estudo) {
		this.estudo = estudo;
	}
	
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	
	public String getSala() {
		return sala;
	}
	public void setSala(String sala) {
		this.sala = sala;
	}
	
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	
	@Temporal(TemporalType.DATE)
	public LocalDate getData() {
		return data;
	}
	public void setData(LocalDate data) {
		this.data = data;
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
	
	public String getObsFolha() {
		return ObsFolha;
	}
	public void setObsFolha(String obsFolha) {
		ObsFolha = obsFolha;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="semana_id")
	public SemanaDesignacao getSemana() {
		return semana;
	}
	public void setSemana(SemanaDesignacao semana) {
		this.semana = semana;
	}
	
	@Transient
	public boolean isCopiar() {
		return copiar;
	}
	public void setCopiar(boolean copiar) {
		this.copiar = copiar;
	}
	
	@Override
	public int compareTo(Designacao o) {
		if (o.getNumero() == this.numero) {
			return 0;
		
		} else if (o.getNumero() < this.numero) {
			return 1;
		
		} else {
			return -1;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Designacao && ((Designacao) obj).id == id) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return id * id;
	}
}