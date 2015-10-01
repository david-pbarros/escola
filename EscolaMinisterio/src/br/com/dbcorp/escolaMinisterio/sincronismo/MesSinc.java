package br.com.dbcorp.escolaMinisterio.sincronismo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import br.com.dbcorp.escolaMinisterio.MesesDom;
import br.com.dbcorp.escolaMinisterio.dataBase.SincGerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.MesDesignacao;
import br.com.dbcorp.escolaMinisterio.entidades.Sincronismo;
import br.com.dbcorp.escolaMinisterio.sincronismo.PHPConnection.HTTP_METHOD;

public class MesSinc {

	private final String url = "/service.php/mes";
	
	private SincGerenciador gerenciador;
	private Sincronismo ultimaSincronia;
	private String hash;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	private List<MesDesignacao> inseridos;
	private List<MesDesignacao> atualizados;
	
	public MesSinc(SincGerenciador gerenciador, Sincronismo ultimaSincronia, String hash) {
		this.ultimaSincronia = ultimaSincronia;
		this.gerenciador = gerenciador;
		this.hash = hash;
		
		this.inseridos = new ArrayList<MesDesignacao>();
		this.atualizados = new ArrayList<MesDesignacao>();
	}
	
	public String enviarNovos() throws IOException {
		List<MesDesignacao> meses = this.gerenciador.obterMesesNovos();
		
		StringBuffer sb = new StringBuffer();
		
		for (MesDesignacao mes : meses) {
			PHPConnection con = new PHPConnection(this.url, HTTP_METHOD.POST, this.hash);
			
			con.setParameter("ano", mes.getAno());
			con.setParameter("status", mes.getStatus());
			con.setParameter("mes", mes.getMes().ordinal());
			
			con.connect();
			
			if (con.getResponseCode() != 200) {
				this.mesErro(sb, mes, con.getErrorDetails());
			
			} else {
				JSONObject obj = con.getResponse();
				
				if (!"ERRO".equalsIgnoreCase(obj.getString("response"))) {
					mes.setIdOnline(obj.getString("id_online"));
		
					this.gerenciador.atualizar(mes);
		
					if (!"existente".equalsIgnoreCase(obj.getString("response"))) {
						this.inseridos.add(mes);
					}
				} else {
					this.mesErro(sb, mes, obj.getString("mensagem"));
				}
			}
		}
		
		return sb.toString();
	}
	
	public String atualizarWeb() throws IOException {
		List<MesDesignacao> meses = this.gerenciador.obterMesesAtualizados(this.ultimaSincronia.getData());
		
		StringBuffer sb = new StringBuffer();
		
		for (MesDesignacao mes : meses) {
			if (!this.inseridos.contains(mes)) {
				PHPConnection con = new PHPConnection(this.url, HTTP_METHOD.PUT, this.hash);
				
				con.setParameter("status", mes.getStatus());
				con.setParameter("id", mes.getIdOnline());
				
				con.connect();
				
				if (con.getResponseCode() != 200) {
					this.mesErro(sb, mes, con.getErrorDetails());
				
				} else {
					JSONObject obj = con.getResponse();
					
					if ("ERRO".equalsIgnoreCase(obj.getString("response"))) {
						this.mesErro(sb, mes, obj.getString("mensagem"));
					
					} else {
						this.atualizados.add(mes);
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
					
					MesDesignacao mes = this.gerenciador.obterMesDesignacao(item.getString("id"));
					
					if (this.inseridos.contains(mes) || this.atualizados.contains(mes)) {
						continue;
					}
					
					mes.setIdOnline(item.getString("id"));
					mes.setAno(item.getInt("ano"));
					mes.setStatus(item.getString("status").charAt(0));
					mes.setMes(MesesDom.values()[item.getInt("mes")]);
					
					if (mes.getId() == 0) {
						this.gerenciador.salvar(mes);
						
					} else {
						this.gerenciador.atualizar(mes);
					}
				}
			} else if ("ERRO".equalsIgnoreCase(obj.getString("response"))) {
				return "\n" + obj.getString("mensagem");
			}
		}
		
		return "";
	}
	
	private void mesErro(StringBuffer sb, MesDesignacao mes, String msg) {
		sb.append("\nMes: ")
			.append(mes.getMes().toString())
			.append("/")
			.append(mes.getAno())
			.append(" Mensagem: ")
			.append(msg);
	}
}
