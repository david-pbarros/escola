package br.com.dbcorp.escolaMinisterio.entidades;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@EntityListeners(RemoveListener.class)
public abstract class Pessoa implements Entidade {

	private int id;
	private String nome;
	private LocalDate ultimaDesignacao;
	private Genero genero;
	private boolean excluido;
	protected String idOnline;
	protected LocalDateTime dtUltimaAtualiza;
	
	@Id @GeneratedValue(strategy=GenerationType.TABLE)
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
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Temporal(TemporalType.DATE)
	public LocalDate getUltimaDesignacao() {
		return ultimaDesignacao;
	}
	public void setUltimaDesignacao(LocalDate ultimaDesignacao) {
		this.ultimaDesignacao = ultimaDesignacao;
	}
	
	@Enumerated(EnumType.ORDINAL)
	public Genero getGenero() {
		return genero;
	}
	public void setGenero(Genero genero) {
		this.genero = genero;
	}
	
	public boolean isExcluido() {
		return excluido;
	}
	public void setExcluido(boolean excluido) {
		this.excluido = excluido;
	}
	
	@PrePersist
	@PreUpdate
	public void dataAtualiza() {
		this.dtUltimaAtualiza = LocalDateTime.now();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Pessoa && ((Pessoa) obj).id == id) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return id * id;
	}
}