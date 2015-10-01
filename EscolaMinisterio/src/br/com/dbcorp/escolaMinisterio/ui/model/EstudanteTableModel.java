package br.com.dbcorp.escolaMinisterio.ui.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.com.dbcorp.escolaMinisterio.Log;
import br.com.dbcorp.escolaMinisterio.entidades.Estudante;

public class EstudanteTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -6199320224633204398L;
	
	private Log log = Log.getInstance();
	
	private static final int NOME = 0;
	private static final int DESABILITADO = 1;
	private static final int NAO_AJUDANTE = 2;
	
	private String[] colunas = new String[] { "Nome", "Desabilitado", "Não Ajudante" };
	
	private List<Estudante> estudantes;

	public EstudanteTableModel() {
		this(new ArrayList<Estudante>());
	}
	
	public EstudanteTableModel(List<Estudante> estudantes) {
		this.estudantes = estudantes;
	}
	
	public void setItens(List<Estudante> estudantes) {
		this.estudantes = estudantes;
	}
	
	public List<Estudante> getEstudantes() {
		return estudantes;
	}
	
	public int getColumnCount() {
		return this.colunas.length;
	}

	public int getRowCount() {
		return this.estudantes.size();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return this.colunas[columnIndex];
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case NOME:
			return String.class;
		case DESABILITADO:
		case NAO_AJUDANTE:
			return Boolean.class;
		default:
			this.log.error("columnIndex out of bounds");
			throw new IndexOutOfBoundsException("columnIndex out of bounds");
		}
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case DESABILITADO:
		case NAO_AJUDANTE:
			return true;
		default:
			return false;
		}
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		Estudante estudante = this.estudantes.get(rowIndex);
		
		switch (columnIndex) {
		case NOME:
			return estudante.getNome();
		case DESABILITADO:
			return estudante.isDesabilitado();
		case NAO_AJUDANTE:
			return estudante.isNaoAjudante();
		default:
			this.log.error("columnIndex out of bounds");
			throw new IndexOutOfBoundsException("columnIndex out of bounds");
		}
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		Estudante estudante = this.estudantes.get(rowIndex);
		
		switch (columnIndex) {
		case DESABILITADO:
			estudante.setDesabilitado((Boolean) aValue);
			break;
		case NAO_AJUDANTE:
			estudante.setNaoAjudante((Boolean) aValue);
			break;
		default:
			this.log.error("columnIndex out of bounds");
			throw new IndexOutOfBoundsException("columnIndex out of bounds");
		}
		
		fireTableCellUpdated(rowIndex, columnIndex); // Notifica a atualização da célula
	}
		
	
	public void limpar() {
		this.estudantes.clear();
		fireTableDataChanged();
	}
}