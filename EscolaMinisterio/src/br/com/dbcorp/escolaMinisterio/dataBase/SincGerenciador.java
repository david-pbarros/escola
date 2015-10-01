package br.com.dbcorp.escolaMinisterio.dataBase;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import br.com.dbcorp.escolaMinisterio.entidades.Ajudante;
import br.com.dbcorp.escolaMinisterio.entidades.Designacao;
import br.com.dbcorp.escolaMinisterio.entidades.Estudante;
import br.com.dbcorp.escolaMinisterio.entidades.Estudo;
import br.com.dbcorp.escolaMinisterio.entidades.ItemProfile;
import br.com.dbcorp.escolaMinisterio.entidades.MesDesignacao;
import br.com.dbcorp.escolaMinisterio.entidades.Profile;
import br.com.dbcorp.escolaMinisterio.entidades.RemoveLog;
import br.com.dbcorp.escolaMinisterio.entidades.SemanaDesignacao;
import br.com.dbcorp.escolaMinisterio.entidades.Usuario;

public class SincGerenciador extends Gerenciador {

	@SuppressWarnings("unchecked")
	public void apagarVelhos() {
		Query query = DataBaseHelper.createQuery("SELECT s.id FROM Sincronismo s ORDER BY s.data DESC")
				.setMaxResults(10);
		
		List<Integer> ids = query.getResultList();
		
		if (!ids.isEmpty()) {
			Properties prop = new Properties();
			prop.put("ids", ids);
			
			DataBaseHelper.executeDeleteQuery("DELETE FROM Sincronismo s WHERE s.id NOT IN (:ids)", prop);
		}
	}
	
	public void salvar(Object entity) {
		DataBaseHelper.persist(entity);
	}
	
	public void atualizar(Object entity) {
		DataBaseHelper.merge(entity);
	}
	
	public void remover(Object entity) {
		DataBaseHelper.remove(entity);
	}
	
