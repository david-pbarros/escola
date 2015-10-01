package br.com.dbcorp.escolaMinisterio.entidades;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.dbcorp.escolaMinisterio.MesesDom;

@Entity
public class MesDesignacao implements Entidade {
	
	private int id;
	private MesesDom mes;
	private int ano;
	private char status;
	private List<SemanaDesignacao> semanas;
	private String idOnline;
	private Date dtUltimaAtualiza;

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
	public Date getDtUltimaAtualiza() {
		return dtUltimaAtualiza;
	}
	public void setDtUltimaAtualiza(Date dtUltimaAtualiza) {
		this.dtUltimaAtualiza = dtUltimaAtualiza;
	}
	
	@Enumerated(EnumType.ORDINAL)
	public MesesDom getMes() {
		return mes;
	}
	public void setMes(MesesDom mes) {
		this.mes = mes;
	}
	
	public int getAno() {
		return ano;
	}
	public void setAno(int ano) {
		this.ano = ano;
	}
	
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	
	@OneToMany(mappedBy="mes", cascade=CascadeType.ALL)
	public List<SemanaDesignacao> getSemanas() {
		return semanas;
	}
	public void setSemanas(List<SemanaDesignacao> semanas) {
		this.semanas = semanas;
	}
	
	@PostLoad
	public void ordenaSemanas() {
		Collections.sort(this.semanas);
	}
	
	@PrePersist
	@PreUpdate
	public void dataAtualiza() {
		this.dtUltimaAtualiza = new Date();
	}
	
	@PreRemove
	public void onRemove() {
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MesDesignacao && ((MesDesignacao) obj).id == id) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return id * id;
	}
}