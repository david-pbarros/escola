package br.com.dbcorp.escolaMinisterio.dataBase;

import java.util.List;

import javax.persistence.Query;

import br.com.dbcorp.escolaMinisterio.entidades.Estudo;
import br.com.dbcorp.escolaMinisterio.entidades.RemoveListener;
import br.com.dbcorp.escolaMinisterio.exceptions.DuplicateKeyException;

public class EstudoGerenciador {

	@SuppressWarnings("unchecked")
	public List<Estudo> listarEstudos() {
		Query query = DataBaseHelper.createQuery("FROM Estudo e WHERE e.excluido = false OR e.excluido IS NULL ORDER BY e.nrEstudo");
		
		return query.getResultList();
	}
	
	public void inserir(Estudo estudo) throws DuplicateKeyException {
		if (DataBaseHelper.find(Estudo.class, estudo.getNrEstudo()) == null) {
			DataBaseHelper.persist(estudo);
			
		} else {
			throw new DuplicateKeyException();
		}
	}
	
	public void atualizar(Estudo estudo) {
		DataBaseHelper.merge(estudo);
	}
	
	public void remover(Estudo estudo) {
		Query query = DataBaseHelper.createQuery("FROM Designacao d WHERE d.estudo.nrEstudo = :nrEstudo")
				.setParameter("nrEstudo", estudo.getNrEstudo());
		
		if (!query.getResultList().isEmpty()) {
			DataBaseHelper.beginTX();
			
			estudo.setExcluido(true);
			DataBaseHelper.mergeWTX(estudo);
			new RemoveListener().onRemove(estudo);

			DataBaseHelper.commitTX();
			
		} else {
			DataBaseHelper.remove(estudo);
		}
	}
}
