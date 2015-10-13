package br.com.dbcorp.escolaMinisterio.sincronismo;

import java.io.IOException;
import java.io.StringReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.time.LocalDateTime;
import java.util.Base64;

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
			
			String erro = null;
			
			this.refreshMsg("\nObtendo novos perfis...");
			if (!this.hasError(erro = this.profile.obterNovos())) {
				this.refreshMsg("\nObtendo novos itens de segurança...");
				if (!this.hasError(erro = this.itemProfile.obterNovos())) {
					this.refreshMsg("\nObtendo novos usuarios...");
					erro = this.usuario.obterNovos();
				}
			}
			
			this.mensagensTela.append(erro);
			
			this.refreshMsg("\nFim do sincronismo. Reinicie o sistema.");
			
		} catch (Exception ex) {
			String erro = "Erro inesperado durante o sincronismo de informações.";
			this.log.error(erro, ex);
			
			this.refreshMsg("\n" + erro+ " Consultar Log.");
			
			sinc.setSucesso(false);
		}
		
		sinc.setData(LocalDateTime.now());
		this.gerenciador.salvar(sinc);
	}
	
	public void sincronizar() {
		boolean hasErro = false;
		
		Sincronismo sinc = new Sincronismo();
		
		try {
			this.gerenciador.limparDesignacoesNaoVinculadas();
			this.gerenciador.limparSemEstudo();
			this.gerenciador.limpaIndevidos();
			this.gerenciador.limparDuplicados();
			
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
			
			hasErro = true;
			
			if (!this.hasError(this.apagarLocal())) {
				if (!this.hasError(this.apagaWeb())) {
					if (!this.hasError(this.sicronizarProfile())) {
						if (!this.hasError(this.sincronizarItemProfile())) {
							if (!this.hasError(this.sincronizarUsuario())) {
								if (!this.hasError(this.sincronizarEstudo())) {
									if (!this.hasError(this.sicronizarEstudante())) {
										if (!this.hasError(this.sicronizarAjudante())) {
											if (!this.hasError(this.sincronizaMes())) {
												if (!this.hasError(this.sincronizaSemana())) {
													this.sicronizarDesignacao();
													
													hasErro = false;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			
			this.mensagensTela.append(this.versao());
			
			this.refreshMsg("\nFim do sincronismo.");
			
			if (this.ultimaSincronia.isCriado()) {
				this.refreshMsg(" Reinicie o sistema.");
			}
			
		} catch (Exception ex) {
			String erro = "Erro inesperado durante o sincronismo de informações.";
			this.log.error(erro, ex);
			
			this.refreshMsg("\n" + erro + " Consultar Log.");
			
			hasErro = true;
		}
		
		sinc.setData(LocalDateTime.now());
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
		StringBuffer hash = new StringBuffer(Params.propriedades().getProperty("congregacao")).append(";")
			.append(this.gerenciador.getUser().getNome()).append(";")
			.append(this.gerenciador.getUser().getSenha()).append(";")
			.append(Params.propriedades().get("verionNumber"));
		
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
		cipher.init(Cipher.ENCRYPT_MODE, this.chave);
		
		byte[] ciphered = cipher.doFinal(hash.toString().getBytes());
		
		this.hash = Base64.getEncoder().encodeToString(ciphered);
	}
	
	private String apagarLocal() throws JSONException, IOException {
		String erro = null;
		
		this.refreshMsg("\nSincronizando usuários removidos...");
		if (this.hasError(erro = this.usuario.apagar())) {
			this.refreshMsg("\nProblemas apagando usuarios:" + erro);
		
		} else {
			this.refreshMsg("\nSincronizando itens de segurança removidos...");
			if (this.hasError(erro = this.itemProfile.apagar())) {
				this.refreshMsg("\nProblemas apagando itens de segurança:" + erro);
			
			} else {
				this.refreshMsg("\nSincronizando perfis removidos...");
				if (this.hasError(erro = this.profile.apagar())) {
					this.refreshMsg("\nProblemas apagando perfis:" + erro);
					
				} else {
					this.refreshMsg("\nSincronizando designações removidos...");
					if (this.hasError(erro = this.designacao.apagar())) {
						this.refreshMsg("\nProblemas apagando designações:" + erro);
						
					} else {
						this.refreshMsg("\nSincronizando ajudantes removidos...");
						if (this.hasError(erro = this.ajudante.apagar())) {
							this.refreshMsg("\nProblemas apagando ajudantes:" + erro);
							
						} else {
							this.refreshMsg("\nSincronizando estudantes removidos...");
							if (this.hasError(erro = this.estudante.apagar())) {
								this.refreshMsg("\nProblemas apagando estudantes:" + erro);
								
							} else {
								this.refreshMsg("\nSincronizando estudos removidos...");
								if (this.hasError(erro = this.estudo.apagar())) {
									this.refreshMsg("\nProblemas apagando estudos:" + estudo);
								}
							}
						}
					}
				}
			}
		}
		
		return erro;
	}
	
	private String apagaWeb() throws IOException {
		String erro = null;
		
		this.refreshMsg("\nSincronizando usuários removidos na WEB...");
		if (this.hasError(erro = this.usuario.apagarWeb())) {
			this.refreshMsg("\nProblemas apagando usuarios na web:" + erro);
			
		} else {
			this.refreshMsg("\nSincronizando itens de segurança removidos na WEB...");
			if (this.hasError(erro = this.itemProfile.apagarWeb())) {
				this.refreshMsg("\nProblemas apagando itens de segurança na web:" + erro);
				
			} else {
				this.refreshMsg("\nSincronizando perfis removidos na WEB...");
				if (this.hasError(erro = this.profile.apagarWeb())) {
					this.refreshMsg("\nProblemas apagando perfis na web:" + erro);
				
				} else {
					this.refreshMsg("\nSincronizando designações removidos na WEB...");
					if (this.hasError(erro = this.designacao.apagarWeb())) {
						this.refreshMsg("\nProblemas apagando designações na web:" + erro);
						
					} else {
						this.refreshMsg("\nSincronizando ajudantes removidos na WEB...");
						if (this.hasError(erro = this.ajudante.apagarWeb())) {
							this.refreshMsg("\nProblemas apagando ajudantes na web:" + erro);
						
						} else {
							this.refreshMsg("\nSincronizando estudantes removidos na WEB...");
							if (this.hasError(erro = this.estudante.apagarWeb())) {
								this.refreshMsg("\nProblemas apagando estudantes na web:" + erro);
								
							} else {
								this.refreshMsg("\nSincronizando estudos removidos na WEB...");
								if (this.hasError(erro = this.estudo.apagarWeb())) {
									this.refreshMsg("\nProblemas apagando estudos na web:" + erro);
								}
							}
						}
					}
				}
			}
		}
		
		return erro;
	}
	
	private String sincronizaMes() throws IOException {
		String erro = null;
		
		this.refreshMsg("\nEnviando novos cadastros de mês...");
		if (this.hasError(erro = this.mes.enviarNovos())) {
			this.refreshMsg("\nProblemas nos meses enviados:" + erro);
			
		} else {
			this.refreshMsg("\nAtualizando cadastros de mês...");
			if (this.hasError(erro = this.mes.atualizarWeb())) {
				this.refreshMsg("\nProblemas nos meses atualizados:" + erro);
				
			} else {
				this.refreshMsg("\nObtendo novos cadastros de mês...");
				if (this.hasError(erro = this.mes.obterNovos())) {
					this.refreshMsg("\nProblemas nos meses recebidos:" + erro);
				}
			}
		}
		
		return erro;
	}
	
	private String sicronizarAjudante() throws IOException {
		String erro = null;
		
		this.refreshMsg("\nEnviando novos cadastros de ajudante...");
		if (this.hasError(erro = this.ajudante.enviarNovos())) {
			this.refreshMsg("\nProblemas nos ajudantes enviados:" + erro);
			
		} else {
			this.refreshMsg("\nAtualizando cadastros de ajudante...");
			if (this.hasError(erro = this.ajudante.atualizarWeb())) {
				this.refreshMsg("\nProblemas nos ajudantes atualizados:" + erro);
				
			} else {
				this.refreshMsg("\nObtendo novos cadastros de ajudante...");
				if (this.hasError(erro = this.ajudante.obterNovos())) {
					this.refreshMsg("\nProblemas nos ajudantes recebidos:" + erro);
				}
			}
		}

		return erro;
	}
	
	private String sicronizarEstudante() throws IOException {
		String erro = null;
		
		this.refreshMsg("\nEnviando novos cadastros de estudante...");
		if (this.hasError(erro = this.estudante.enviarNovos())) {
			this.refreshMsg("\nProblemas nos estudantes enviados:" + erro);
			
		} else {
			this.refreshMsg("\nAtualizando cadastros de estudante...");
			if (this.hasError(erro = this.estudante.atualizarWeb())) {
				this.refreshMsg("\nProblemas nos estudantes atualizados:" + erro);
				
			} else {
				this.refreshMsg("\nObtendo novos cadastros de estudante...");
				if (this.hasError(erro = this.estudante.obterNovos())) {
					this.refreshMsg("\nProblemas nos estudantes recebidos:" + erro);
				}
			}
		}
		
		return erro;
	}
	
	private String sincronizarEstudo() throws IOException {
		String erro = null;
		
		this.refreshMsg("\nEnviando novos cadastros de estudo...");
		if (this.hasError(erro = this.estudo.enviar())) {
			this.refreshMsg("\nProblemas nos estudos enviados:" + erro);
			
		} else {
			this.refreshMsg("\nObtendo novos cadastros de estudo...");
			if (this.hasError(erro = this.estudo.obterNovos())) {
				this.refreshMsg("\nProblemas nos estudos recebidos:" + erro);
			}
		}
		
		return erro;
	}
	
	private String sicronizarProfile() throws IOException {
		String erro = null;
		
		this.refreshMsg("\nEnviando novos cadastros de perfil...");
		if (this.hasError(erro = this.profile.enviarNovos())) {
			this.refreshMsg("\nProblemas nos profiles enviados:" + erro);
			
		} else {
			this.refreshMsg("\nAtualizando cadastros de perfil...");
			if (this.hasError(erro = this.profile.atualizarWeb())) {
				this.refreshMsg("\nProblemas nos profiles atualizados:" + erro);
				
			} else {
				this.refreshMsg("\nObtendo novos cadastros de perfil...");
				if (this.hasError(erro = this.profile.obterNovos())) {
					this.refreshMsg("\nProblemas nos profiles recebidos:" + erro);
				}
			}
		}
		
		return erro;
	}
	
	private String sincronizarItemProfile() throws IOException {
		String erro = null;
		
		this.refreshMsg("\nEnviando novos cadastros de itens de segurança...");
		if (this.hasError(erro = this.itemProfile.enviarNovos())) {
			this.refreshMsg("\nProblemas nos itens de segurança enviados:" + erro);
			
		} else {
			this.refreshMsg("\nObtendo novos cadastros de itens de segurança...");
			if (this.hasError(erro = this.itemProfile.obterNovos())) {
				this.refreshMsg("\nProblemas nos itens de segurança recebidos:" + erro);
			}
		}
		
		return erro;
	}
	
	private String sincronizarUsuario() throws IOException {
		String erro = null;
		
		this.refreshMsg("\nEnviando novos cadastros de usuario...");
		if (this.hasError(erro = this.usuario.enviarNovos())) {
			this.refreshMsg("\nProblemas nos usuarios enviados:" + erro);
			
		} else {
			this.refreshMsg("\nAtualizando cadastros de usuario...");
			if (this.hasError(erro = this.usuario.atualizarWeb())) {
				this.refreshMsg("\nProblemas nos usuarios atualizados:" + erro);
				
			} else {
				this.refreshMsg("\nObtendo novos cadastros de usuario...");
				if (this.hasError(erro = this.usuario.obterNovos())) {
					this.refreshMsg("\nProblemas nos usuarios recebidos:" + erro);
				}
			}
		}
		
		return erro;
	}
	
	private String sincronizaSemana() throws IOException {
		String erro = null;
		
		this.refreshMsg("\nEnviando novos cadastros de dia de reunião...");
		if (this.hasError(erro = this.semana.enviarNovos())) {
			this.refreshMsg("\nProblemas nos dias de reunião enviados:" + erro);
			
		} else {
			this.refreshMsg("\nAtualizando cadastros de dia de reunião...");
			if (this.hasError(erro = this.semana.atualizarWeb())) {
				this.refreshMsg("\nProblemas nos dias de reunião atualizados:" + erro);
				
			} else {
				this.refreshMsg("\nObtendo novos cadastros de dia de reunião...");
				if (this.hasError(erro = this.semana.obterNovos())) {
					this.refreshMsg("\nProblemas nos dias de reunião recebidos:" + erro);
				}
			}
		}
		
		return erro;
	}
	
	private String sicronizarDesignacao() throws IOException {
		String erro = null;
		
		this.refreshMsg("\nEnviando novos cadastros de designação...");
		if (this.hasError(erro = this.designacao.enviarNovos())) {
			this.refreshMsg("\nProblemas nas designações enviadas:" + erro);
			
		} else {
			this.refreshMsg("\nAtualizando cadastros de designação...");
			if (this.hasError(erro = this.designacao.atualizarWeb())) {
				this.refreshMsg("\nProblemas nas designações atualizadas:" + erro);
				
			} else {
				this.refreshMsg("\nObtendo novos cadastros de designação...");
				if (this.hasError(erro = this.designacao.obterNovos())) {
					this.refreshMsg("\nProblemas nas designações recebidas:" + erro);
				}
			}
		}
		
		return erro;
	}
	
	private boolean hasError(String erroStr) {
		return erroStr != null && erroStr.length() > 0;
	}
	
	private void refreshMsg(String texto) {
		this.mensagensTela.append(texto);
		
		if (this.textArea != null) {
			this.textArea.setText(this.mensagensTela.toString());
		}
	}
}
