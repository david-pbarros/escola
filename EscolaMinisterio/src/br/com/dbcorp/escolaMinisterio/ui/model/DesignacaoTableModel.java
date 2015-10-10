package br.com.dbcorp.escolaMinisterio.ui.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import br.com.dbcorp.escolaMinisterio.entidades.Designacao;
import br.com.dbcorp.escolaMinisterio.entidades.Estudo;
import br.com.dbcorp.escolaMinisterio.ui.Params;

public class DesignacaoTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 6616701439106034678L;

	private Map<Integer, Integer> colunasEstudos;
	private Map<Integer, String> colunasObservacao; 
	
	private List<Estudo> estudos;
	private Map<Integer, Designacao> designacoes;
	
	public DesignacaoTableModel() {
		this(new ArrayList<Estudo>());
	}
	
	public DesignacaoTableModel(List<Estudo> estudos) {
		this(estudos, new HashMap<Integer, Designacao>());
	}
	
	public DesignacaoTableModel(List<Estudo> estudos, Map<Integer, Designacao> designacoes) {
		this.estudos = estudos;
		this.designacoes = designacoes;
		
		this.colunasEstudos = new HashMap<Integer, Integer>();
		this.colunasObservacao = new HashMap<Integer, String>();
	}
	
	public void setEstudos(List<Estudo> estudos) {
		this.estudos = estudos;
	}
	
	public void setDesignacaoes(Map<Integer, Designacao> designacoes) {
		this.designacoes = designacoes;
	}
	
	public int getColumnCount() {
		return this.estudos.size();
	}

	public int getRowCount() {
		return 1;
	}

	public List<Estudo> getEstudos() {
		return this.estudos;
	}
	
	public Map<Integer, Designacao> getDesignacoes() {
		return designacoes;
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		this.colunasEstudos.put(columnIndex, this.estudos.get(columnIndex).getNrEstudo());
		
		return Integer.toString(this.estudos.get(columnIndex).getNrEstudo());
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return Character.class;
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		Designacao designacao = this.designacoes.get(this.colunasEstudos.get(columnIndex));
		
		if (designacao != null) {
			String obs = (designacao.getData() != null ? designacao.getData().format(Params.dateFormate()) : "");
			obs += designacao.getObservacao() != null ? " - " + designacao.getObservacao() : "";
			this.colunasObservacao.put(columnIndex, obs);
			
			return designacao.getStatus();
		}
		
		return null;
	}
	
	public void limpar() {
		this.estudos.clear();
		this.designacoes.clear();
		fireTableDataChanged();
	}
	
	public String getObservacao(int columnIndex) {
		return this.colunasObservacao.get(columnIndex);
	}
}
