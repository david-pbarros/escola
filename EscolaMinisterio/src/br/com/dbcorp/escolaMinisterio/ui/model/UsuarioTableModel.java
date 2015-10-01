package br.com.dbcorp.escolaMinisterio.ui.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.com.dbcorp.escolaMinisterio.Log;
import br.com.dbcorp.escolaMinisterio.entidades.Usuario;

public class UsuarioTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -6199320224633204398L;
	
	private Log log = Log.getInstance();
	
	private static final int NOME = 0;
	
	private String[] colunas = new String[] { "Nome" };
	
	private List<Usuario> usuarios;

	public UsuarioTableModel() {
		this(new ArrayList<Usuario>());
	}
	
	public UsuarioTableModel(List<Usuario> usuario) {
		this.usuarios = usuario;
	}
	
	public void setItens(List<Usuario> usuario) {
		this.usuarios = usuario;
	}
	
	public List<Usuario> getUsuario() {
		return usuarios;
	}
	
	public int getColumnCount() {
		return this.colunas.length;
	}

	public int getRowCount() {
		return this.usuarios.size();
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
		Usuario usuario = this.usuarios.get(rowIndex);
		
		switch (columnIndex) {
		case NOME:
			return usuario.getNome();
		default:
			this.log.error("columnIndex out of bounds");
			throw new IndexOutOfBoundsException("columnIndex out of bounds");
		}
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		Usuario usuario = this.usuarios.get(rowIndex);
		
		switch (columnIndex) {
		case NOME:
			usuario.getNome();
			break;
		default:
			this.log.error("columnIndex out of bounds");
			throw new IndexOutOfBoundsException("columnIndex out of bounds");
		}
		
		fireTableCellUpdated(rowIndex, columnIndex); // Notifica a atualização da célula
	}
		
	
	public void limpar() {
		this.usuarios.clear();
		fireTableDataChanged();
	}
}