package br.com.dbcorp.escolaMinisterio.entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class RemoveLog {
	private int id;
	private String tabela;
	private String idOnline;
	
	@Id @GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTabela() {
		return tabela;
	}
	public void setTabela(String tabela) {
		this.tabela = tabela;
	}
	
	public String getIdOnline() {
		return idOnline;
	}
	public void setIdOnline(String idOnline) {
		this.idOnline = idOnline;
	}
}
