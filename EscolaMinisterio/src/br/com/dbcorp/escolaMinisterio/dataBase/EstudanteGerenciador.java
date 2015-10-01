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
		Query query = DataBaseHelper.createQuery("FROM Estudante e JOIN FETCH e.designacoes WHERE e.genero = :genero")
				.setParameter("genero", genero);
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Estudante> listarEstudantes(Genero genero) {
		Query query = DataBaseHelper.createQuery("FROM Estudante e JOIN FETCH e.designacoes WHERE (e.desabilitado = false OR e.desabilitado IS NULL) AND e.genero = :genero")
				.setParameter("genero", genero);
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Estudante> listarEstudantes(Genero genero, String nome) {
		Query query = DataBaseHelper.createQuery("FROM Estudante e JOIN FETCH e.designacoes WHERE (e.desabilitado = false OR e.desabilitado IS NULL) AND e.genero = :genero AND e.nome LIKE :nome")
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
		Query query = DataBaseHelper.createQuery("FROM Estudante e WHERE e.nome = :nome")
				.setParameter("nome", estudante.getNome());
		
		try {
			query.getSingleResult();
			throw new DuplicateKeyException();
			
		} catch (NoResultException exception) {
			DataBaseHelper.persist(estudante);
			
		}
	}

	public void atualizar(Estudante estudante) {
		DataBaseHelper.merge(estudante);
	}
	
	public void remover(Estudante estudante) {
		DataBaseHelper.remove(estudante);
	}
}
