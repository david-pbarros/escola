package br.com.dbcorp.escolaMinisterio.dataBase;

import java.util.List;

import javax.persistence.Query;

import br.com.dbcorp.escolaMinisterio.entidades.ItemProfile;
import br.com.dbcorp.escolaMinisterio.entidades.Profile;
import br.com.dbcorp.escolaMinisterio.exceptions.DuplicateKeyException;

public class ProfileGerenciador {
	
	@SuppressWarnings("unchecked")
	public List<Profile> listarProfiles() {
		Query query = DataBaseHelper.createQuery("FROM Profile p ORDER BY p.nome");
		
		return query.getResultList();
	}
	
	public void inserir(Profile profile) throws DuplicateKeyException {
		if (profile.getId() == 0) {
			DataBaseHelper.persist(profile);
			
		} else {
			throw new DuplicateKeyException();
		}
	}
	
	public void salvar(Profile profile) {
		if (profile.getId() != 0) {
			DataBaseHelper.merge(profile);
		}
	}
	
	public void remover(Profile profile) {
		DataBaseHelper.remove(profile);
	}
	
	public void removerItem(ItemProfile item) {
		DataBaseHelper.remove(item);
	}
}
