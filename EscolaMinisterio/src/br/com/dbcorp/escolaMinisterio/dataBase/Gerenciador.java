package br.com.dbcorp.escolaMinisterio.dataBase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.dbcorp.escolaMinisterio.entidades.Designacao;
import br.com.dbcorp.escolaMinisterio.entidades.Estudante;
import br.com.dbcorp.escolaMinisterio.entidades.ItemProfile;
import br.com.dbcorp.escolaMinisterio.entidades.ItemProfile.ItensSeg;
import br.com.dbcorp.escolaMinisterio.entidades.RemoveLog;
import br.com.dbcorp.escolaMinisterio.entidades.Sincronismo;
import br.com.dbcorp.escolaMinisterio.entidades.Usuario;

public class Gerenciador {
	
	protected static Usuario usuarioLogado;
	
	public enum StatusLogon {
		BLOQUEADO("Usuário bloqueado."),
		NAO_LOCALIZADO("Usuário / Senha não localizado."),
		NAO_LOCALIZADO_WEB("Usuário / Senha não localizado."),
		REINICIAR("reiniciar"),
		VALIDO("valido");
		
		public String msg;
		
		private StatusLogon(String msg) {
			this.msg = msg;
		}
	}
	
	public Usuario getUser() {
		return usuarioLogado;
	}
	
	public String criptoSenha(String senha) throws NoSuchAlgorithmException {
		MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(senha.getBytes());
        
        return Base64.getEncoder().encodeToString(result);
	}
	
	public StatusLogon login(String usuario, String senha) throws NoSuchAlgorithmException {
		try {
			this.localizaUsuario(usuario, this.criptoSenha(senha));
			
			if (usuarioLogado.isBloqueado()) {
				return StatusLogon.BLOQUEADO; 
			
			} else if (usuarioLogado.isReiniciaSenha()) {
				return StatusLogon.REINICIAR;
			
			} else {
				return StatusLogon.VALIDO;
			}
		} catch(NoResultException ex) {
			usuarioLogado = new Usuario();
			usuarioLogado.setNome(usuario);
			usuarioLogado.setSenha(this.criptoSenha(senha));
			
			return StatusLogon.NAO_LOCALIZADO;
		}
	}
	
	public void trocaSenha(String senhaNova) throws NoSuchAlgorithmException {
		usuarioLogado.setReiniciaSenha(false);
		usuarioLogado.setSenha(senhaNova);
		
		DataBaseHelper.merge(usuarioLogado);
	}
	
	public boolean isEnable(ItensSeg itensSeg) {
		ItemProfile item = new ItemProfile();
		item.setItem(itensSeg);
		
		return usuarioLogado.getProfile().getItens().contains(item);
	}
	
	public void adicionalogRemocao(String tabela, String idOnline) {
		RemoveLog remove = new RemoveLog();
		remove.setIdOnline(idOnline);
		remove.setTabela(tabela);
		
		DataBaseHelper.persistWTX(remove);
	}
	
	public void localizaUsuario(String usuario, String senha) throws NoSuchAlgorithmException {
		Query query = DataBaseHelper.createQuery("FROM Usuario u WHERE LOWER(u.nome) = :nome AND u.senha = :senha")
				.setParameter("nome", usuario.toLowerCase())
				.setParameter("senha", senha);
		
		usuarioLogado = (Usuario) query.getSingleResult();
		
		if ("Administrador".equalsIgnoreCase(usuarioLogado.getProfile().getNome())) {
			//this.setAcessoADM();
		}
 	}
	
	public Sincronismo pegarUltimo() {
		return this.pegarUltimoInt(1);
	}
	
	public Sincronismo pegarUltimoSeguranca() {
		return this.pegarUltimoInt(2);
	}
	
	@SuppressWarnings("unchecked")
	private Sincronismo pegarUltimoInt(int tipo) {
		Sincronismo ultimaSincronia = null;
		
		Query query = DataBaseHelper.createQuery("FROM Sincronismo s WHERE s.sucesso = true ORDER BY s.data DESC");
		
		List<Sincronismo> itens = query.getResultList();
		
		if (itens.isEmpty() || (tipo == 1 && itens.size() == 1)) {
			ultimaSincronia = new Sincronismo();
			ultimaSincronia.setSucesso(true);
			ultimaSincronia.setDateTime(LocalDateTime.of(1900, Month.JANUARY, 1, 0, 0));
			ultimaSincronia.setCriado(true);
			
		} else {
			ultimaSincronia = itens.get(0);
		}
		
		return ultimaSincronia;
	}
	
