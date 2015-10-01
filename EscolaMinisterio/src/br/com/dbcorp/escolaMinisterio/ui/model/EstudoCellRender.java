package br.com.dbcorp.escolaMinisterio.ui.model;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class EstudoCellRender implements TableCellRenderer {

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		switch (column) {
		case 0:
		case 1:
		case 2:
			boolean selecionado = (Boolean)value;
			
			JCheckBox check = new JCheckBox("", selecionado);
			check.setHorizontalAlignment(JCheckBox.CENTER);
			
			if (selecionado) {
				if (column == 0) {
					check.setBackground(Color.ORANGE);
					
				} else if (column == 1) {
					check.setBackground(Color.decode("#FFFF66"));
					
				} else {
					check.setBackground(Color.decode("#FF3333"));
				}
			} else {
				check.setBackground(Color.WHITE);
			}
			
			
			return check;

		case 3:
			JLabel label = new JLabel( ((Integer)value).toString() );
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setFont(new Font("Tahoma", Font.PLAIN, 13));
			return label;
		default:
			return null;
		}
	}
}
