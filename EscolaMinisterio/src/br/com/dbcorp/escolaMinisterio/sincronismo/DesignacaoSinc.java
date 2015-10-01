package br.com.dbcorp.escolaMinisterio.sincronismo;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.dbcorp.escolaMinisterio.Log;
import br.com.dbcorp.escolaMinisterio.dataBase.SincGerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.Designacao;
import br.com.dbcorp.escolaMinisterio.entidades.RemoveLog;
import br.com.dbcorp.escolaMinisterio.entidades.Sincronismo;
import br.com.dbcorp.escolaMinisterio.sincronismo.PHPConnection.HTTP_METHOD;

public class DesignacaoSinc {

	private final String url = "/service.php/designacao";
	
	private SincGerenciador gerenciador;
	private Sincronismo ultimaSincronia;
	private String hash;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	
	private List<Designacao> inseridos;
	private List<Designacao> atualizados;
	
	public DesignacaoSinc(SincGerenciador gerenciador, Sincronismo ultimaSincronia, String hash) {
		this.ultimaSincronia = ultimaSincronia;
		this.gerenciador = gerenciador;
		this.hash = hash;
		
		this.inseridos = new ArrayList<Designacao>();
		this.atualizados = new ArrayList<Designacao>();
	}
	
	public String enviarNovos() throws IOException {
		List<Designacao> designacoes = this.gerenciador.obterDesignacoesNovas();
		
		StringBuffer sb = new StringBuffer();
		
		for (Designacao designacao : designacoes) {
			PHPConnection con = new PHPConnection(this.url, HTTP_METHOD.POST, this.hash);
			
			con.setParameter("estudo", designacao.getEstudo().getNrEstudo());
			con.setParameter("numero", designacao.getNumero());
			con.setParameter("sala", designacao.getSala());
			con.setParameter("status", designacao.getStatus());
			con.setParameter("tema", designacao.getTema());
			con.setParameter("fonte", designacao.getFonte());
			con.setParameter("data", this.sdf.format(designacao.getData()));
			con.setParameter("estudante", designacao.getEstudante().getIdOnline());
			con.setParameter("semana", designacao.getSemana().getIdOnline());
			
			if (designacao.getAjudante() != null) {
				con.setParameter("ajudante", designacao.getAjudante().getIdOnline());
				
			} else {
				con.setParameter("ajudante", null);
			}
			
			if (designacao.getObservacao() == null || designacao.getObservacao().length() == 0) {
				con.setParameter("observacao", null);
				
			} else {
				con.setParameter("observacao", designacao.getObservacao());
			}
			
			if (designacao.getObsFolha() == null || designacao.getObsFolha().length() == 0) {
				con.setParameter("ObsFolha", null);
				
			} else {
				con.setParameter("ObsFolha", designacao.getObsFolha());
			}
			
			con.connect();
			
			if (con.getResponseCode() != 200) {
				this.designacaoErro(sb, designacao, con.getErrorDetails());
			
			} else {
				JSONObject obj = con.getResponse();
				
				if (!"ERRO".equalsIgnoreCase(obj.getString("response"))) {
					designacao.setIdOnline(obj.getString("id_online"));
		
					this.gerenciador.atualizar(designacao);
		
					if (!"existente".equalsIgnoreCase(obj.getString("response"))) {
						this.inseridos.add(designacao);
					}
				} else {
					this.designacaoErro(sb, designacao, obj.getString("mensagem"));
				}
			}
		}
		
		return sb.toString();
	}
	
