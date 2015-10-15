package br.com.dbcorp.escolaMinisterio.entidades;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@EntityListeners({RemoveListener.class, PersistListener.class})
public class ItemProfile implements Entidade {
	
	public enum ItensSeg {
		MENU_PROGRAMA, MENU_CADASTRO,
		ITM_NOVA, ITM_AVALIA, ITM_HISTORICO, ITM_ESTUDANTES, ITM_AJUDANTES, ITM_ESTUDO, ITM_IMPORTAR, ITM_DEFRAG, ITM_SEGURANCA,
		NOVA_ADICIONA, NOVA_SALVA, NOVA_APROVA,
		AVALIA_SALVA, AVALIA_REABRE, AVALIA_PRINT, AVALIA_EXPORT, AVALIA_APROVA,
		HIST_PRINT;
	}
	
	private int id;
	private ItensSeg item;
	private String idOnline;
	private LocalDateTime dtUltimaAtualiza;
	private Profile profile;
	
	public ItemProfile() {
		super();
	}
	
	public ItemProfile(ItensSeg item) {
		super();
		
		this.item = item;
	}
	
	
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
	
	@Enumerated(EnumType.ORDINAL)
	public ItensSeg getItem() {
		return item;
	}
	public void setItem(ItensSeg item) {
		this.item = item;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="profile_id")
	public Profile getProfile() {
		return profile;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ItemProfile) {
			return this.item == ((ItemProfile) obj).item;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.item.hashCode();
	}
}
