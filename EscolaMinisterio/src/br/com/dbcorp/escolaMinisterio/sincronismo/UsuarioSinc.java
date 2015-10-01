package br.com.dbcorp.escolaMinisterio.sincronismo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.dbcorp.escolaMinisterio.dataBase.SincGerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.RemoveLog;
import br.com.dbcorp.escolaMinisterio.entidades.Sincronismo;
import br.com.dbcorp.escolaMinisterio.entidades.Usuario;
import br.com.dbcorp.escolaMinisterio.sincronismo.PHPConnection.HTTP_METHOD;

public class UsuarioSinc {

	private final String url = "/service.php/usuario";
	
	private SincGerenciador gerenciador;
	private Sincronismo ultimaSincronia;
	private String hash;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	private List<Usuario> inseridos;
	private List<Usuario> atualizados;
	
	
	public UsuarioSinc(SincGerenciador gerenciador, Sincronismo ultimaSincronia, String hash) {
		this.ultimaSincronia = ultimaSincronia;
		this.gerenciador = gerenciador;
		this.hash = hash;
		
		this.inseridos = new ArrayList<Usuario>();
		this.atualizados = new ArrayList<Usuario>();
	}
	
	public String enviarNovos() throws IOException {
		List<Usuario> estudantes = this.gerenciador.obterUsuariosNovos();
		
		StringBuffer sb = new StringBuffer();
		
		for (Usuario usuario : estudantes) {
			PHPConnection con = new PHPConnection(this.url, HTTP_METHOD.POST, this.hash);
			
			con.setParameter("senha", usuario.getSenha());
			con.setParameter("nome", usuario.getNome());
			con.setParameter("reiniciaSenha", usuario.isReiniciaSenha() ? 1 : 0);
			con.setParameter("bloqueado", usuario.isBloqueado() ? 1 : 0);
			con.setParameter("profile", usuario.getProfile().getIdOnline());
			
			con.connect();
			
			if (con.getResponseCode() != 200) {
				this.erroUsuario(sb, usuario, con.getErrorDetails());
				
			} else {
				JSONObject obj = con.getResponse();
				
				if (!"ERRO".equalsIgnoreCase(obj.getString("response"))) {
					usuario.setIdOnline(obj.getString("id_online"));
		
					this.gerenciador.atualizar(usuario);
		
					if (!"existente".equalsIgnoreCase(obj.getString("response"))) {
						this.inseridos.add(usuario);
					}
				} else {
					this.erroUsuario(sb, usuario, obj.getString("mensagem"));
				}
			}
		}
		
		return sb.toString();
	}
	
	public String atualizarWeb() throws IOException {
		List<Usuario> usuarios = this.gerenciador.obterUsuariosAtualizados(this.ultimaSincronia.getData());
		
		StringBuffer sb = new StringBuffer();
		
		for (Usuario usuario : usuarios) {
			if (!this.inseridos.contains(usuario)) {
				PHPConnection con = new PHPConnection(this.url, HTTP_METHOD.PUT, this.hash);
				
				con.setParameter("id_online", usuario.getIdOnline());
				con.setParameter("senha", usuario.getSenha());
				con.setParameter("nome", usuario.getNome());
				con.setParameter("reiniciaSenha", usuario.isReiniciaSenha() ? 1 : 0);
				con.setParameter("bloqueado", usuario.isBloqueado() ? 1 : 0);
				con.setParameter("profile", usuario.getProfile().getIdOnline());
				
				con.connect();
				
				if (con.getResponseCode() != 200) {
					this.erroUsuario(sb, usuario, con.getErrorDetails());
				
				} else {
					JSONObject obj = con.getResponse();
					
					if ("ERRO".equalsIgnoreCase(obj.getString("response"))) {
						this.erroUsuario(sb, usuario, obj.getString("mensagem"));
					
					} else {
						this.atualizados.add(usuario);
					}
				}
			}
		}
		
		return sb.toString();
	}

