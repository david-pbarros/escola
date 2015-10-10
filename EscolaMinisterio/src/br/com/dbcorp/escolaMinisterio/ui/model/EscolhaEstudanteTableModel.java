package br.com.dbcorp.escolaMinisterio.ui.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.com.dbcorp.escolaMinisterio.Log;
import br.com.dbcorp.escolaMinisterio.entidades.Designacao;
import br.com.dbcorp.escolaMinisterio.entidades.Estudante;
import br.com.dbcorp.escolaMinisterio.ui.Params;

public class EscolhaEstudanteTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -6199320224633204398L;
	
	private Log log = Log.getInstance();
	
	private static final int NOME = 0;
	private static final int ESTUDO = 1;
	private static final int DATA = 2;
	private static final int AVALIACAO = 3;
	
	private String[] colunas = new String[] { "Nome", "Ult. Designação", "Dt. Ult. Designação", "Avaliação" };
	
	private List<Estudante> estudantes;

	public EscolhaEstudanteTableModel() {
		this(new ArrayList<Estudante>());
	}
	
	public EscolhaEstudanteTableModel(List<Estudante> estudantes) {
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
		case DATA:
		case AVALIACAO:
			return String.class;
		case ESTUDO:
			return Integer.class;
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
		Estudante estudante = this.estudantes.get(rowIndex);

		String data = null;
		String avaliacao = null;
		Integer estudo = null;
		
		if (estudante.getUltimaDesignacao() != null && estudante.getDesignacoes() != null) {
			for (Designacao designacao : estudante.getDesignacoes()) {
				if (designacao.getData().equals(estudante.getUltimaDesignacao())) {
					data = designacao.getData().format(Params.dateFormate());
					avaliacao = this.avaliacao(designacao.getStatus());
					estudo = designacao.getEstudo() != null ? designacao.getEstudo().getNrEstudo() : null;
					
					break;
				}
			}
		}
		
		switch (columnIndex) {
		case NOME:
			return estudante.getNome();
		case DATA:
			return data;
		case AVALIACAO:
			return avaliacao;
		case ESTUDO:
			return estudo;
		default:
			this.log.error("columnIndex out of bounds");
			throw new IndexOutOfBoundsException("columnIndex out of bounds");
		}
	}
	
	public void limpar() {
		this.estudantes.clear();
		fireTableDataChanged();
	}
	
	private String avaliacao(char status) {
		switch (status) {
		case 'P':
			return "Passou";
		case 'F':
			return "Falhor";
		default:
			return "Não avaliado";
		}
	}
}