package br.com.dbcorp.escolaMinisterio.sincronismo;

import java.io.IOException;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.dbcorp.escolaMinisterio.dataBase.EstudanteGerenciador;
import br.com.dbcorp.escolaMinisterio.dataBase.SincGerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.Estudante;
import br.com.dbcorp.escolaMinisterio.entidades.Genero;
import br.com.dbcorp.escolaMinisterio.entidades.RemoveLog;
import br.com.dbcorp.escolaMinisterio.entidades.Sincronismo;
import br.com.dbcorp.escolaMinisterio.sincronismo.PHPConnection.HTTP_METHOD;
import br.com.dbcorp.escolaMinisterio.ui.Params;

public class EstudanteSinc {

	private final String url = "/service.php/estudante";
	
	private SincGerenciador gerenciador;
	private Sincronismo ultimaSincronia;
	private String hash;
	
	private List<Estudante> inseridos;
	private List<Estudante> atualizados;
	
	public EstudanteSinc(SincGerenciador gerenciador, Sincronismo ultimaSincronia, String hash) {
		this.ultimaSincronia = ultimaSincronia;
		this.gerenciador = gerenciador;
		this.hash = hash;
		
		this.inseridos = new ArrayList<Estudante>();
		this.atualizados = new ArrayList<Estudante>();
	}
	
	public String enviarNovos() throws IOException {
		List<Estudante> estudantes = this.gerenciador.obterEstudantesNovos();
		
		StringBuffer sb = new StringBuffer();
		
		for (Estudante estudante : estudantes) {
			PHPConnection con = new PHPConnection(this.url, HTTP_METHOD.POST, this.hash);
			
			con.setParameter("genero", estudante.getGenero().ordinal());
			con.setParameter("nome", estudante.getNome());
			con.setParameter("desabilitado", estudante.isDesabilitado() ? 1 : 0);
			con.setParameter("naoAjudante", estudante.isNaoAjudante() ? 1 : 0);
			
			if (estudante.getUltimaDesignacao() != null) {
				con.setParameter("ultimadesignacao", estudante.getUltimaDesignacao().format(Params.dateFormate()));
			
			} else {
				con.setParameter("ultimadesignacao", "");
			}
			
			if (estudante.getSalaUltimaDesignacao() != '\u0000') {
				con.setParameter("salaUltima", estudante.getSalaUltimaDesignacao());
				
			} else {
				con.setParameter("salaUltima", "");
			}
			
			if (estudante.getObservacao() == null || estudante.getObservacao().length() == 0) {
				con.setParameter("observacao", null);
				
			} else {
				con.setParameter("observacao", estudante.getObservacao());
			}
			
			con.connect();
			
			if (con.getResponseCode() != 200) {
				this.estudanteErro(sb, estudante, con.getErrorDetails());
			
			} else {
				JSONObject obj = con.getResponse();
				
				if (!"ERRO".equalsIgnoreCase(obj.getString("response"))) {
					estudante.setIdOnline(obj.getString("id_online"));
		
					this.gerenciador.atualizar(estudante);
		
					if (!"existente".equalsIgnoreCase(obj.getString("response"))) {
						this.inseridos.add(estudante);
					}
				} else {
					this.estudanteErro(sb, estudante, obj.getString("mensagem"));
				}
			}
		}
		
		return sb.toString();
	}
	
