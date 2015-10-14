package br.com.dbcorp.escolaMinisterio.entidades;

import java.time.LocalDateTime;

public interface Entidade {
	String getIdOnline();
	
	void setDtUltimaAtualiza(LocalDateTime dtUltimaAtualiza);
}
