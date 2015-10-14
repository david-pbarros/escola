package br.com.dbcorp.escolaMinisterio.entidades;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import br.com.dbcorp.escolaMinisterio.Params;

public class PersistListener {
	
	@PrePersist
	@PreUpdate
	public void dataAtualiza(Object ent) {
		
		Params.propriedades().put("doSinc", true);

		if (ent instanceof Entidade) {
			((Entidade) ent).setDtUltimaAtualiza(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));

		} else {
			((Estudo) ent).setDtUltimaAtualiza(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
		}
	}
}
