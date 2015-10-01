package br.com.dbcorp.escolaMinisterio.sincronismo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.dbcorp.escolaMinisterio.dataBase.SincGerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.Profile;
import br.com.dbcorp.escolaMinisterio.entidades.RemoveLog;
import br.com.dbcorp.escolaMinisterio.entidades.Sincronismo;
import br.com.dbcorp.escolaMinisterio.sincronismo.PHPConnection.HTTP_METHOD;

public class ProfileSinc {

	private final String url = "/service.php/profile";
	
	private SincGerenciador gerenciador;
	private Sincronismo ultimaSincronia;
	private String hash;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	private List<Profile> inseridos;
	private List<Profile> atualizados;
	
	public ProfileSinc(SincGerenciador gerenciador, Sincronismo ultimaSincronia, String hash) {
		this.ultimaSincronia = ultimaSincronia;
		this.gerenciador = gerenciador;
		this.hash = hash;
		
		this.inseridos = new ArrayList<Profile>();
		this.atualizados = new ArrayList<Profile>();
	}
	
	public String enviarNovos() throws IOException {
		List<Profile> profiles = this.gerenciador.obterProfilesNovos();
		
		StringBuffer sb = new StringBuffer();
		
		for (Profile profile : profiles) {
			PHPConnection con = new PHPConnection(this.url, HTTP_METHOD.POST, this.hash);
			
			con.setParameter("nome", profile.getNome());
			con.connect();
			
			if (con.getResponseCode() != 200) {
				this.profileErro(sb, profile, con.getErrorDetails());
			
			} else {
				JSONObject obj = con.getResponse();
				
				if (!"ERRO".equalsIgnoreCase(obj.getString("response"))) {
					profile.setIdOnline(obj.getString("id_online"));
		
					this.gerenciador.atualizar(profile);
		
					if (!"existente".equalsIgnoreCase(obj.getString("response"))) {
						this.inseridos.add(profile);
					}
				} else {
					this.profileErro(sb, profile, obj.getString("mensagem"));
				}
			}
		}
		
		return sb.toString();
	}
	
	public String atualizarWeb() throws IOException {
		List<Profile> profiles = this.gerenciador.obterProfilesAtualizados(this.ultimaSincronia.getData());
		
		StringBuffer sb = new StringBuffer();
		
		for (Profile profile : profiles) {
			if (!this.inseridos.contains(profile)) {
				PHPConnection con = new PHPConnection(this.url, HTTP_METHOD.PUT, this.hash);
				
				con.setParameter("nome", profile.getNome());
				con.setParameter("id_online", profile.getIdOnline());
	
				con.connect();
				
				if (con.getResponseCode() != 200) {
					this.profileErro(sb, profile, con.getErrorDetails());
				
				} else {
					JSONObject obj = con.getResponse();
					
					if ("ERRO".equalsIgnoreCase(obj.getString("response"))) {
						this.profileErro(sb, profile, obj.getString("mensagem"));
					
					} else {
						this.atualizados.add(profile);
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
					
					Profile profile = this.gerenciador.obterProfile(item.getString("id"));
					
					if (this.inseridos.contains(profile) || this.atualizados.contains(profile)) {
						continue;
					}
					
					profile.setIdOnline(item.getString("id"));
					profile.setNome(item.getString("nome"));
					
					if (profile.getId() == 0) {
						this.gerenciador.salvar(profile);
						
					} else {
						this.gerenciador.atualizar(profile);
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
		
		List<RemoveLog> logs = this.gerenciador.obterRemovidos(Profile.class.getSimpleName());
		
		for (RemoveLog log : logs) {
			PHPConnection con = new PHPConnection(this.url, HTTP_METHOD.DELETE, this.hash);
			
			con.setParameter("id", log.getIdOnline());
			con.connect();
			
			if (con.getResponseCode() != 200) {
				sb.append("\nProfile: ")
					.append(log.getIdOnline())
					.append(" Mensagem: ")
					.append(con.getErrorDetails());
			
			} else {
				JSONObject obj = con.getResponse();
				
				if ("ok".equalsIgnoreCase(obj.getString("response"))) {
					this.gerenciador.remover(log);
					
				} else {
					sb.append("\nProfile: ")
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
					
					Profile profile = this.gerenciador.obterProfile(item.getString("id"));
					
					if (profile != null) {
						this.gerenciador.remover(profile);
					}
				}
			} else if ("ERRO".equalsIgnoreCase(obj.getString("response"))) {
				return "\n" + obj.getString("mensagem");
			}
		}
		
		return "";
	}
	
	private void profileErro(StringBuffer sb, Profile profile, String msg) {
		sb.append("\nProfile: ")
			.append(profile.getNome())
			.append(" Mensagem: ")
			.append(msg);
	}
}