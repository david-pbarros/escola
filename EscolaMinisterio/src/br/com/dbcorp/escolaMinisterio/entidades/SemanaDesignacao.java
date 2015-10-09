package br.com.dbcorp.escolaMinisterio.entidades;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.com.dbcorp.escolaMinisterio.dataBase.MyLocalDateConverter;

@Entity
@EntityListeners(RemoveListener.class)
public class SemanaDesignacao implements Comparable<SemanaDesignacao>, Entidade {

	private int id;
	private LocalDate data;
	private boolean visita;
	private boolean assebleia;
	private boolean recapitulacao;
	private boolean semReuniao;
	private List<Designacao> designacoes;
	private List<Designacao> designacoesRemovidas;
	private String idOnline;
	private Date dtUltimaAtualiza;
	private MesDesignacao mes;
	
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
	
	@Convert(converter=MyLocalDateConverter.class)
	@Temporal(TemporalType.DATE)
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
	
	public boolean isSemReuniao() {
		return semReuniao;
	}
	public void setSemReuniao(boolean semReuniao) {
		this.semReuniao = semReuniao;
	}
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="semana")
	public List<Designacao> getDesignacoes() {
		return designacoes;
	}
	public void setDesignacoes(List<Designacao> designacoes) {
		this.designacoes = designacoes;
	}
	
	@Transient
	public List<Designacao> getDesignacoesRemovidas() {
		return designacoesRemovidas;
	}
	public void setDesignacoesRemovidas(List<Designacao> designacoesRemovidas) {
		this.designacoesRemovidas = designacoesRemovidas;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="mes_id")
	public MesDesignacao getMes() {
		return mes;
	}
	public void setMes(MesDesignacao mes) {
		this.mes = mes;
	}
	
	@Override
	public int compareTo(SemanaDesignacao o) {
		return data.compareTo(o.data);
	}
	
	@PrePersist
	@PreUpdate
	public void dataAtualiza() {
		this.dtUltimaAtualiza = new Date();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SemanaDesignacao && ((SemanaDesignacao) obj).id == id) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return id * id;
	}
}