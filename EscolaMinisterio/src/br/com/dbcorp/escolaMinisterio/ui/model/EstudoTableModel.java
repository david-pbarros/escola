package br.com.dbcorp.escolaMinisterio.ui.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.com.dbcorp.escolaMinisterio.Log;
import br.com.dbcorp.escolaMinisterio.entidades.Estudo;

public class EstudoTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 6616701439106034678L;

	private Log log = Log.getInstance();
	
	private static final int LEITURA = 0;
	private static final int DEMONSTRACAO = 1;
	private static final int DISCURSO = 2;
	private static final int NUMERO = 3;
	private static final int PONTOS = 4;
	
	private String[] colunas = new String[] { "Leitura", "Demonstração", "Discurso", "Nº", "Pontos" };
	
	private List<Estudo> estudos;
	
	public EstudoTableModel() {
		this(new ArrayList<Estudo>());
	}
	
	public EstudoTableModel(List<Estudo> estudos) {
		this.estudos = estudos;
	}
	
	public void setItens(List<Estudo> estudos) {
		this.estudos = estudos;
	}
	
	public int getColumnCount() {
		return this.colunas.length;
	}

	public int getRowCount() {
		return this.estudos.size();
	}

	public List<Estudo> getEstudos() {
		return this.estudos;
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		return this.colunas[columnIndex];
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case LEITURA:
		case DEMONSTRACAO:
		case DISCURSO:
			return Boolean.class;
		case NUMERO:
			return Integer.class;
		case PONTOS:
			return String.class;
		default:
			this.log.error("columnIndex out of bounds");
			throw new IndexOutOfBoundsException("columnIndex out of bounds");
		}
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case LEITURA:
		case DEMONSTRACAO:
		case DISCURSO:
			return true;
		default:
			return false;
		}
	}
	
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		Estudo estudo = this.estudos.get(rowIndex);
		
		switch (columnIndex) {
		case LEITURA:
			return estudo.isLeitura();
		case DEMONSTRACAO:
			return estudo.isDemonstracao();
		case DISCURSO:
			return estudo.isDiscurso();
		case NUMERO:
			return estudo.getNrEstudo();
		case PONTOS:
			return estudo.getDescricao();
		default:
			this.log.error("columnIndex out of bounds");
			throw new IndexOutOfBoundsException("columnIndex out of bounds");
		}
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		Estudo estudo = this.estudos.get(rowIndex);
		
		switch (columnIndex) {
		case LEITURA:
			estudo.setLeitura((Boolean) aValue);
			break;
		case DEMONSTRACAO:
			estudo.setDemonstracao((Boolean) aValue);
			break;
		case DISCURSO:
			estudo.setDiscurso((Boolean) aValue);
			break;
		case NUMERO:
			estudo.setNrEstudo((Integer) aValue);
			break;
		case PONTOS:
			estudo.setDescricao((String) aValue);
			break;
		default:
			this.log.error("columnIndex out of bounds");
			throw new IndexOutOfBoundsException("columnIndex out of bounds");
		}
		
		fireTableCellUpdated(rowIndex, columnIndex); // Notifica a atualização da célula
	}
	
	public void limpar() {
		this.estudos.clear();
		fireTableDataChanged();
	}
}