	public String atualizarWeb() throws IOException {
		List<Estudante> estudantes = this.gerenciador.obterEstudantesAtualizados(this.ultimaSincronia.getData());
		
		StringBuffer sb = new StringBuffer();
		
		for (Estudante estudante : estudantes) {
			if (!this.inseridos.contains(estudante)) {
				PHPConnection con = new PHPConnection(this.url, HTTP_METHOD.PUT, this.hash);
				
				con.setParameter("nome", estudante.getNome());
				con.setParameter("id_online", estudante.getIdOnline());
				con.setParameter("observacao", estudante.getObservacao());
				con.setParameter("desabilitado", estudante.isDesabilitado() ? 1 : 0);
				con.setParameter("naoAjudante", estudante.isNaoAjudante() ? 1 : 0);
	
				if (estudante.getUltimaDesignacao() != null) {
					con.setParameter("ultimadesignacao", estudante.getUltimaDesignacao().format(Params.dateFormate()));
				}
				
				if (estudante.getSalaUltimaDesignacao() != '\u0000') {
					con.setParameter("salaUltima", estudante.getSalaUltimaDesignacao());
					
				} else {
					con.setParameter("salaUltima", "");
				}
				
				con.connect();
				
				if (con.getResponseCode() != 200) {
					this.estudanteErro(sb, estudante, con.getErrorDetails());
				
				} else {
					JSONObject obj = con.getResponse();
					
					if ("ERRO".equalsIgnoreCase(obj.getString("response"))) {
						this.estudanteErro(sb, estudante, obj.getString("mensagem"));
					
					} else {
						this.atualizados.add(estudante);
					}
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
					
					Estudante estudante = this.gerenciador.obterEstudante(item.getString("id"));
					
					if (this.inseridos.contains(estudante) || this.atualizados.contains(estudante)) {
						continue;
					}
					
					estudante.setIdOnline(item.getString("id"));
					estudante.setNome(URLDecoder.decode(item.getString("nome"), "UTF-8"));
					estudante.setGenero(Genero.values()[item.getInt("genero")]);
					estudante.setObservacao(item.isNull("observacao") ? null : item.getString("observacao"));
					estudante.setDesabilitado(item.getInt("desabilitado") == 1);
					estudante.setNaoAjudante(item.getInt("naoajudante") == 1);
					
					if (item.getString("ultimadesignacao").length() > 0) {
						estudante.setUltimaDesignacao(LocalDate.parse(item.getString("ultimadesignacao"), Params.dateFormate()));
					}
					
					if (item.getString("salaultimadesignacao").length() > 0) {
						estudante.setSalaUltimaDesignacao(item.getString("salaultimadesignacao").charAt(0));
					}
					
					
					if (estudante.getId() == 0) {
						this.gerenciador.salvar(estudante);
						
					} else {
						this.gerenciador.atualizar(estudante);
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
		
		List<RemoveLog> logs = this.gerenciador.obterRemovidos(Estudante.class.getSimpleName());
		
		for (RemoveLog log : logs) {
			PHPConnection con = new PHPConnection(this.url, HTTP_METHOD.DELETE, this.hash);
			
			con.setParameter("id", log.getIdOnline());
			con.connect();
			
			if (con.getResponseCode() != 200) {
				sb.append("\nEstudante: ")
					.append(log.getIdOnline())
					.append(" Mensagem: ")
					.append(con.getErrorDetails());
			
			} else {
				JSONObject obj = con.getResponse();
				
				if ("ok".equalsIgnoreCase(obj.getString("response"))) {
					this.gerenciador.remover(log);
					
				} else {
					sb.append("\nEstudante: ")
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
				
				EstudanteGerenciador estGerenciador = new EstudanteGerenciador();
				
				for (int i = 0; i < array.length(); i++) {
					JSONObject item = array.getJSONObject(i);
					
					Estudante estudante = this.gerenciador.obterEstudante(item.getString("id"));
					
					if (estudante != null) {
						estGerenciador.remover(estudante);
					}
				}
			} else if ("ERRO".equalsIgnoreCase(obj.getString("response"))) {
				return "\n" + obj.getString("mensagem");
			}
		}
		
		return "";
	}
	
	private void estudanteErro(StringBuffer sb, Estudante estudante, String msg) {
		sb.append("\nEstudante: ")
			.append(estudante.getNome())
			.append(" Mensagem: ")
			.append(msg);
	}
}