package br.com.dbcorp.escolaMinisterio.entidades;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@EntityListeners({RemoveListener.class, PersistListener.class})
public class Usuario implements Entidade {
	
	private int id;
	private String nome;
	private String senha;
	private boolean reiniciaSenha;
	private boolean bloqueado;
	private Profile profile;
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
	
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public boolean isReiniciaSenha() {
		return reiniciaSenha;
	}
	public void setReiniciaSenha(boolean reiniciaSenha) {
		this.reiniciaSenha = reiniciaSenha;
	}
	
	public boolean isBloqueado() {
		return bloqueado;
	}
	public void setBloqueado(boolean bloqueado) {
		this.bloqueado = bloqueado;
	}
	
	@OneToOne
	public Profile getProfile() {
		return profile;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	
	@PrePersist
	@PreUpdate
	public void dataAtualiza() {
		this.dtUltimaAtualiza = LocalDateTime.now();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Usuario && ((Usuario) obj).id == id) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return id * id;
	}
}
