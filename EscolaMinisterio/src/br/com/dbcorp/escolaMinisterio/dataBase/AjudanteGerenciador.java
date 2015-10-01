package br.com.dbcorp.escolaMinisterio.dataBase;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.dbcorp.escolaMinisterio.entidades.Ajudante;
import br.com.dbcorp.escolaMinisterio.entidades.RemoveListener;
import br.com.dbcorp.escolaMinisterio.exceptions.DuplicateKeyException;

public class AjudanteGerenciador {

	@SuppressWarnings("unchecked")
	public List<Ajudante> listarAjudantes() {
		Query query = DataBaseHelper.createQuery("FROM Ajudante a WHERE a.excluido = false OR a.excluido IS NULL ORDER BY a.nome");
		
		return query.getResultList();
	}
	
	public void inserir(Ajudante ajudante) throws DuplicateKeyException {
		Query query = DataBaseHelper.createQuery("FROM Ajudante a WHERE (a.excluido = false OR a.excluido IS NULL ) AND a.nome = :nome")
				.setParameter("nome", ajudante.getNome());
		
		try {
			query.getSingleResult();
			throw new DuplicateKeyException();
			
		} catch (NoResultException exception) {
			DataBaseHelper.persist(ajudante);
			
		}
	}
	
	public void atualizar(Ajudante ajudante) {
		DataBaseHelper.merge(ajudante);
	}
	
	public void remover(Ajudante ajudante) {
		Query query = DataBaseHelper.createQuery("FROM Designacao d WHERE d.ajudante.id = :idAjudante")
				.setParameter("idAjudante", ajudante.getId());
		
		if (!query.getResultList().isEmpty()) {
			DataBaseHelper.beginTX();
			
			ajudante.setExcluido(true);
			DataBaseHelper.mergeWTX(ajudante);
			new RemoveListener().onRemove(ajudante);

			DataBaseHelper.commitTX();
			
		} else {
			DataBaseHelper.remove(ajudante);
		}
	}
}