package br.com.dbcorp.escolaMinisterio.dataBase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.dbcorp.escolaMinisterio.entidades.Designacao;
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
			senha = this.criptoSenha(senha);
			
			this.localizaUsuario(usuario, senha);
			
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
			usuarioLogado.setSenha(senha);
			
			
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
		Query query = DataBaseHelper.createQuery("FROM Usuario u WHERE u.nome = :nome AND u.senha = :senha")
				.setParameter("nome", usuario)
				.setParameter("senha", senha);
		
		usuarioLogado = (Usuario) query.getSingleResult();
		
		if ("Administrador".equalsIgnoreCase(usuarioLogado.getProfile().getNome())) {
			//this.setAcessoADM();
		}
 	}
	
	//apaga designações em estado de historico e designadas que não tem estudo definido
	public void limparSemEstudo() {
		DataBaseHelper.executeDeleteQuery("DELETE FROM Designacao d WHERE EXISTS (SELECT d1 FROM MesDesignacao m INNER JOIN m.semanas s INNER JOIN s.designacoes d1 "
				+ "WHERE m.status IN ('D', 'F') AND d1 = d AND d1.estudo IS NULL)");
	}
	
	//apaga designações que não tem vinculo com uma semana de reunião.
	public void limparDesignacoesNaoVinculadas() {
		DataBaseHelper.executeDeleteQuery("DELETE FROM Designacao d WHERE NOT EXISTS (SELECT s FROM SemanaDesignacao s WHERE s.designacoes = d)");
	}
	
	@SuppressWarnings("unchecked")
	public void limparDuplicados() {
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
	
	public void limpaIndevidos() {
		DataBaseHelper.executeDeleteQuery("DELETE FROM Designacao d WHERE EXISTS (SELECT d1 FROM Designacao d1 JOIN d1.semana s WHERE d1 = d AND (s.visita = true "
				+ "OR s.assebleia = true OR s.recapitulacao = true OR s.semReuniao = true) )");
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
			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR, 1900);
			
			ultimaSincronia = new Sincronismo();
			ultimaSincronia.setSucesso(true);
			ultimaSincronia.setData(c.getTime());
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
	
	/*private void setAcessoADM() {
		ItemProfile item1 = new ItemProfile();
		ItemProfile item2 = new ItemProfile();
		ItemProfile item3 = new ItemProfile();
		ItemProfile item4 = new ItemProfile();
		ItemProfile item5 = new ItemProfile();
		ItemProfile item6 = new ItemProfile();
		ItemProfile item7 = new ItemProfile();
		ItemProfile item8 = new ItemProfile();
		ItemProfile item9 = new ItemProfile();
		ItemProfile item10 = new ItemProfile();
		ItemProfile item11 = new ItemProfile();
		ItemProfile item12 = new ItemProfile();
		ItemProfile item13 = new ItemProfile();
		ItemProfile item14 = new ItemProfile();
		ItemProfile item15 = new ItemProfile();
		ItemProfile item16 = new ItemProfile();
		ItemProfile item17 = new ItemProfile();
		ItemProfile item18 = new ItemProfile();
		ItemProfile item19 = new ItemProfile();
		ItemProfile item20 = new ItemProfile();
		
		item1.setItem(ItensSeg.MENU_PROGRAMA);
		item2.setItem(ItensSeg.MENU_CADASTRO);
		item3.setItem(ItensSeg.ITM_NOVA);
		item4.setItem(ItensSeg.ITM_AVALIA);
		item5.setItem(ItensSeg.ITM_HISTORICO);
		item6.setItem(ItensSeg.ITM_ESTUDANTES);
		item7.setItem(ItensSeg.ITM_AJUDANTES);
		item8.setItem(ItensSeg.ITM_ESTUDO);
		item9.setItem(ItensSeg.ITM_IMPORTAR);
		item10.setItem(ItensSeg.ITM_DEFRAG);
		item11.setItem(ItensSeg.ITM_SEGURANCA);
		item12.setItem(ItensSeg.NOVA_ADICIONA);
		item13.setItem(ItensSeg.NOVA_SALVA);
		item14.setItem(ItensSeg.NOVA_APROVA);
		item15.setItem(ItensSeg.AVALIA_SALVA);
		item16.setItem(ItensSeg.AVALIA_REABRE);
		item17.setItem(ItensSeg.AVALIA_PRINT);
		item18.setItem(ItensSeg.AVALIA_EXPORT);
		item19.setItem(ItensSeg.AVALIA_APROVA);
		item20.setItem(ItensSeg.HIST_PRINT);
		
		this.addItem(item1);
		this.addItem(item2);
		this.addItem(item3);
		this.addItem(item4);
		this.addItem(item5);
		this.addItem(item6);
		this.addItem(item7);
		this.addItem(item8);
		this.addItem(item9);
		this.addItem(item10);
		this.addItem(item11);
		this.addItem(item12);
		this.addItem(item13);
		this.addItem(item14);
		this.addItem(item15);
		this.addItem(item16);
		this.addItem(item17);
		this.addItem(item18);
		this.addItem(item19);
		this.addItem(item20);
	}
	
	private void addItem(ItemProfile item) {
		if (!usuarioLogado.getProfile().getItens().contains(item)) {
			usuarioLogado.getProfile().getItens().add(item);
			item.setProfile(usuarioLogado.getProfile());
		}
	}*/
}
