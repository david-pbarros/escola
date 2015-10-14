package br.com.dbcorp.escolaMinisterio.entidades;

import javax.persistence.PreRemove;

import br.com.dbcorp.escolaMinisterio.Params;
import br.com.dbcorp.escolaMinisterio.dataBase.Gerenciador;

public class RemoveListener {
	
	@PreRemove
	public void onRemove(Object entidade) {
		Params.propriedades().put("doSinc", true);
		
		
		if (entidade instanceof Entidade) {
			Entidade e = (Entidade) entidade;
			
			if (e.getIdOnline() != null && e.getIdOnline().length() > 0) {
				new Gerenciador().adicionalogRemocao(entidade.getClass().getSimpleName(), ((Entidade) entidade).getIdOnline());
			}
		} else if (entidade instanceof Estudo) {
			new Gerenciador().adicionalogRemocao(Estudo.class.getSimpleName(), Integer.toString(((Estudo) entidade).getNrEstudo()));
		}
	}
}
