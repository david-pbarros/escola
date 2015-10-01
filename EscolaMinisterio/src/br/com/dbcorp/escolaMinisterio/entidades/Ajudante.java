package br.com.dbcorp.escolaMinisterio.entidades;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Entity
@EntityListeners(RemoveListener.class)
public class Ajudante extends Pessoa {
	
	@PrePersist
	@PreUpdate
	public void dataAtualiza() {
		this.dtUltimaAtualiza = new Date();
	}
}