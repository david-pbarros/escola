package br.com.dbcorp.escolaMinisterio.sincronismo;

import java.io.IOException;
import java.io.StringReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.dbcorp.escolaMinisterio.Log;
import br.com.dbcorp.escolaMinisterio.Params;
import br.com.dbcorp.escolaMinisterio.dataBase.SincGerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.Sincronismo;
import br.com.dbcorp.escolaMinisterio.entidades.Usuario;
import br.com.dbcorp.escolaMinisterio.sincronismo.PHPConnection.HTTP_METHOD;

public class Sincronizador {

	private Log log = Log.getInstance();
	
	private SincGerenciador gerenciador;
	private PublicKey chave;
	private Sincronismo ultimaSincronia;
	private String hash;
	private StringBuffer mensagensTela;
	
	private JTextArea textArea;
	
	private MesSinc mes;
	private AjudanteSinc ajudante;
	private EstudanteSinc estudante;
	private EstudoSinc estudo;
	private ProfileSinc profile;
	private ItemProfileSinc itemProfile;
	private UsuarioSinc usuario;
	private SemanaSinc semana;
	private DesignacaoSinc designacao;
	
	public Sincronizador(JTextArea textArea) {
		this.gerenciador = new SincGerenciador();
		this.textArea = textArea;
		this.mensagensTela = new StringBuffer();
		this.refreshMsg("Iniciando sincronismo com WEB...");
	}
	
	public Sincronizador() {
		this(null);
	}
	
	public void verificaSinc() {
		try {
			this.obterChave();
			this.gerarHash();
			
			PHPConnection con = new PHPConnection("/service.php/lastSinc", HTTP_METHOD.GET, this.hash);
			con.connect();
			
			if (con.getResponseCode() != 200) {
				throw new RuntimeException(con.getErrorDetails());
			}
			
			JSONObject obj = con.getResponse();
			
			if (obj != null) {
				if ("existente".equalsIgnoreCase(obj.getString("response"))) {
					Date temp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(obj.getString("data"));
					
					//LocalDateTime temp = LocalDateTime.parse(obj.getString("data"), br.com.dbcorp.escolaMinisterio.ui.Params.dateTimeFormate());
					
					Params.propriedades().put("doSinc", this.gerenciador.pegarUltimo().getData().getTime() < temp.getTime());
					
				} else {
					Params.propriedades().put("doSinc", true);
				}
			}
		} catch (Exception ex) {
			this.log.error("Erro verificando sincronismo", ex);
		}
	}
	
	public void finalizaSinc(boolean hasErro) {
		try {
			this.obterChave();
			this.gerarHash();
			
			PHPConnection con = new PHPConnection("/service.php/lastSinc", HTTP_METHOD.POST, this.hash);
			con.setParameter("status", hasErro ? "I" : "C");
			con.connect();
			
			if (con.getResponseCode() != 200) {
				throw new RuntimeException(con.getErrorDetails());
			}
		} catch (Exception ex) {
			Params.propriedades().put("doSinc", true);
		}
	}
	
	public String versao() {
		String retorno = "";
		
		try {
			this.obterChave();
			this.gerarHash();
			
			PHPConnection con = new PHPConnection("/service.php/versao", HTTP_METHOD.GET, this.hash);
			con.connect();
			
			if (con.getResponseCode() != 200) {
				throw new RuntimeException(con.getErrorDetails());
			}
			
			JSONObject obj = con.getResponse();
			
			if (obj != null) {
				int versao = (int) Params.propriedades().get("verionNumber");
				
				if (versao < obj.getInt("versao")) {
					retorno = "\nExiste nova versão do sistema para download.";
					
					String msg = obj.getString("msg");
					
					if (msg.length() > 0) {
						retorno += "\n" + msg;
					}
				}
			}
		} catch (Exception ex) {
			retorno =  "";
		}
		
		return retorno;
	}
	