	@SuppressWarnings("unchecked")
	public List<RemoveLog> obterRemovidos(String tabela) {
		Query query = DataBaseHelper.createQuery("FROM RemoveLog r WHERE r.tabela = :tabela")
				.setParameter("tabela", tabela);
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<MesDesignacao> obterMesesNovos() {
		Query query = DataBaseHelper.createQuery("FROM MesDesignacao m WHERE m.idOnline IS NULL");
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<MesDesignacao> obterMesesAtualizados(Date ultimaAtualizacao) {
		Query query = DataBaseHelper.createQuery("FROM MesDesignacao m WHERE m.idOnline IS NOT NULL AND m.dtUltimaAtualiza > :data")
				.setParameter("data", ultimaAtualizacao);
		
		return query.getResultList();
	}
	
	public MesDesignacao obterMesDesignacao(String idOnline) {
		Query query = DataBaseHelper.createQuery("FROM MesDesignacao m WHERE m.idOnline = :idOnline")
				.setParameter("idOnline", idOnline);
		
		try {
			return (MesDesignacao) query.getSingleResult();
			
		} catch (NoResultException ex) {
			return new MesDesignacao();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Ajudante> obterAjudantesNovos() {
		Query query = DataBaseHelper.createQuery("FROM Ajudante a WHERE a.idOnline IS NULL");
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Ajudante> obterAjudantesAtualizados(Date ultimaAtualizacao) {
		Query query = DataBaseHelper.createQuery("FROM Ajudante a WHERE a.idOnline IS NOT NULL AND a.dtUltimaAtualiza > :data")
				.setParameter("data", ultimaAtualizacao);
		
		return query.getResultList();
	}
	
	public Ajudante obterAjudante(String idOnline) {
		Query query = DataBaseHelper.createQuery("FROM Ajudante a WHERE a.idOnline = :idOnline")
				.setParameter("idOnline", idOnline);
		
		try {
			return (Ajudante) query.getSingleResult();
			
		} catch (NoResultException ex) {
			return new Ajudante();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Estudante> obterEstudantesNovos() {
		Query query = DataBaseHelper.createQuery("FROM Estudante e WHERE e.idOnline IS NULL AND e.nome IS NOT NULL");
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Estudante> obterEstudantesAtualizados(Date ultimaAtualizacao) {
		Query query = DataBaseHelper.createQuery("FROM Estudante e WHERE e.idOnline IS NOT NULL AND e.dtUltimaAtualiza > :data")
				.setParameter("data", ultimaAtualizacao);
		
		return query.getResultList();
	}
	
	public Estudante obterEstudante(String idOnline) {
		Query query = DataBaseHelper.createQuery("FROM Estudante e WHERE e.idOnline = :idOnline")
				.setParameter("idOnline", idOnline);
		
		try {
			return (Estudante) query.getSingleResult();
			
		} catch (NoResultException ex) {
			return new Estudante();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Estudo> obterEstudosNovos(Date data) {
		Query query = DataBaseHelper.createQuery("FROM Estudo e WHERE e.dtUltimaAtualiza IS NULL OR e.dtUltimaAtualiza > :dataUltima")
				.setParameter("dataUltima", data);
		
		return query.getResultList();
	}
	
	public Estudo obterEstudo(int nrEstudo) {
		Query query = DataBaseHelper.createQuery("FROM Estudo e WHERE e.nrEstudo = :nrEstudo")
				.setParameter("nrEstudo", nrEstudo);
		
		try {
			return (Estudo) query.getSingleResult();
			
		} catch (NoResultException ex) {
			return new Estudo();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Profile> obterProfilesNovos() {
		Query query = DataBaseHelper.createQuery("FROM Profile p WHERE p.idOnline IS NULL");
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Profile> obterProfilesAtualizados(Date ultimaAtualizacao) {
		Query query = DataBaseHelper.createQuery("FROM Profile p WHERE p.idOnline IS NOT NULL AND p.dtUltimaAtualiza > :data")
				.setParameter("data", ultimaAtualizacao);
		
		return query.getResultList();
	}
	
	public Profile obterProfile(String idOnline) {
		Query query = DataBaseHelper.createQuery("FROM Profile p WHERE p.idOnline = :idOnline")
				.setParameter("idOnline", idOnline);
		
		try {
			return (Profile) query.getSingleResult();
			
		} catch (NoResultException ex) {
			return new Profile();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ItemProfile> obterItemProfilesNovos() {
		Query query = DataBaseHelper.createQuery("FROM ItemProfile p WHERE p.idOnline IS NULL");
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<ItemProfile> obterItemProfilesAtualizados(Date ultimaAtualizacao) {
		Query query = DataBaseHelper.createQuery("FROM ItemProfile p WHERE p.idOnline IS NOT NULL AND p.dtUltimaAtualiza > :data")
				.setParameter("data", ultimaAtualizacao);
		
		return query.getResultList();
	}
	
	public ItemProfile obterItemProfile(String idOnline) {
		Query query = DataBaseHelper.createQuery("FROM ItemProfile p WHERE p.idOnline = :idOnline")
				.setParameter("idOnline", idOnline);
		
		try {
			return (ItemProfile) query.getSingleResult();
			
		} catch (NoResultException ex) {
			return new ItemProfile();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Usuario> obterUsuariosNovos() {
		Query query = DataBaseHelper.createQuery("FROM Usuario u WHERE u.idOnline IS NULL AND u.nome IS NOT NULL");
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Usuario> obterUsuariosAtualizados(Date ultimaAtualizacao) {
		Query query = DataBaseHelper.createQuery("FROM Usuario u WHERE u.idOnline IS NOT NULL AND u.dtUltimaAtualiza > :data")
				.setParameter("data", ultimaAtualizacao);
		
		return query.getResultList();
	}
	
	public Usuario obterUsuario(String idOnline) {
		Query query = DataBaseHelper.createQuery("FROM Usuario u WHERE u.idOnline = :idOnline")
				.setParameter("idOnline", idOnline);
		
		try {
			return (Usuario) query.getSingleResult();
			
		} catch (NoResultException ex) {
			return new Usuario();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<SemanaDesignacao> obterSemanasNovas() {
		Query query = DataBaseHelper.createQuery("FROM SemanaDesignacao s WHERE s.idOnline IS NULL");
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<SemanaDesignacao> obterSemanasAtualizadas(Date ultimaAtualizacao) {
		Query query = DataBaseHelper.createQuery("FROM SemanaDesignacao s WHERE s.idOnline IS NOT NULL AND s.dtUltimaAtualiza > :data")
				.setParameter("data", ultimaAtualizacao);
		
		return query.getResultList();
	}
	
	public SemanaDesignacao obterSemanaDesignacao(String idOnline) {
		Query query = DataBaseHelper.createQuery("FROM SemanaDesignacao s WHERE s.idOnline = :idOnline")
				.setParameter("idOnline", idOnline);
		
		try {
			return (SemanaDesignacao) query.getSingleResult();
			
		} catch (NoResultException ex) {
			return new SemanaDesignacao();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Designacao> obterDesignacoesNovas() {
		Query query = DataBaseHelper.createQuery("FROM Designacao d WHERE d.idOnline IS NULL");
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Designacao> obterDesignacoessAtualizadas(Date ultimaAtualizacao) {
		Query query = DataBaseHelper.createQuery("FROM Designacao d WHERE d.idOnline IS NOT NULL AND d.dtUltimaAtualiza > :data")
				.setParameter("data", ultimaAtualizacao);
		
		return query.getResultList();
	}
	
	public Designacao obterDesignacao(String idOnline) {
		Query query = DataBaseHelper.createQuery("FROM Designacao d WHERE d.idOnline = :idOnline")
				.setParameter("idOnline", idOnline);
		
		try {
			return (Designacao) query.getSingleResult();
			
		} catch (NoResultException ex) {
			return new Designacao();
		
		} catch (NonUniqueResultException e) {
			System.out.println(idOnline);
			e.printStackTrace();
			
			throw new RuntimeException();
		}
	}
}
