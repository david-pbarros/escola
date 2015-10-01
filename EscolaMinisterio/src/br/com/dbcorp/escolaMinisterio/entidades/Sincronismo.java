package br.com.dbcorp.escolaMinisterio.entidades;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Sincronismo {
	private int id;
	private Date data;
	private boolean sucesso;
	private boolean criado;
	
	@Id @GeneratedValue(strategy=GenerationType.TABLE)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	
	public boolean isSucesso() {
		return sucesso;
	}
	public void setSucesso(boolean sucesso) {
		this.sucesso = sucesso;
	}
	
	@Transient
	public boolean isCriado() {
		return criado;
	}
	public void setCriado(boolean criado) {
		this.criado = criado;
	}
}
