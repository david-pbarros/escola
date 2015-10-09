package br.com.dbcorp.escolaMinisterio.sincronismo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import br.com.dbcorp.escolaMinisterio.Log;
import br.com.dbcorp.escolaMinisterio.dataBase.SincGerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.SemanaDesignacao;
import br.com.dbcorp.escolaMinisterio.entidades.Sincronismo;
import br.com.dbcorp.escolaMinisterio.sincronismo.PHPConnection.HTTP_METHOD;
import br.com.dbcorp.escolaMinisterio.ui.Params;

public class SemanaSinc {

	private final String url = "/service.php/semana";
	
	private SincGerenciador gerenciador;
	private Sincronismo ultimaSincronia;
	private String hash;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MMM/yy");
	
	private List<SemanaDesignacao> inseridos;
	private List<SemanaDesignacao> atualizados;
	
	public SemanaSinc(SincGerenciador gerenciador, Sincronismo ultimaSincronia, String hash) {
		this.ultimaSincronia = ultimaSincronia;
		this.gerenciador = gerenciador;
		this.hash = hash;
		
		this.inseridos = new ArrayList<SemanaDesignacao>();
		this.atualizados = new ArrayList<SemanaDesignacao>();
	}
	
	public String enviarNovos() throws IOException {
		List<SemanaDesignacao> semanas = this.gerenciador.obterSemanasNovas();
		
		StringBuffer sb = new StringBuffer();
		
		for (SemanaDesignacao semana : semanas) {
			PHPConnection con = new PHPConnection(this.url, HTTP_METHOD.POST, this.hash);
			
			con.setParameter("data", sdf.format(semana.getData()));
			con.setParameter("visita", semana.isVisita() ? 1 : 0);
			con.setParameter("assebleia", semana.isAssebleia() ? 1 : 0);
			con.setParameter("recapitulacao", semana.isRecapitulacao() ? 1 : 0);
			con.setParameter("semReuniao", semana.isSemReuniao() ? 1 : 0);
			con.setParameter("mes", semana.getMes().getIdOnline());
			
			con.connect();
			
			if (con.getResponseCode() != 200) {
				this.semanaErro(sb, semana, con.getErrorDetails());
			
			} else {
				JSONObject obj = con.getResponse();
				
				if (!"ERRO".equalsIgnoreCase(obj.getString("response"))) {
					semana.setIdOnline(obj.getString("id_online"));
		
					this.gerenciador.atualizar(semana);
		
					if (!"existente".equalsIgnoreCase(obj.getString("response"))) {
						this.inseridos.add(semana);
					}
				} else {
					this.semanaErro(sb, semana, obj.getString("mensagem"));
				}
			}
		}
		
		return sb.toString();
	}
	
	public String atualizarWeb() throws IOException {
		List<SemanaDesignacao> semanas = this.gerenciador.obterSemanasAtualizadas(this.ultimaSincronia.getData());
		
		StringBuffer sb = new StringBuffer();
		
		for (SemanaDesignacao semana : semanas) {
			if (!this.inseridos.contains(semana)) {
				PHPConnection con = new PHPConnection(this.url, HTTP_METHOD.PUT, this.hash);
				
				con.setParameter("id", semana.getIdOnline());
				con.setParameter("visita", semana.isVisita() ? 1 : 0);
				con.setParameter("assebleia", semana.isAssebleia() ? 1 : 0);
				con.setParameter("recapitulacao", semana.isRecapitulacao() ? 1 : 0);
				con.setParameter("semReuniao", semana.isSemReuniao() ? 1 : 0);
				
				con.connect();
				
				if (con.getResponseCode() != 200) {
					this.semanaErro(sb, semana, con.getErrorDetails());
				
				} else {
					JSONObject obj = con.getResponse();
					
					if ("ERRO".equalsIgnoreCase(obj.getString("response"))) {
						this.semanaErro(sb, semana, obj.getString("mensagem"));
					
					} else {
						this.atualizados.add(semana);
					}
				}
			}
		}
		
		return sb.toString();
	}

	public String obterNovos() throws IOException {
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
					
					SemanaDesignacao semana = this.gerenciador.obterSemanaDesignacao(item.getString("id"));
					
					if (this.inseridos.contains(semana) || this.atualizados.contains(semana)) {
						continue;
					}
					
					try {
						semana.setData(LocalDate.parse(item.getString("data"), Params.dateFormate()));
						semana.setIdOnline(item.getString("id"));
						semana.setVisita(item.getInt("visita") == 1);
						semana.setAssebleia(item.getInt("assebleia") == 1);
						semana.setRecapitulacao(item.getInt("recapitulacao") == 1);
						semana.setSemReuniao(item.getInt("semreuniao") == 1);
						semana.setMes(this.gerenciador.obterMesDesignacao(item.getString("mes_id")));
						
						if (semana.getId() == 0) {
							this.gerenciador.salvar(semana);
							
						} else {
							this.gerenciador.atualizar(semana);
						}
					} catch (DateTimeParseException ex) {
						String erro = "Erro inesperado recebendo dias de reunião.";
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
	
	private void semanaErro(StringBuffer sb, SemanaDesignacao semana, String msg) {
		sb.append("\nSemana: ")
			.append(this.sdf2.format(semana.getData()))
			.append(" Mensagem: ")
			.append(msg);
	}
}
