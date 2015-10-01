package br.com.dbcorp.escolaMinisterio.sincronismo;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.dbcorp.escolaMinisterio.dataBase.SincGerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.Ajudante;
import br.com.dbcorp.escolaMinisterio.entidades.Genero;
import br.com.dbcorp.escolaMinisterio.entidades.RemoveLog;
import br.com.dbcorp.escolaMinisterio.entidades.Sincronismo;
import br.com.dbcorp.escolaMinisterio.sincronismo.PHPConnection.HTTP_METHOD;

public class AjudanteSinc {

	private final String url = "/service.php/ajudante";
	
	private SincGerenciador gerenciador;
	private Sincronismo ultimaSincronia;
	private String hash;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	private List<Ajudante> inseridos;
	private List<Ajudante> atualizados;
	
	public AjudanteSinc(SincGerenciador gerenciador, Sincronismo ultimaSincronia, String hash) {
		this.ultimaSincronia = ultimaSincronia;
		this.gerenciador = gerenciador;
		this.hash = hash;
		
		this.inseridos = new ArrayList<Ajudante>();
		this.atualizados = new ArrayList<Ajudante>();
	}
	
	public String enviarNovos() throws IOException {
		List<Ajudante> ajudantes = this.gerenciador.obterAjudantesNovos();
		
		StringBuffer sb = new StringBuffer();
		
		for (Ajudante ajudante : ajudantes) {
			PHPConnection con = new PHPConnection(this.url, HTTP_METHOD.POST, this.hash);
			
			con.setParameter("genero", ajudante.getGenero().ordinal());
			con.setParameter("nome", ajudante.getNome());
			
			if (ajudante.getUltimaDesignacao() != null) {
				con.setParameter("ultimadesignacao", sdf.format(ajudante.getUltimaDesignacao()));
			
			} else {
				con.setParameter("ultimadesignacao", "");
			}
			
			con.connect();
			
			if (con.getResponseCode() != 200) {
				this.erroAjudante(sb, ajudante, con.getErrorDetails());
			
			} else {
				JSONObject obj = con.getResponse();
				
				if (!"ERRO".equalsIgnoreCase(obj.getString("response"))) {
					ajudante.setIdOnline(obj.getString("id_online"));
		
					this.gerenciador.atualizar(ajudante);
		
					if (!"existente".equalsIgnoreCase(obj.getString("response"))) {
						this.inseridos.add(ajudante);
					}
				} else {
					this.erroAjudante(sb, ajudante, obj.getString("mensagem"));
				}
			}
		}
		
		return sb.toString();
	}
	
	public String atualizarWeb() throws IOException {
		List<Ajudante> ajudantes = this.gerenciador.obterAjudantesAtualizados(this.ultimaSincronia.getData());
		
		StringBuffer sb = new StringBuffer();
		
		for (Ajudante ajudante : ajudantes) {
			if (!this.inseridos.contains(ajudante)) {
				PHPConnection con = new PHPConnection(this.url, HTTP_METHOD.PUT, this.hash);
				
				con.setParameter("nome", ajudante.getNome());
				con.setParameter("id_online", ajudante.getIdOnline());
	
				if (ajudante.getUltimaDesignacao() != null) {
					con.setParameter("ultimadesignacao", sdf.format(ajudante.getUltimaDesignacao()));
				}
				
				con.connect();
				
				if (con.getResponseCode() != 200) {
					this.erroAjudante(sb, ajudante, con.getErrorDetails());
				
				} else {
					JSONObject obj = con.getResponse();
					
					if ("ERRO".equalsIgnoreCase(obj.getString("response"))) {
						this.erroAjudante(sb, ajudante, obj.getString("mensagem"));
					
					} else {
						this.atualizados.add(ajudante);
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
					
					Ajudante ajudante = this.gerenciador.obterAjudante(item.getString("id"));
					
					if (this.inseridos.contains(ajudante) || this.atualizados.contains(ajudante)) {
						continue;
					}
					
					ajudante.setIdOnline(item.getString("id"));
					ajudante.setNome(item.getString("nome"));
					ajudante.setGenero(Genero.values()[item.getInt("genero")]);
					
	
					if (item.getString("ultimadesignacao").length() > 0) {
						try {
							ajudante.setUltimaDesignacao(sdf.parse(item.getString("ultimadesignacao")));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					
					if (ajudante.getId() == 0) {
						this.gerenciador.salvar(ajudante);
						
					} else {
						this.gerenciador.atualizar(ajudante);
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
		
		List<RemoveLog> logs = this.gerenciador.obterRemovidos(Ajudante.class.getSimpleName());
		
		for (RemoveLog log : logs) {
			PHPConnection con = new PHPConnection(this.url, HTTP_METHOD.DELETE, this.hash);
			
			con.setParameter("id", log.getIdOnline());
			con.connect();
			
			if (con.getResponseCode() != 200) {
				sb.append("\nAjudante: ")
					.append(log.getIdOnline())
					.append(" Mensagem: ")
					.append(con.getErrorDetails());
			
			} else {
				JSONObject obj = con.getResponse();
				
				if ("ok".equalsIgnoreCase(obj.getString("response"))) {
					this.gerenciador.remover(log);
					
				} else {
					sb.append("\nAjudante: ")
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
					
					Ajudante ajudante = this.gerenciador.obterAjudante(item.getString("id"));
					
					if (ajudante != null) {
						this.gerenciador.remover(ajudante);;
					}
				}
			} else if ("ERRO".equalsIgnoreCase(obj.getString("response"))) {
				return "\n" + obj.getString("mensagem");
			}
		}
		
		return "";
	}
	
	private void erroAjudante(StringBuffer sb, Ajudante ajudante, String msg) {
		sb.append("\nAjudante: ")
			.append(ajudante.getNome())
			.append(" Mensagem: ")
			.append(msg);
	}
}