package br.com.dbcorp.escolaMinisterio.ui.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.com.dbcorp.escolaMinisterio.Log;
import br.com.dbcorp.escolaMinisterio.entidades.Profile;

public class ProfileTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -6199320224633204398L;
	
	private Log log = Log.getInstance();
	
	private static final int PROFILE = 0;
	
	private String[] colunas = new String[] { "Perfil" };
	
	private List<Profile> profiles;

	public ProfileTableModel() {
		this(new ArrayList<Profile>());
	}
	
	public ProfileTableModel(List<Profile> profiles) {
		this.profiles = profiles;
	}
	
	public void setItens(List<Profile> profiles) {
		this.profiles = profiles;
	}
	
	public List<Profile> getProfile() {
		return profiles;
	}
	
	public int getColumnCount() {
		return this.colunas.length;
	}

	public int getRowCount() {
		return this.profiles.size();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return this.colunas[columnIndex];
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case PROFILE:
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
		Profile profile = this.profiles.get(rowIndex);
		
		switch (columnIndex) {
		case PROFILE:
			return profile.getNome();
		default:
			this.log.error("columnIndex out of bounds");
			throw new IndexOutOfBoundsException("columnIndex out of bounds");
		}
	}
	
	public void limpar() {
		this.profiles.clear();
		fireTableDataChanged();
	}
}