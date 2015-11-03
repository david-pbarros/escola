package br.com.dbcorp.escolaMinisterio.sincronismo;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.dbcorp.escolaMinisterio.dataBase.EstudoGerenciador;
import br.com.dbcorp.escolaMinisterio.dataBase.SincGerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.Estudo;
import br.com.dbcorp.escolaMinisterio.entidades.RemoveLog;
import br.com.dbcorp.escolaMinisterio.entidades.Sincronismo;
import br.com.dbcorp.escolaMinisterio.sincronismo.PHPConnection.HTTP_METHOD;
import br.com.dbcorp.escolaMinisterio.ui.Params;

public class EstudoSinc {

	private final String url = "/service.php/estudo";
	
	private SincGerenciador gerenciador;
	private Sincronismo ultimaSincronia;
	private String hash;
	
	private List<Estudo> inseridos;
	
	public EstudoSinc(SincGerenciador gerenciador, Sincronismo ultimaSincronia, String hash) {
		this.ultimaSincronia = ultimaSincronia;
		this.gerenciador = gerenciador;
		this.hash = hash;
		
		this.inseridos = new ArrayList<Estudo>();
	}
	
	public String enviar() throws IOException {
		List<Estudo> estudos = this.gerenciador.obterEstudosNovos(ultimaSincronia.getData());
		
		StringBuffer sb = new StringBuffer();
		
		for (Estudo estudo : estudos) {
			PHPConnection con = new PHPConnection(this.url, HTTP_METHOD.POST, this.hash);
			
			con.setParameter("numero", estudo.getNrEstudo());
			con.setParameter("descricao", estudo.getDescricao());
			con.setParameter("leitura", estudo.isLeitura() ? 1 : 0 );
			con.setParameter("demonstracao", estudo.isDemonstracao() ? 1 : 0);
			con.setParameter("discurso", estudo.isDiscurso() ? 1 : 0);
			
			con.connect();
			
			if (con.getResponseCode() != 200) {
				this.estudoErro(sb, estudo, con.getErrorDetails());
			
			} else {
				JSONObject obj = con.getResponse();
				
				if ("ERRO".equalsIgnoreCase(obj.getString("response"))) {
					this.estudoErro(sb, estudo, obj.getString("mensagem"));
					
				} else if (!"existente".equalsIgnoreCase(obj.getString("response"))) {
					this.inseridos.add(estudo);
				}
			}
		}
		
		return sb.toString();
	}

	public String obterNovos() throws IOException, JSONException {
		PHPConnection con = new PHPConnection(this.url, HTTP_METHOD.GET, this.hash);
		
		con.setParameter("data_ultima", this.ultimaSincronia.getData().format(Params.dateTimeFormate()));
		con.connect();
		
		if (con.getResponseCode() != 200) {
			return "\n" + con.getErrorDetails();
		
		} else {
			JSONObject obj = con.getResponse();
			
			if ("ok".equalsIgnoreCase(obj.getString("response"))) {
				JSONArray array = obj.getJSONArray("itens");
				
				for (int i = 0; i < array.length(); i++) {
					JSONObject item = array.getJSONObject(i);
					
					Estudo estudo = this.gerenciador.obterEstudo(item.getInt("nrestudo"));
					
					if (this.inseridos.contains(estudo)) {
						continue;
					}
					
					estudo.setDescricao(URLDecoder.decode(item.getString("descricao"), "UTF-8"));
					estudo.setLeitura(item.getInt("leitura") == 1);
					estudo.setDemonstracao(item.getInt("demonstracao") == 1);
					estudo.setDiscurso(item.getInt("discurso") == 1);
					
					if (estudo.getNrEstudo() == 0) {
						estudo.setNrEstudo(item.getInt("nrestudo"));
						this.gerenciador.salvar(estudo);
						
					} else {
						this.gerenciador.atualizar(estudo);
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
		
		List<RemoveLog> logs = this.gerenciador.obterRemovidos(Estudo.class.getSimpleName());
		
		for (RemoveLog log : logs) {
			PHPConnection con = new PHPConnection(this.url, HTTP_METHOD.DELETE, this.hash);
			
			con.setParameter("id", log.getIdOnline());
			con.connect();
			
			if (con.getResponseCode() != 200) {
				sb.append("\nEstudo: ")
					.append(log.getIdOnline())
					.append(" Mensagem: ")
					.append(con.getErrorDetails());
			
			} else {
				JSONObject obj = con.getResponse();
				
				if ("ok".equalsIgnoreCase(obj.getString("response"))) {
					this.gerenciador.remover(log);
					
				} else {
					sb.append("\nEstudo: ")
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
				
				EstudoGerenciador estGerenciador = new EstudoGerenciador();
				
				for (int i = 0; i < array.length(); i++) {
					JSONObject item = array.getJSONObject(i);
					
					Estudo estudo = this.gerenciador.obterEstudo(item.getInt("nrestudo"));
					
					if (estudo != null) {
						estGerenciador.remover(estudo);
					}
				}
			} else if ("ERRO".equalsIgnoreCase(obj.getString("response"))) {
				return "\n" + obj.getString("mensagem");
			}
		}
		
		return "";
	}
	
	private void estudoErro(StringBuffer sb, Estudo estudo, String msg) {
		sb.append("\nEstudo: ")
			.append(estudo.getNrEstudo())
			.append(" Mensagem: ")
			.append(msg);
	}
}