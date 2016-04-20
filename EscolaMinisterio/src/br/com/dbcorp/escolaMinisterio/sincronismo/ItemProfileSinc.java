package br.com.dbcorp.escolaMinisterio.sincronismo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.dbcorp.escolaMinisterio.dataBase.SincGerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.ItemProfile;
import br.com.dbcorp.escolaMinisterio.entidades.ItemProfile.ItensSeg;
import br.com.dbcorp.escolaMinisterio.entidades.RemoveLog;
import br.com.dbcorp.escolaMinisterio.entidades.Sincronismo;
import br.com.dbcorp.escolaMinisterio.sincronismo.PHPConnection.HTTP_METHOD;
import br.com.dbcorp.escolaMinisterio.ui.Params;

public class ItemProfileSinc {

	private final String url = "/service.php/itemProfile";
	
	private SincGerenciador gerenciador;
	private Sincronismo ultimaSincronia;
	private String hash;
	
	private List<ItemProfile> inseridos;
	
	public ItemProfileSinc(SincGerenciador gerenciador, Sincronismo ultimaSincronia, String hash) {
		this.ultimaSincronia = ultimaSincronia;
		this.gerenciador = gerenciador;
		this.hash = hash;
		
		this.inseridos = new ArrayList<ItemProfile>();
	}
	
	public String enviarNovos() throws IOException {
		List<ItemProfile> itens = this.gerenciador.obterItemProfilesNovos();
		
		StringBuffer sb = new StringBuffer();
		
		for (ItemProfile item : itens) {
			PHPConnection con = new PHPConnection(this.url, HTTP_METHOD.POST, this.hash);
			
			con.setParameter("item", item.getItem().ordinal());
			con.setParameter("profile", item.getProfile().getIdOnline());
			con.connect();
			
			if (con.getResponseCode() != 200) {
				this.itemErro(sb, item, con.getErrorDetails());
			
			} else {
				JSONObject obj = con.getResponse();
				
				if (!"ERRO".equalsIgnoreCase(obj.getString("response"))) {
					item.setIdOnline(obj.getString("id_online"));
		
					this.gerenciador.atualizar(item);
		
					if (!"existente".equalsIgnoreCase(obj.getString("response"))) {
						this.inseridos.add(item);
					}
				} else {
					this.itemErro(sb, item, obj.getString("mensagem"));
				}
			}
		}
		
		return sb.toString();
	}

	public String obterNovos() throws IOException, JSONException {
		PHPConnection con = new PHPConnection(this.url, HTTP_METHOD.GET, this.hash);
		
		con.setParameter("data_ultima", this.ultimaSincronia.getDateTime().format(Params.dateTimeFormate()));
		con.connect();
		
		if (con.getResponseCode() != 200) {
			return "\n" + con.getErrorDetails();
		
		} else {
			JSONObject obj = con.getResponse();
			
			if ("ok".equalsIgnoreCase(obj.getString("response"))) {
				JSONArray array = obj.getJSONArray("itens");
				
				for (int i = 0; i < array.length(); i++) {
					JSONObject item = array.getJSONObject(i);
					
					ItemProfile itemP = this.gerenciador.obterItemProfile(item.getString("id"));
					
					if (this.inseridos.contains(itemP)) {
						continue;
					}
					
					itemP.setIdOnline(item.getString("id"));
					itemP.setItem(ItensSeg.values()[item.getInt("item")]);
					itemP.setProfile(this.gerenciador.obterProfile(item.getString("profile_id")));
					
					if (itemP.getId() == 0) {
						this.gerenciador.salvar(itemP);
						
					} else {
						this.gerenciador.atualizar(itemP);
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
		
		List<RemoveLog> logs = this.gerenciador.obterRemovidos(ItemProfile.class.getSimpleName());
		
		for (RemoveLog log : logs) {
			PHPConnection con = new PHPConnection(this.url, HTTP_METHOD.DELETE, this.hash);
			
			con.setParameter("id", log.getIdOnline());
			con.connect();
			
			if (con.getResponseCode() != 200) {
				sb.append("\nItem Acesso: ")
					.append(log.getIdOnline())
					.append(" Mensagem: ")
					.append(con.getErrorDetails());
			
			} else {
				JSONObject obj = con.getResponse();
				
				if ("ok".equalsIgnoreCase(obj.getString("response"))) {
					this.gerenciador.remover(log);
					
				} else {
					sb.append("\nItem Acesso: ")
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
					
					ItemProfile itemP = this.gerenciador.obterItemProfile(item.getString("id"));
					
					if (itemP != null) {
						this.gerenciador.remover(itemP);
					}
				}
			} else if ("ERRO".equalsIgnoreCase(obj.getString("response"))) {
				return "\n" + obj.getString("mensagem");
			}
		}
		
		return "";
	}
	
	private void itemErro(StringBuffer sb, ItemProfile item, String msg) {
		sb.append("\nItem Acesso: ")
			.append(item.getItem())
			.append(" Mensagem: ")
			.append(msg);
	}
}