package br.com.dbcorp.escolaMinisterio.dataBase;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.dbcorp.escolaMinisterio.entidades.Designacao;
import br.com.dbcorp.escolaMinisterio.entidades.Estudante;
import br.com.dbcorp.escolaMinisterio.entidades.Estudo;
import br.com.dbcorp.escolaMinisterio.entidades.Genero;
import br.com.dbcorp.escolaMinisterio.exceptions.DuplicateKeyException;

public class EstudanteGerenciador extends Gerenciador {
	
	@SuppressWarnings("unchecked")
	public List<Estudante> listarEstudantesTodos(Genero genero) {
		Query query = DataBaseHelper.createQuery("FROM Estudante e JOIN FETCH e.designacoes WHERE (e.excluido = false OR e.excluido IS NULL) AND e.genero = :genero ORDER BY e.nome")
				.setParameter("genero", genero);
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Estudante> listarEstudantes(Genero genero) {
		Query query = DataBaseHelper.createQuery("FROM Estudante e JOIN FETCH e.designacoes WHERE (e.excluido = false OR e.excluido IS NULL) AND "
			+ "(e.desabilitado = false OR e.desabilitado IS NULL) AND e.genero = :genero ORDER BY e.nome")
				.setParameter("genero", genero);
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Estudante> listarEstudantes(Genero genero, String nome) {
		Query query = DataBaseHelper.createQuery("FROM Estudante e JOIN FETCH e.designacoes WHERE (e.excluido = false OR e.excluido IS NULL) AND "
			+ "(e.desabilitado = false OR e.desabilitado IS NULL) AND e.genero = :genero AND e.nome LIKE :nome  ORDER BY e.nome")
				.setParameter("genero", genero)
				.setParameter("nome", "%" + nome + "%");
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Estudo> listarEstudos() {
		Query query = DataBaseHelper.createQuery("FROM Estudo e ORDER BY e.nrEstudo");
		
		return query.getResultList();
	}
	
	public void inserirDesignacao(Designacao designacao) {
		DataBaseHelper.persist(designacao);
	}
	
	public void inserir(Estudante estudante) throws DuplicateKeyException {
		Query query = DataBaseHelper.createQuery("FROM Estudante e WHERE (e.excluido = false OR e.excluido IS NULL) AND LOWER(e.nome) = :nome")
				.setParameter("nome", estudante.getNome().trim().toLowerCase());
		
		try {
			query.getSingleResult();
			throw new DuplicateKeyException();
			
		} catch (NoResultException exception) {
			estudante.setNome(estudante.getNome().trim());
			DataBaseHelper.persist(estudante);
			
		}
	}

	public void atualizar(Estudante estudante) {
		estudante.setNome(estudante.getNome().trim());
		DataBaseHelper.merge(estudante);
	}
	
	public void remover(Estudante estudante) {
		Query query = DataBaseHelper.createQuery("FROM Designacao d WHERE d.estudante.id = :idEstudante")
				.setParameter("idEstudante", estudante.getId());
		
		if (!query.getResultList().isEmpty()) {
			estudante.setExcluido(true);

			DataBaseHelper.beginTX();
			DataBaseHelper.mergeWTX(estudante);
			DataBaseHelper.commitTX();
			
		} else {
			DataBaseHelper.remove(estudante);
		}
	}
}
