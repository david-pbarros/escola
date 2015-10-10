package br.com.dbcorp.escolaMinisterio.entidades;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@EntityListeners(RemoveListener.class)
public class Profile implements Entidade {

	private int id;
	private String nome;
	private List<ItemProfile> itens;
	private String idOnline;
	private LocalDateTime dtUltimaAtualiza;
	
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
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
		
	}
	
	@OneToMany(mappedBy="profile", cascade=CascadeType.ALL)
	public List<ItemProfile> getItens() {
		return itens;
	}
	public void setItens(List<ItemProfile> itens) {
		this.itens = itens;
	}
	
	@PrePersist
	@PreUpdate
	public void dataAtualiza() {
		this.dtUltimaAtualiza = LocalDateTime.now();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Profile && ((Profile) obj).id == id) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return id * id;
	}
}