	public String atualizarWeb() throws IOException {
		List<Designacao> designacoes = this.gerenciador.obterDesignacoessAtualizadas(this.ultimaSincronia.getData());
		
		StringBuffer sb = new StringBuffer();
		
		for (Designacao designacao : designacoes) {
			if (!this.inseridos.contains(designacao)) {
				PHPConnection con = new PHPConnection(this.url, HTTP_METHOD.PUT, this.hash);
				
				con.setParameter("id", designacao.getIdOnline());
				con.setParameter("status", designacao.getStatus());
				con.setParameter("tema", designacao.getTema());
				con.setParameter("fonte", designacao.getFonte());
				
				if (designacao.getEstudante() != null) {
					con.setParameter("estudante", designacao.getEstudante().getIdOnline());
					
					if (designacao.getEstudo() != null) {
						con.setParameter("estudo", designacao.getEstudo().getNrEstudo());
						
					} else { 
						con.setParameter("estudo", null);
					}
				} else {
					con.setParameter("estudante", null);
				}
				
				if (designacao.getAjudante() != null) {
					con.setParameter("ajudante", designacao.getAjudante().getIdOnline());
					
				} else {
					con.setParameter("ajudante", null);
				}
				
				if (designacao.getObservacao() == null || designacao.getObservacao().length() == 0) {
					con.setParameter("observacao", null);
					
				} else {
					con.setParameter("observacao", designacao.getObservacao());
				}
				
				if (designacao.getObsFolha() == null || designacao.getObsFolha().length() == 0) {
					con.setParameter("ObsFolha", null);
					
				} else {
					con.setParameter("ObsFolha", designacao.getObsFolha());
				}
				
				con.connect();
				
				if (con.getResponseCode() != 200) {
					this.designacaoErro(sb, designacao, con.getErrorDetails());
				
				} else {
					JSONObject obj = con.getResponse();
					
					if ("ERRO".equalsIgnoreCase(obj.getString("response"))) {
						this.designacaoErro(sb, designacao, obj.getString("mensagem"));
					
					} else {
						this.atualizados.add(designacao);
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
					
					try {
						Designacao designacao = this.gerenciador.obterDesignacao(item.getString("id"));
						
						if (this.inseridos.contains(designacao) || this.atualizados.contains(designacao)) {
							continue;
						}
						
						designacao.setIdOnline(item.getString("id"));
						designacao.setNumero(item.getInt("numero"));
						designacao.setSala(item.getString("sala"));
						designacao.setStatus(item.getString("status").charAt(0));
						designacao.setTema(item.getString("tema"));
						designacao.setFonte(item.getString("fonte"));
						designacao.setObservacao(item.getString("observacao"));
						designacao.setObsFolha(item.getString("obsfolha"));
						
						designacao.setSemana(this.gerenciador.obterSemanaDesignacao(item.getString("semana_id")));
						designacao.setData(this.sdf2.parse(item.getString("data")));
						
						String idEstudante = item.getString("estudante_id");
						
						if (idEstudante != null && idEstudante.length() > 0) {
							designacao.setEstudante(this.gerenciador.obterEstudante(idEstudante));
							
							Integer nrEstudo = item.getInt("estudo_id");
							
							if (nrEstudo != null && nrEstudo != 0) {
								designacao.setEstudo(this.gerenciador.obterEstudo(nrEstudo));
							}
						}
						
						String idAjudante = item.getString("ajudante_id");
						
						if (idAjudante != null && idAjudante.length() > 0) {
							if (idAjudante.contains("estudante")) {
								designacao.setAjudante(this.gerenciador.obterEstudante(idAjudante));
								
							} else {
								designacao.setAjudante(this.gerenciador.obterAjudante(idAjudante));
							}
						}
						
						if (designacao.getId() == 0) {
							this.gerenciador.salvar(designacao);
							
						} else {
							this.gerenciador.atualizar(designacao);
						}
					} catch (ParseException ex) {
						String erro = "Erro inesperado recebendo designações.";
						Log.getInstance().debug(erro, ex);
						
						return erro;
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
		
		List<RemoveLog> logs = this.gerenciador.obterRemovidos(Designacao.class.getSimpleName());
		
		for (RemoveLog log : logs) {
			PHPConnection con = new PHPConnection(this.url, HTTP_METHOD.DELETE, this.hash);
			
			con.setParameter("id", log.getIdOnline());
			con.connect();
			
			if (con.getResponseCode() != 200) {
				sb.append("\nDesignação: ")
					.append(log.getIdOnline())
					.append(" Mensagem: ")
					.append(con.getErrorDetails());
			
			} else {
				JSONObject obj = con.getResponse();
				
				if ("ok".equalsIgnoreCase(obj.getString("response"))) {
					this.gerenciador.remover(log);
					
				} else {
					sb.append("\nDesignação: ")
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
					
					Designacao designacao = this.gerenciador.obterDesignacao(item.getString("id"));
					
					if (designacao != null) {
						this.gerenciador.remover(designacao);
					}
				}
			} else if ("ERRO".equalsIgnoreCase(obj.getString("response"))) {
				return "\n" + obj.getString("mensagem");
			}
		}
		
		return "";
	}
	
	private void designacaoErro(StringBuffer sb, Designacao designacao, String msg) {
		sb.append("\nDesignação: ")
			.append("Nº").append(designacao.getNumero())
			.append(" De:")
			.append(this.sdf2.format(designacao.getData()))
			.append(" Mensagem: ")
			.append(msg);
	}
}