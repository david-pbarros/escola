package br.com.dbcorp.escolaMinisterio.ui.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.com.dbcorp.escolaMinisterio.Log;
import br.com.dbcorp.escolaMinisterio.ui.Params;

public class EstudoTableModalModel extends AbstractTableModel {
	private static final long serialVersionUID = 6616701439106034678L;

	private Log log = Log.getInstance();
	
	private static final int NUMERO = 0;
	private static final int LEITURA = 1;
	private static final int DEMONSTRACAO = 2;
	private static final int DISCURSO = 3;
	private static final int PONTOS = 4;
	private static final int STATUS = 5;
	
	private String[] colunas = new String[] { "Nº", "Leitura", "Demonstração", "Discurso", "Pontos", "Situação" };
	
	private List<EstudoDecorator> estudos;
	
	public EstudoTableModalModel() {
		this(new ArrayList<EstudoDecorator>());
	}
	
	public EstudoTableModalModel(List<EstudoDecorator> estudos) {
		this.estudos = estudos;
	}
	
	public void setItens(List<EstudoDecorator> estudos) {
		this.estudos = estudos;
	}
	
	public int getColumnCount() {
		return this.colunas.length;
	}

	public int getRowCount() {
		return this.estudos.size();
	}

	public List<EstudoDecorator> getEstudos() {
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
		case STATUS:
			return String.class;
		default:
			this.log.error("columnIndex out of bounds");
			throw new IndexOutOfBoundsException("columnIndex out of bounds");
		}
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}
	
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		EstudoDecorator estudo = this.estudos.get(rowIndex);
		
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
		case STATUS:
			return (estudo.getAvaliacao() != null ? estudo.getAvaliacao().getLabel() : "") + 
					(estudo.getData() != null ?	" em " + estudo.getData().format(Params.dateFormate()) : "");
			
		default:
			this.log.error("columnIndex out of bounds");
			throw new IndexOutOfBoundsException("columnIndex out of bounds");
		}
	}
}
