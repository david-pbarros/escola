package br.com.dbcorp.escolaMinisterio.ui.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.com.dbcorp.escolaMinisterio.Log;
import br.com.dbcorp.escolaMinisterio.entidades.Ajudante;

public class AjudanteTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 4842874392467777392L;

	private Log log = Log.getInstance();
	
	private static final int AJUDANTE = 0;
	private static final int DATA_ULTIMA_DESIGNACAO = 1;
	
	private String[] colunas = new String[] { "Nome", "Data da Ultima Designação" };
	
	private List<Ajudante> ajudantes;

	private SimpleDateFormat sdf;
	
	public AjudanteTableModel() {
		this(new ArrayList<Ajudante>());
	}
	
	public AjudanteTableModel(List<Ajudante> ajudantes) {
		this.ajudantes = ajudantes;
		
		this.sdf = new SimpleDateFormat("dd/MM/yyyy");
	}
	
	public void setItens(List<Ajudante> ajudantes) {
		this.ajudantes = ajudantes;
	}
	
	public List<Ajudante> getAjudantes() {
		return ajudantes;
	}
	
	public int getColumnCount() {
		return this.colunas.length;
	}

	public int getRowCount() {
		return this.ajudantes.size();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return this.colunas[columnIndex];
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case AJUDANTE:
		case DATA_ULTIMA_DESIGNACAO:
			return String.class;
		default:
			this.log.error("columnIndex out of bounds");
			throw new IndexOutOfBoundsException("columnIndex out of bounds");
		}
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case AJUDANTE:
			return true;
		default:
			return false;
		}
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		Ajudante ajudante = this.ajudantes.get(rowIndex);
		
		switch (columnIndex) {
		case AJUDANTE:
			return ajudante.getNome();
		case DATA_ULTIMA_DESIGNACAO:
			return ajudante.getUltimaDesignacao() != null ? sdf.format(ajudante.getUltimaDesignacao()) : "";
		default:
			this.log.error("columnIndex out of bounds");
			throw new IndexOutOfBoundsException("columnIndex out of bounds");
		}
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		Ajudante ajudante = this.ajudantes.get(rowIndex);
		
		switch (columnIndex) {
		case AJUDANTE:
			ajudante.setNome((String) aValue);
			break;
		case DATA_ULTIMA_DESIGNACAO:
			try {
				ajudante.setUltimaDesignacao(sdf.parse((String)aValue));
			} catch (ParseException e) {
				this.log.error("", e);
			}
			break;
		default:
			this.log.error("columnIndex out of bounds");
			throw new IndexOutOfBoundsException("columnIndex out of bounds");
		}
		
		fireTableCellUpdated(rowIndex, columnIndex); // Notifica a atualização da célula
	}
	
	public void limpar() {
		this.ajudantes.clear();
		fireTableDataChanged();
	}
}