	private int pontuar(Designacao designacao) {
		int pontos = 0;
		
		if (designacao.getEstudante() != null) {
			pontos++;
		}
		
		if (designacao.getAjudante() != null) {
			pontos++;
		}
		
		if (designacao.getEstudo() != null) {
			pontos++;
		}
		
		if (designacao.getTema() != null && designacao.getTema().length() > 0) {
			pontos++;
		}
		
		if (designacao.getObservacao() != null && designacao.getObservacao().length() > 0) {
			pontos++;
		}
		
		if (designacao.getObsFolha() != null && designacao.getObsFolha().length() > 0) {
			pontos++;
		}
		
		if (designacao.getFonte() != null && designacao.getFonte().length() > 0) {
			pontos++;
		}
		
		return pontos;
	}
	
	public void desfragmentarBase() {
		this.limparDesignacoesNaoVinculadas();
		this.limparSemEstudo();
		this.limpaIndevidos();
		this.limparDuplicados();
		this.ordenaDesignacoes();
		this.limparSemEstudantes();
	}
	
	protected void limparSemEstudantes() {
		DataBaseHelper.executeDeleteQuery("DELETE FROM Designacao d WHERE d.estudante in (SELECT e FROM Estudante e WHERE e.nome = null OR e.nome = '')");

		DataBaseHelper.executeDeleteQuery("DELETE FROM Estudante e WHERE e.nome = null OR e.nome = ''");
	}
	
	//apaga designações que não tem vinculo com uma semana de reunião.
	private void limparDesignacoesNaoVinculadas() {
		DataBaseHelper.executeDeleteQuery("DELETE FROM Designacao d WHERE NOT EXISTS (SELECT s FROM SemanaDesignacao s WHERE s.designacoes = d)");
	}
	
	//apaga designações em estado de historico e designadas que não tem estudo definido
	private void limparSemEstudo() {
		DataBaseHelper.executeDeleteQuery("DELETE FROM Designacao d WHERE EXISTS (SELECT d1 FROM MesDesignacao m INNER JOIN m.semanas s INNER JOIN s.designacoes d1 "
				+ "WHERE m.status IN ('D', 'F') AND d1 = d AND d1.estudo IS NULL)");
	}
	
	private void limpaIndevidos() {
		DataBaseHelper.executeDeleteQuery("DELETE FROM Designacao d WHERE EXISTS (SELECT d1 FROM Designacao d1 JOIN d1.semana s WHERE d1 = d AND (s.visita = true "
				+ "OR s.assebleia = true OR s.recapitulacao = true OR s.semReuniao = true) )");
	}
	
	@SuppressWarnings("unchecked")
	private void limparDuplicados() {
		Query query = DataBaseHelper.createQuery("SELECT d FROM Designacao d JOIN Designacao d1 ON d1.data = d.data AND d1.sala = d.sala AND d1.numero = d.numero "
				+ "AND d1.id <> d.id ORDER BY d.data, d.sala, d.numero, d.id DESC");
				
		List<Designacao> designacoes = query.getResultList();
		
		if (!designacoes.isEmpty()) {
			Designacao toKeep = designacoes.get(0);
			
			StringBuffer toDelete = new StringBuffer();
			
			for (Designacao designacao : designacoes) {
				if (toKeep.getId() != designacao.getId()) {
					if (toKeep.getNumero() == designacao.getNumero() && toKeep.getSala().equals(designacao.getSala()) && toKeep.getData().equals(designacao.getData())) {
						int keep = this.pontuar(toKeep);
						int list = this.pontuar(designacao);
						
						if (keep < list) {
							toDelete.append(toKeep.getId()).append(",");
							toKeep = designacao;
							
						} else {
							toDelete.append(designacao.getId()).append(",");
						}
					} else {
						toKeep = designacao;
					}
				}
			}
			
			DataBaseHelper.executeDeleteQuery("DELETE FROM Designacao d WHERE d.id IN (" + toDelete.deleteCharAt(toDelete.length() - 1).toString() + ")");
		}
	}
	
	@SuppressWarnings("unchecked")
	private void ordenaDesignacoes() {
		Query query = DataBaseHelper.createQuery("FROM Estudante e JOIN FETCH e.designacoes");
		
		List<Estudante> estudantes = query.getResultList();
		
		estudantes.forEach(e->{
			if (e.getUltimaDesignacao() != null) {
				Optional<Designacao> max = e.getDesignacoes().stream().max(Comparator.comparing(Designacao::getData));
				
				if (max.isPresent()) {
					LocalDate maxDate = max.orElse(null).getData();
					
					if (maxDate != null && !maxDate.isEqual(e.getUltimaDesignacao())) {
						e.setUltimaDesignacao(max.orElse(null).getData());
						e.setSalaUltimaDesignacao(max.orElse(null).getSala().charAt(0));
						
						DataBaseHelper.merge(e);
					}
				}
			}
		});
	}
}