	public String obterNovos() throws IOException, JSONException {
		PHPConnection con = new PHPConnection(this.url, HTTP_METHOD.GET, this.hash);
		
		con.setParameter("data_ultima", this.sdf.format(this.ultimaSincronia.getData()));
		con.connect();
		
		if (con.getResponseCode() != 200) {
			return "\n" + con.getErrorDetails();
		
		} else {
			JSONObject obj = con.getResponse();
			
			if ("ok".equalsIgnoreCase(obj.getString("response"))) {
				JSONArray array = obj.getJSONArray("itens");
				
				for (int i = 0; i < array.length(); i++) {
					JSONObject item = array.getJSONObject(i);
					
					Usuario usuario = this.gerenciador.obterUsuario(item.getString("id"));
					
					if (this.inseridos.contains(usuario) || this.atualizados.contains(usuario)) {
						continue;
					}
					
					usuario.setIdOnline(item.getString("id"));
					usuario.setSenha(item.getString("senha"));
					usuario.setNome(item.getString("nome"));
					usuario.setReiniciaSenha(item.getInt("reiniciaSenha") == 1);
					usuario.setBloqueado(item.getInt("bloqueado") == 1);
					usuario.setProfile(this.gerenciador.obterProfile(item.getString("profile_id")));
					
					if (usuario.getId() == 0) {
						this.gerenciador.salvar(usuario);
						
					} else {
						this.gerenciador.atualizar(usuario);
					}
				}
			} else if ("ERRO".equalsIgnoreCase(obj.getString("response"))) {
				return "\n" + obj.getString("mensagem");
			}
		}

		return "";
	}
	
	public String apagarWeb() throws IOException {
		StringBuffer sb = new StringBuffer();
		
		List<RemoveLog> logs = this.gerenciador.obterRemovidos(Usuario.class.getSimpleName());
		
		for (RemoveLog log : logs) {
			PHPConnection con = new PHPConnection(this.url, HTTP_METHOD.DELETE, this.hash);
			
			con.setParameter("id", log.getIdOnline());
			con.connect();
			
			if (con.getResponseCode() != 200) {
				sb.append("\nUsuario: ")
					.append(log.getIdOnline())
					.append(" Mensagem: ")
					.append(con.getErrorDetails());
			
			} else {
				JSONObject obj = con.getResponse();
				
				if ("ok".equalsIgnoreCase(obj.getString("response"))) {
					this.gerenciador.remover(log);
					
				} else {
					sb.append("\nUsuario: ")
						.append(log.getIdOnline())
						.append(" Mensagem: ")
						.append(obj.getString("mensagem"));
				}
			}
		}
		
		return sb.toString();
	}
	
	public String apagar() throws IOException, JSONException {
		PHPConnection con = new PHPConnection(this.url + "/removidos", HTTP_METHOD.GET, this.hash);
		con.connect();
		
		if (con.getResponseCode() != 200) {
			return "\n" + con.getErrorDetails();
		
		} else {
			JSONObject obj = con.getResponse();
			
			if ("ok".equalsIgnoreCase(obj.getString("response"))) {
				JSONArray array = obj.getJSONArray("itens");
				
				for (int i = 0; i < array.length(); i++) {
					JSONObject item = array.getJSONObject(i);
					
					Usuario usuario = this.gerenciador.obterUsuario(item.getString("id"));
					
					if (usuario != null) {
						this.gerenciador.remover(usuario);
					}
				}
			} else if ("ERRO".equalsIgnoreCase(obj.getString("response"))) {
				return "\n" + obj.getString("mensagem");
			}
		}
		
		return "";
	}
	
	private void erroUsuario(StringBuffer sb, Usuario usuario, String msg) {
		sb.append("\nUsuario: ")
			.append(usuario.getNome())
			.append(" Mensagem: ")
			.append(msg);
	}
}