	public void verificaVersao() {
		String retorno = this.versao();
		
		if (!"".equals(retorno)) {
			JOptionPane.showMessageDialog(null, retorno, "Nova Versão", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public boolean login() {
		try {
			this.obterChave();
			this.gerarHash();
			
			PHPConnection con = new PHPConnection("/service.php/logon", HTTP_METHOD.POST, this.hash);
			con.connect();
			
			if (con.getResponseCode() != 200) {
				throw new RuntimeException(con.getErrorDetails());
			}
			
			JSONObject obj = con.getResponse();
			
			return "OK".equalsIgnoreCase(obj.getString("response"));
			
		} catch (Exception ex) {
			log.error("Erro no no logon.", ex);
			return false;
		}
	}
	
	public boolean trocaSenha(String senha, boolean reinicia, Usuario usuario) {
		try {
			this.obterChave();
			this.gerarHash();
			
			if (usuario == null) {
				usuario = this.gerenciador.getUser();
			}
			
			PHPConnection con = new PHPConnection("/service.php/pass", HTTP_METHOD.POST, this.hash);
			con.setParameter("senha", senha);
			con.setParameter("reiniciaSenha", reinicia ? 1 : 0);
			con.setParameter("id_online", usuario.getIdOnline());
			con.connect();
			
			if (con.getResponseCode() != 200) {
				this.log.error(con.getErrorDetails());
				return false;
				
			} else {
				JSONObject obj = con.getResponse();
				
				return "OK".equalsIgnoreCase(obj.getString("response"));
			}
		} catch (Exception ex) {
			return false;
		}
	}
	
	public void sincronizarSeguranca() {
		Sincronismo sinc  = new Sincronismo();
		sinc.setSucesso(true);
		
		try {
			this.obterChave();
			this.gerarHash();
			
			this.ultimaSincronia = this.gerenciador.pegarUltimo();
			
			this.profile = new ProfileSinc(this.gerenciador, this.ultimaSincronia, this.hash);
			this.itemProfile = new ItemProfileSinc(this.gerenciador, this.ultimaSincronia, this.hash);
			this.usuario = new UsuarioSinc(this.gerenciador, this.ultimaSincronia, this.hash);
			
			this.refreshMsg("\nObtendo novos perfis...");
			if (!this.hasError("\nProblemas nos profiles recebidos:", this.profile.obterNovos())) {
				this.refreshMsg("\nObtendo novos itens de segurança...");
				if (!this.hasError("\nProblemas nos itens de segurança recebidos:", this.itemProfile.obterNovos())) {
					this.refreshMsg("\nObtendo novos usuarios...");
					this.hasError("\nProblemas nos usuarios recebidos:", this.usuario.obterNovos());
				}
			}
			
			this.refreshMsg("\nFim do sincronismo. Reinicie o sistema.");
			
		} catch (Exception ex) {
			String erro = "Erro inesperado durante o sincronismo de informações.";
			this.log.error(erro, ex);
			
			this.refreshMsg("\n" + erro+ " Consultar Log.");
			
			sinc.setSucesso(false);
		}
		
		sinc.setData(new Date());
		this.gerenciador.salvar(sinc);
	}
	
	public void sincronizar() {
		boolean hasErro = false;
		
		Sincronismo sinc = new Sincronismo();
		
		try {
			
			this.obterChave();
			this.gerarHash();
			
			this.gerenciador.apagarVelhos();
			this.ultimaSincronia = this.gerenciador.pegarUltimo();
			Sincronismo ultimoSeguranca = this.gerenciador.pegarUltimoSeguranca();

			this.mes = new MesSinc(this.gerenciador, this.ultimaSincronia, this.hash);
			this.ajudante = new AjudanteSinc(this.gerenciador, this.ultimaSincronia, this.hash);
			this.estudante = new EstudanteSinc(this.gerenciador, this.ultimaSincronia, this.hash);
			this.estudo = new EstudoSinc(this.gerenciador, this.ultimaSincronia, this.hash);
			this.profile = new ProfileSinc(this.gerenciador, ultimoSeguranca, this.hash);
			this.itemProfile = new ItemProfileSinc(this.gerenciador, ultimoSeguranca, this.hash);
			this.usuario = new UsuarioSinc(this.gerenciador, ultimoSeguranca, this.hash);
			this.semana = new SemanaSinc(this.gerenciador, this.ultimaSincronia, this.hash);
			this.designacao = new DesignacaoSinc(this.gerenciador, this.ultimaSincronia, this.hash);
			
			this.refreshMsg("\nDesfragmentando a base...");
			this.gerenciador.desfragmentarBase();

			hasErro = true;
			
			if (this.apagarLocal()) {
				if (this.apagaWeb()) {
					if (this.enviarNovos()) {
						if (this.atualizarWeb()) {
							if (this.obterNovos()) {
								hasErro = false;
								Params.propriedades().put("doSinc", false);
							}
						}
					}
				}
			}
			
			this.finalizaSinc(hasErro);
			
			this.refreshMsg("\nFim do sincronismo. Reinicie o sistema.");
			
		} catch (Exception ex) {
			String erro = "Erro inesperado durante o sincronismo de informações.";
			this.log.error(erro, ex);
			
			this.refreshMsg("\n" + erro + " Consultar Log.");
			
			hasErro = true;
		}
		
		sinc.setData(new Date());
		sinc.setSucesso(!hasErro);
		
		this.gerenciador.salvar(sinc);
	}
	
	private void obterChave() throws IOException {
		if (Security.getProvider("BC") == null) {
		    Security.addProvider(new BouncyCastleProvider());
		}
		
		byte[] hash = Base64.getDecoder().decode(Params.propriedades().getProperty("hash"));
		
		String keyString = new String(hash, "UTF-8");
		
		 PEMReader pemReader= new PEMReader(new StringReader(keyString));
		 this.chave = (PublicKey) pemReader.readObject();
		 
		 pemReader.close();
	}
	
	private void gerarHash() throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		StringBuffer hash = new StringBuffer(Params.propriedades().getProperty("congregacao")).append(";");
		
		if (this.gerenciador.getUser() != null) {
			hash.append(this.gerenciador.getUser().getNome()).append(";")
				.append(this.gerenciador.getUser().getSenha()).append(";");
		
		} else {
			hash.append(";").append(";");
		}
		
		hash.append(Params.propriedades().get("verionNumber"));
		
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
		cipher.init(Cipher.ENCRYPT_MODE, this.chave);
		
		byte[] ciphered = cipher.doFinal(hash.toString().getBytes());
		
		this.hash = Base64.getEncoder().encodeToString(ciphered);
	}
	
	private boolean apagarLocal() throws JSONException, IOException {
		boolean correto = false;
		
		this.refreshMsg("\nSincronizando usuários removidos...");
		if (!this.hasError("\nProblemas apagando usuarios:", this.usuario.apagar())) {
			this.refreshMsg("\nSincronizando itens de segurança removidos...");
			if (!this.hasError("\nProblemas apagando itens de segurança:", this.itemProfile.apagar())) {
				this.refreshMsg("\nSincronizando perfis removidos...");
				if (!this.hasError("\nProblemas apagando perfis:", this.profile.apagar())) {
					this.refreshMsg("\nSincronizando designações removidos...");
					if (!this.hasError("\nProblemas apagando designações:", this.designacao.apagar())) {
						this.refreshMsg("\nSincronizando ajudantes removidos...");
						if (!this.hasError("\nProblemas apagando ajudantes:", this.ajudante.apagar())) {
							this.refreshMsg("\nSincronizando estudantes removidos...");
							if (!this.hasError("\nProblemas apagando estudantes:", this.estudante.apagar())) {
								this.refreshMsg("\nSincronizando estudos removidos...");
								if (!this.hasError("\nProblemas apagando estudos:", this.estudo.apagar())) {
									correto = true;
								}
							}
						}
					}
				}
			}	
		}
				
		return correto;
	}
	
	private boolean apagaWeb() throws IOException {
		boolean correto = false;
		
		this.refreshMsg("\nSincronizando usuários removidos na WEB...");
		if (!this.hasError("\nProblemas apagando usuarios na web:", this.usuario.apagarWeb())) {
			this.refreshMsg("\nSincronizando itens de segurança removidos na WEB...");
			if (!this.hasError("\nProblemas apagando itens de segurança na web:", this.itemProfile.apagarWeb())) {
				this.refreshMsg("\nSincronizando perfis removidos na WEB...");
				if (!this.hasError("\nProblemas apagando perfis na web:", this.profile.apagarWeb())) {
					this.refreshMsg("\nSincronizando designações removidos na WEB...");
					if (!this.hasError("\nProblemas apagando designações na web:", this.designacao.apagarWeb())) {
						this.refreshMsg("\nSincronizando ajudantes removidos na WEB...");
						if (!this.hasError("\nProblemas apagando ajudantes na web:", this.ajudante.apagarWeb())) {
							this.refreshMsg("\nSincronizando estudantes removidos na WEB...");
							if (!this.hasError("\nProblemas apagando estudantes na web:", this.estudante.apagarWeb())) {
								this.refreshMsg("\nSincronizando estudos removidos na WEB...");
								if (!this.hasError("\nProblemas apagando estudos na web:", this.estudo.apagarWeb())) {
									correto = true;
								}
							}
						}
					}
				}
			}
		}
		
		return correto;
	}
	
	private boolean enviarNovos() throws IOException {
		boolean correto = false;
		
		this.refreshMsg("\nEnviando novos cadastros de perfil...");
		if (!this.hasError("\nProblemas nos profiles enviados:", this.profile.enviarNovos())) {
			this.refreshMsg("\nEnviando novos cadastros de itens de segurança...");
			if (!this.hasError("\nProblemas nos itens de segurança enviados:", this.itemProfile.enviarNovos())) {
				this.refreshMsg("\nEnviando novos cadastros de usuario...");
				if (!this.hasError("\nProblemas nos usuarios enviados:", this.usuario.enviarNovos())) {
					this.refreshMsg("\nEnviando novos cadastros de estudo...");
					if (!this.hasError("\nProblemas nos estudos enviados:", this.estudo.enviar())) {
						this.refreshMsg("\nEnviando novos cadastros de estudante...");
						if (!this.hasError("\nProblemas nos estudantes enviados:", this.estudante.enviarNovos())) {
							this.refreshMsg("\nEnviando novos cadastros de ajudante...");
							if (!this.hasError("\nProblemas nos ajudantes enviados:", this.ajudante.enviarNovos())) {
								this.refreshMsg("\nEnviando novos cadastros de mês...");
								if (!this.hasError("\nProblemas nos meses enviados:", this.mes.enviarNovos())) {
									this.refreshMsg("\nEnviando novos cadastros de dia de reunião...");
									if (!this.hasError("\nProblemas nos dias de reunião enviados:", this.semana.enviarNovos())) {
										this.refreshMsg("\nEnviando novos cadastros de designação...");
										if (!this.hasError("\nProblemas nas designações enviadas:", this.designacao.enviarNovos())) {
											correto = true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		return correto;
	}
	
	private boolean atualizarWeb() throws IOException {
		boolean correto = false;

		this.refreshMsg("\nAtualizando cadastros de perfil...");
		if (!this.hasError("\nProblemas nos profiles atualizados:", this.profile.atualizarWeb())) {
			this.refreshMsg("\nAtualizando cadastros de usuario...");
			if (!this.hasError("\nProblemas nos usuarios atualizados:", this.usuario.atualizarWeb())) {
				this.refreshMsg("\nAtualizando cadastros de estudante...");
				if (!this.hasError("\nProblemas nos estudantes atualizados:", this.estudante.atualizarWeb())) {
					this.refreshMsg("\nAtualizando cadastros de ajudante...");
					if (!this.hasError("\nProblemas nos ajudantes atualizados:", this.ajudante.atualizarWeb())) {
						this.refreshMsg("\nAtualizando cadastros de mês...");
						if (!this.hasError("\nProblemas nos meses atualizados:", this.mes.atualizarWeb())) {
							this.refreshMsg("\nAtualizando cadastros de dia de reunião...");
							if (!this.hasError("\nProblemas nos dias de reunião atualizados:", this.semana.atualizarWeb())) {
								this.refreshMsg("\nAtualizando cadastros de designação...");
								if (!this.hasError("\nProblemas nas designações atualizadas:", this.designacao.atualizarWeb())) {
									correto = true;
								}
							}
						}
					}
				}
			}
		}
		
		return correto;
	}
	
	private boolean obterNovos() throws IOException {
		boolean correto = false;
		
		this.refreshMsg("\nObtendo novos cadastros de perfil...");
		if (!this.hasError("\nProblemas nos profiles recebidos:", this.profile.obterNovos())) {
			this.refreshMsg("\nObtendo novos cadastros de itens de segurança...");
			if (!this.hasError("\nProblemas nos itens de segurança recebidos:", this.itemProfile.obterNovos())) {
				this.refreshMsg("\nObtendo novos cadastros de usuario...");
				if (!this.hasError("\nProblemas nos usuarios recebidos:", this.usuario.obterNovos())) {
					this.refreshMsg("\nObtendo novos cadastros de estudo...");
					if (!this.hasError("\nProblemas nos estudos recebidos:", this.estudo.obterNovos())) {
						this.refreshMsg("\nObtendo novos cadastros de estudante...");
						if (!this.hasError("\nProblemas nos estudantes recebidos:", this.estudante.obterNovos())) {
							this.refreshMsg("\nObtendo novos cadastros de ajudante...");
							if (!this.hasError("\nProblemas nos ajudantes recebidos:", this.ajudante.obterNovos())) {
								this.refreshMsg("\nObtendo novos cadastros de mês...");
								if (!this.hasError("\nProblemas nos meses recebidos:", this.mes.obterNovos())) {
									this.refreshMsg("\nObtendo novos cadastros de dia de reunião...");
									if (!this.hasError("\nProblemas nos dias de reunião recebidos:", this.semana.obterNovos())) {
										this.refreshMsg("\nObtendo novos cadastros de designação...");
										if (!this.hasError("\nProblemas nas designações recebidas:", this.designacao.obterNovos())) {
											correto = true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		return correto;
	}
	
	private boolean hasError(String msg, String erroStr) {
		boolean temErro = erroStr != null && erroStr.length() > 0;
		
		if (temErro) {
			this.refreshMsg(msg + erroStr);
		}
	
		return temErro;
	}
	
	private void refreshMsg(String texto) {
		this.mensagensTela.append(texto);
		
		if (this.textArea != null) {
			this.textArea.setText(this.mensagensTela.toString());
		}
	}
}
