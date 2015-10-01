package br.com.dbcorp.escolaMinisterio.ui.model;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class EscolhaEstudanteCellRender extends DefaultTableCellRenderer implements TableCellRenderer {
	private static final long serialVersionUID = 7560420402548596840L;

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		this.setFont(new Font("Tahoma", Font.PLAIN, 13));
		this.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		
		switch (column) {
		case 0:
			setValue(value);
			this.setHorizontalAlignment(DefaultTableCellRenderer.LEFT);
			break;
		case 1:
			this.setValue(value != null ? ((Integer)value).toString() : "");
			break;
		case 2:
		case 3:
			this.setValue( (String)value);
			break;
		}
		
		if (!isSelected) {
			if (row % 2 == 0) {
				super.setBackground(Color.WHITE);
			} else {
				super.setBackground(Color.decode("#F5F5F0"));
			}
		}
		
		return this;
	}
}
