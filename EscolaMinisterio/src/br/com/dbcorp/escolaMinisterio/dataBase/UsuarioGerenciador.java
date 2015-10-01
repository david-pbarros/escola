package br.com.dbcorp.escolaMinisterio.dataBase;

import java.util.List;

import javax.persistence.Query;

import br.com.dbcorp.escolaMinisterio.entidades.Usuario;
import br.com.dbcorp.escolaMinisterio.exceptions.DuplicateKeyException;

public class UsuarioGerenciador extends Gerenciador {
	
	@SuppressWarnings("unchecked")
	public List<Usuario> listarUsuarios() {
		Query query = DataBaseHelper.createQuery("FROM Usuario u ORDER BY u.nome");
		
		return query.getResultList();
	}
	
	public void inserir(Usuario usuario) throws DuplicateKeyException {
		if (usuario.getId() == 0) {
			DataBaseHelper.persist(usuario);
			
		} else {
			throw new DuplicateKeyException();
		}
	}
	
	public void salvar(Usuario usuario) {
		if (usuario.getId() != 0) {
			DataBaseHelper.merge(usuario);
		}
	}
	
	public void remover(Usuario usuario) {
		DataBaseHelper.remove(usuario);
	}
}
